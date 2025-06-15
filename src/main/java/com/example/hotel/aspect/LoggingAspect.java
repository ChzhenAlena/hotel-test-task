package com.example.hotel.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.example.hotel.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        if (log.isTraceEnabled()) {
            log.trace("Enter: {}", methodName);
            Object[] args = joinPoint.getArgs();
            log.trace("Arguments: {}", Arrays.toString(args));
        }

        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            if (log.isTraceEnabled()) {
                log.trace("Exit: {} with result: {}", methodName, result);
            }
            return result;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.trace("Execution time of {}: {} ms", methodName, elapsed);
        }
    }
}

