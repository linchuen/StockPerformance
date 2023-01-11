package cooba.stockPerformance.AOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopAspect {

    @After("@annotation(com.vincent.demo.aop.SendEmail)")
    public void sendEmail(JoinPoint joinPoint) {
        // TODO
    }
}
