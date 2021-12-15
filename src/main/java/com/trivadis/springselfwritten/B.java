package com.trivadis.springselfwritten;

import org.springframework.stereotype.Component;

@Component
public class B {

    public B() {
        System.out.println("CREATE B"); // 2
    }

    public void doIt() {
        System.out.println("B.doIt");
    }
}
