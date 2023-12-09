package com.synpulse8.ebanking.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.synpulse8.ebanking..*)")
    public void applicationPackagePointcut() {
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var clazz = joinPoint.getTarget().getClass();
        var parameter = joinPoint.getArgs();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            // TODO omit sensitive data
            // example: [2023-12-08 15:15:12.683] [http-nio-8080-exec-6] [ERROR] [LoggingAspect.logAround :50] [class:com.synpulse8.ebanking.auth.controllers.AuthControllerImpl method:login param:[LoginReq[uid=P-0123456789, password=12345]] error:Bad credentials]
            log.error("class:{} method:{} param:{} error:{}", clazz.getName(), joinPoint.getSignature().getName(), parameter, e.getMessage(), e);
            throw e;
        }
        return result;
    }
}
