package shun_chih.com.chih.config.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import shun_chih.com.chih.member.MemberRepository;

import java.util.List;

@Component
public class SpringUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public SpringUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository
                .findByUsername(username)
                .map(v -> new User(
                        v.getUsername(),
                        v.getPassword(),
                        List.of(new SimpleGrantedAuthority(v.getRole().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }
}
