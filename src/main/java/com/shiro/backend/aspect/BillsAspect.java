package com.shiro.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BillsAspect {
    // 定义一个切点，用于匹配BillsServiceImpl类中的所有方法
    @Pointcut("execution(* com.shiro.backend..service.impl.BillsServiceImpl.*(..))")
    public void billsServicePointcut() {
    }

    @Before("billsServicePointcut()")
    public void beforeMethod(JoinPoint joinPoint) {
        // 在方法执行前打印日志
        String methodName = joinPoint.getSignature().getName();
        log.info("即将开始执行" + methodName + "方法");
    }

    @After("billsServicePointcut()")
    public void afterMethod(JoinPoint joinPoint) {
        // 在方法执行后打印日志
        String methodName = joinPoint.getSignature().getName();
        log.info(methodName + "方法执行完毕");
    }

    /**
     * 环绕通知，可以在方法执行前后都进行处理
     */
    @Around("billsServicePointcut()")
    public Object aroundBillsCrudOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 计算执行时间
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("{}方法执行完成，耗时：{}ms", methodName, executionTime);

        return result;
    }
}
