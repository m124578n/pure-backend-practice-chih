package shun_chih.com.chih.member;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;

    public MemberService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.memberRepository = memberRepository;
    }
}
