package org.springframework.boot;

import org.springframework.context.ApplicationContext;

public class SpringApplication {

    public static ApplicationContext run(Class config) {
        return new ApplicationContext(config);
    }

}
