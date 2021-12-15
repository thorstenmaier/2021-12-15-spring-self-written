package com.trivadis.springselfwritten;

import org.springframework.ProceedingJoinPoint;

public class PerformanceAspect {

//    @Around("execution(com.trivadis.springselfwritten.B.doIt())")
    public void performance(ProceedingJoinPoint proceedingJoinPoint) {
        long before = System.currentTimeMillis();

        try {
            proceedingJoinPoint.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long after = System.currentTimeMillis();
        long duration = after - before;
        System.out.println("Duration " + duration + " ms");
    }
}
