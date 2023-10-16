package ru.otus.java.pro;

import ru.otus.java.pro.annotations.After;
import ru.otus.java.pro.annotations.Before;
import ru.otus.java.pro.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnitTestsRunner {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static final Logger log = Logger.getLogger(UnitTestsRunner.class.getName());


    private static final List<Class<? extends Annotation>> SUPPORTED_ANNOTATIONS = List.of(After.class, Before.class, Test.class);

    private static final Map<Class<? extends Annotation>, List<Class<? extends Annotation>>> FORBIDDEN_ANNOTATIONS_MAP = Map.of(
            After.class, List.of(Before.class, Test.class),
            Before.class, List.of(After.class, Test.class),
            Test.class, List.of(After.class, Before.class));

    private static void validateMethodAnnotations(Method method, Class<? extends Annotation> annotation) throws IllegalStateException {
        for (Class<? extends Annotation> forbidden_annotation : FORBIDDEN_ANNOTATIONS_MAP.get(annotation)) {
            if (method.isAnnotationPresent(forbidden_annotation)) {
                throw new IllegalStateException("Annotation \"@" + forbidden_annotation.getSimpleName() +
                        "\" is not allowed for method already annotated with \"@" +
                        annotation.getSimpleName() + "\"");
            }
        }
    }

    private static String getPrettyRootCauseStackTrace(Exception exception) {
        Throwable exceptionToPrint = exception;
        Optional<Throwable> rootCause = Stream.iterate(exception, Throwable::getCause)
                .filter(element -> element.getCause() == null)
                .findFirst();
        if (rootCause.isPresent()) {
            exceptionToPrint = rootCause.get();
        }
        return ANSI_RED + exceptionToPrint + ANSI_RESET + "\nat " +
                Arrays.stream(exceptionToPrint.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\nat "));
    }

    private static Object createTestsClassInstance(Class<?> testsClass) {
        Object testsClassInstance = null;
        try {
            testsClassInstance = testsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.info(getPrettyRootCauseStackTrace(e));
        }
        return testsClassInstance;
    }

    private static boolean runMethodsBatch(Object testsClassInstance, List<Method> methodsBatch) {
        try {
            for (Method method : methodsBatch) {
                method.invoke(testsClassInstance);
            }
        } catch (Exception e) {
            log.info(getPrettyRootCauseStackTrace(e));
            return false;
        }
        return true;
    }

    private static boolean runTestMethod(Object testsClassInstance, Map<Class<? extends  Annotation>, List<Method>> methodsMap, Method method) {
        boolean result = false;
        if (runMethodsBatch(testsClassInstance, methodsMap.get(Before.class))) {
            try {
                method.invoke(testsClassInstance);
                result = true;
            } catch (Exception e) {
                log.info(ANSI_RED + method.getName() + ": failed" + ANSI_RESET);
                log.info(getPrettyRootCauseStackTrace(e));
            }
        }
        runMethodsBatch(testsClassInstance, methodsMap.get(After.class));
        return result;
    }

    private static int runTestMethods(Class<?> testsClass, Map<Class<? extends  Annotation>, List<Method>> methodsMap) {
        int successes = 0;
        for (Method testMethod : methodsMap.get(Test.class)) {
            Object testsClassInstance = createTestsClassInstance(testsClass);
            if (testsClassInstance != null) {
                successes += runTestMethod(testsClassInstance, methodsMap, testMethod)? 1 : 0;
            }
        }
        return successes;
    }

    public static void runTests(String testsClassName) {
        Class<?> testsClass;

        try {
            testsClass = UnitTestsRunner.class.getClassLoader().loadClass(testsClassName);
        } catch (ClassNotFoundException e) {
            log.info(getPrettyRootCauseStackTrace(e));
            return;
        }

        Map<Class<? extends  Annotation>, List<Method>> methodsMap = Map.of(
                After.class, new ArrayList<>(),
                Before.class, new ArrayList<>(),
                Test.class, new ArrayList<>());

        for (Method method : testsClass.getDeclaredMethods()) {
            for (Class<? extends Annotation> annotation : SUPPORTED_ANNOTATIONS) {
                if (method.isAnnotationPresent(annotation)) {
                    try {
                        validateMethodAnnotations(method, annotation);
                    } catch (IllegalStateException e) {
                        log.info(getPrettyRootCauseStackTrace(e));
                        return;
                    }
                    methodsMap.get(annotation).add(method);
                }
            }
        }

        int successes = runTestMethods(testsClass, methodsMap);
        int total = methodsMap.get(Test.class).size();
        if (successes != total) {
            log.info(ANSI_RED + "!!! FAILED !!!" + ANSI_RESET);
        } else {
            log.info(ANSI_GREEN + "PASSED" + ANSI_RESET);
        }
        log.info("Total methods: " + total);
        log.info("Successful runs: " + successes);
        log.info("Failed: " + (total - successes));
    }
}
