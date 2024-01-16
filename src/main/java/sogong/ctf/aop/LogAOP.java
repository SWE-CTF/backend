package sogong.ctf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAOP {
    @Before("execution(* sogong.ctf.controller.*.*(..))")
    public void logBeforeController(JoinPoint jp) {
        log.info(jp.getSignature().getName());
    }
}
