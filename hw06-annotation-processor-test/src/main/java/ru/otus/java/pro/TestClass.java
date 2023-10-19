package ru.otus.java.pro;

import ru.otus.java.annotations.CustomToStringAnnotation;


@CustomToStringAnnotation
public class TestClass {
    private String testPrivateField;
    protected String testProtectedField = "protected";
    public String testPublicField = "public";
}
