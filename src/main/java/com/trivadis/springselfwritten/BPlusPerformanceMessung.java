package com.trivadis.springselfwritten;

import org.springframework.ProceedingJoinPoint;

public class BPlusPerformanceMessung extends B {

    @Override
    public void doIt() {
        new PerformanceAspect().performance(new ProceedingJoinPoint() {
            @Override
            public String call() throws Exception {
                BPlusPerformanceMessung.super.doIt();
                return null;
            }
        });
    }
}
