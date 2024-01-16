package sogong.ctf.mockConfig;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;

import java.util.List;

/*
 * Security Context를 생성해주는 class
 */
public class WithCustomMockAdminSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockAdmin annotation) {
        String username = annotation.username();
        String role = annotation.role();

        Member member = Member.builder()
                .username(username)
                .password("adminpassword!")
                .nickname("admin")
                .role(Role.valueOf(role))
                .build();

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customMemberDetails, "password", List.of(new SimpleGrantedAuthority(role)));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

        return context;
    }
}

