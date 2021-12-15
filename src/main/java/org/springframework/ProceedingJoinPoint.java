package org.springframework;

import java.util.concurrent.Callable;

public abstract class ProceedingJoinPoint implements Callable<String> {

    public void proceed() throws Exception {
        call();
    }

}
