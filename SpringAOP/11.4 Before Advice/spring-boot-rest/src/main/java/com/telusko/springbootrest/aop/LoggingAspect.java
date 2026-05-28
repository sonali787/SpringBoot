package com.telusko.springbootrest.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

	// return type , class-name.method name(args)

	@Before("execution (* com.telusko.springbootrest.service.JobService.*(..))")
	public void logMethodCall(JoinPoint jp) {
		LOGGER.info("Class " + jp.getSignature().getDeclaringTypeName());
		LOGGER.info("Method Called " + jp.getSignature().getName());
		LOGGER.info("Return Type " + ((MethodSignature) jp.getSignature()).getReturnType());
		LOGGER.info("Argument " + java.util.Arrays.toString(jp.getArgs()));
	}

	@After("execution (* com.telusko.springbootrest.service.JobService.*(..))")
	public void logMethodExit(JoinPoint jp) {
		LOGGER.info("Method Exited " + jp.getSignature().getName());
	}

	@AfterThrowing("execution (* com.telusko.springbootrest.service.JobService.*(..))")
	public void logMethodException(JoinPoint jp) {
		LOGGER.info("Method Threw Exception " + jp.getSignature().getName());
	}

}
