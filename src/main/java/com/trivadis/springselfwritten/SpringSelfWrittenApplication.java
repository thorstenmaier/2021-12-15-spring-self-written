package com.trivadis.springselfwritten;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class SpringSelfWrittenApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringSelfWrittenApplication.class);

        A a = applicationContext.getBean(A.class);

        a.doIt();
    }

}
