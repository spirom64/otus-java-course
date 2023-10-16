package ru.otus.java.pro;

import ru.otus.java.pro.annotations.After;
import ru.otus.java.pro.annotations.Before;
import ru.otus.java.pro.annotations.Test;

import java.util.logging.Logger;

public class ExampleTestsClass {
    private static final Logger log = Logger.getLogger(ExampleTestsClass.class.getName());
    @Before
    void beforeMethod() {
        log.info("Before method");
    }

    @After
    void afterMethod() {
        log.info("After method");
    }

    @Test
    void passingTestMethod() {
        log.info("Passing test method");
    }

    @Test
    void failingTestMethod() {
        log.info("Failing test method");
        throw new AssertionError("Test assertion error");
    }
}
