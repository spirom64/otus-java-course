package ru.otus.java.pro;

import ru.otus.java.pro.TestClassWithCustomToString;

import java.util.logging.Logger;

public class MainApp {
    private static final Logger log = Logger.getLogger(MainApp.class.getName());
    public static void main(String[] args) {
        TestClassWithCustomToString test = new TestClassWithCustomToString();
        log.info(test.toString());
    }
}
