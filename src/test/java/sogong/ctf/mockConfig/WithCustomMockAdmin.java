package sogong.ctf.mockConfig;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockAdminSecurityContextFactory.class) // Security Context를 커스텀하게 생성하여 사용하도록 설정
public @interface WithCustomMockAdmin {

    String username() default "admin";
    String role() default "ROLE_ADMIN";
}
