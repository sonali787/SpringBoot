package com.telusko.springbootrest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidationAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

    @Around("execution (* com.telusko.springbootrest.service.JobService.getJob(..)) && args(postId)")
    public Object validate(ProceedingJoinPoint jp, int postId) throws Throwable {
        if (postId < 0) {
            postId = -1 * postId;
            LOGGER.info("PostId is negative , changing it to positive");
        }
        Object result = jp.proceed(new Object[] { postId });
        return result;
    }
}
