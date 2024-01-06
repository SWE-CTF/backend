package sogong.ctf.mockConfig;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class) // Security Context를 커스텀하게 생성하여 사용하도록 설정
public @interface WithCustomMockUser {

    String username() default "member1";
    String role() default "ROLE_MEMBER";
}
