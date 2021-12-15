package com.trivadis.springselfwritten;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    @Autowired
    private B b;

    public A() {
        System.out.println("CREATE A"); // 1
    }

    public void doIt() {
        System.out.println("A.doIt");
        b.doIt();
    }

}
