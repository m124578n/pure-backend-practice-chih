package shun_chih.com.chih.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shun_chih.com.chih.member.data.enu.MemberLoginType;
import shun_chih.com.chih.member.data.enu.MemberRole;
import shun_chih.com.chih.member.data.enu.MemberStatus;
import shun_chih.com.chih.member.data.po.MemberLoginDocument;
import shun_chih.com.chih.member.data.po.MemberPO;
import shun_chih.com.chih.wallet.WalletRepository;
import shun_chih.com.chih.wallet.data.enu.WalletStatus;
import shun_chih.com.chih.wallet.data.po.WalletPO;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final WalletRepository walletRepository;
    private final MemberLoginRepository memberLoginRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate;

    @Value("${custom.avatar.uri}")
    private String avatarUri;

    public MemberService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MemberRepository memberRepository, WalletRepository walletRepository, MemberLoginRepository memberLoginRepository, StringRedisTemplate stringRedisTemplate, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
        this.walletRepository = walletRepository;
        this.memberLoginRepository = memberLoginRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "/10 * * * * *")
    public void fetchRandomAvatarLink() {
        Optional.ofNullable(restTemplate.getForEntity(avatarUri, String.class).getBody())
                .map(v -> stringRedisTemplate.opsForSet().add("randomAvatarLinks", v));
    }

    @Cacheable(cacheNames = {"members"}, key = "#username")
    public Optional<MemberPO> query(String username) {
        return memberRepository.findByUsername(username);
    }

    @CachePut(cacheNames = {"members"}, key = "#result.username")
    public Optional<MemberPO> login(String username, String password) {
        return memberRepository
                .findByUsername(username)
                .map(v -> {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                    memberLoginRepository.save(
                            MemberLoginDocument
                                    .builder()
                                    .id(null)
                                    .username(username)
                                    .type(MemberLoginType.LOGIN)
                                    .build()
                    );

                    return v;
                })
                .or(() -> {
                    String randomAvatarLink = stringRedisTemplate.opsForSet().randomMember("randomAvatarLinks");
                    if (randomAvatarLink.isBlank()) {
                        randomAvatarLink = restTemplate.getForEntity(avatarUri, String.class).getBody();
                    }

                    MemberRole role = MemberRole.ROLE_USER;
                    if (username.equals("admin")) {
                        role = MemberRole.ROLE_ADMIN;
                    }

                    final MemberPO memberPO = memberRepository.save(
                            MemberPO.builder()
                                    .id(null)
                                    .username(username)
                                    .password(passwordEncoder.encode(password))
                                    .about("")
                                    .avatarLink(String.format("https://random.dog/%s", randomAvatarLink))
                                    .role(role)
                                    .status(MemberStatus.ACTIVE)
                                    .build()
                    );
                    walletRepository.save(
                            WalletPO.builder()
                                    .id(null)
                                    .memberId(memberPO.getId())
                                    .balance(BigDecimal.ZERO)
                                    .status(WalletStatus.ACTIVE)
                                    .build()
                    );
                    memberLoginRepository.save(
                            MemberLoginDocument
                                    .builder()
                                    .id(null)
                                    .username(username)
                                    .type(MemberLoginType.SIGNUP)
                                    .build()
                    );

                    return Optional.of(memberPO);
                });
    }
}
