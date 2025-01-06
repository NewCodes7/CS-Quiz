package newcodes.CSQuiz.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GlobalLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(GlobalLoggingAspect.class);

    @Around("execution(* newcodes.CSQuiz..*.*(..))")
    public Object logAll(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("시작: {}", methodName);
        Object result = joinPoint.proceed();
        log.info("종료: {}", methodName);
        return result;
    }
}