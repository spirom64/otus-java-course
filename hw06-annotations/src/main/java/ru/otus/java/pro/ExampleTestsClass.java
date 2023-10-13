package ru.otus.java.pro;

import ru.otus.java.pro.annotations.After;
import ru.otus.java.pro.annotations.Before;
import ru.otus.java.pro.annotations.Test;

public class ExampleTestsClass {
    @Before
    void beforeMethod() {
        System.out.println("Before method");
    }

    @After
    void afterMethod() {
        System.out.println("After method");
    }

    @Test
    void passingTestMethod() {
        System.out.println("Passing test method");
    }

    @Test
    void failingTestMethod() {
        System.out.println("Failing test method");
        throw new AssertionError("Test assertion error");
    }
}
