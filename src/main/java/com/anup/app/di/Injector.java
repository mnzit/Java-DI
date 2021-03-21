package com.anup.app.di;

import com.anup.app.annotation.Component;
import com.anup.app.di.util.InjectionUtil;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.CacheableSearchConfig;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;

import javax.management.RuntimeErrorException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Manjit Shakya
 * @email manjit.shakya@f1soft.com
 */
public class Injector {

    private static Injector injector;

    private Map<Class<?>, Class<?>> diMap;
    private Map<Class<?>, Object> applicationScope;

    private Injector() {
        super();
        diMap = new HashMap<>();
        applicationScope = new HashMap<>();
    }

    /**
     * Start application
     *
     * @param mainClass
     */
    public static void startApplication(Class<?> mainClass) {
        try {
            synchronized (Injector.class) {
                if (injector == null) {
                    injector = new Injector();
                    injector.initFramework(mainClass);
                }
            }
        } catch (Exception ex) {
            System.out.println("Stacktrace: " + ex.getStackTrace());
        }
    }

    public static <T> T getService(Class<T> classz) {
        try {
            return injector.getBeanInstance(classz);
        } catch (Exception ex) {
            System.out.println("Stacktrace: " + ex.getStackTrace());
        }
        return null;
    }

    /**
     * Injector, to create objects
     *
     * @param mainClass
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void initFramework(Class<?> mainClass)
            throws
            IllegalAccessException,
            InstantiationException {
        Class<?>[] classes = getClasses(mainClass.getPackage().getName(), true);
        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        String packageRelPath = mainClass.getPackage().getName().replace(".", "/");

        try (
                ClassHunter.SearchResult result = classHunter.findBy(
                        SearchConfig.forResources(packageRelPath)
                                .by(
                                        ClassCriteria.create()
                                                .allThoseThatMatch(
                                                        cls -> cls.getAnnotation(Component.class) != null)
                                )
                )
        ) {
            Collection<Class<?>> types = result.getClasses();
            for (Class<?> implementationClass : types) {
                Class<?>[] interfaces = implementationClass.getInterfaces();
                if (interfaces.length == 0) {
                    diMap.put(implementationClass, implementationClass);
                } else {
                    for (Class<?> iface : interfaces) {
                        diMap.put(implementationClass, iface);
                    }
                }
            }

            for (Class<?> classz : classes) {
                if (classz.isAnnotationPresent(Component.class)) {
                    Object classInstance = classz.newInstance();
                    applicationScope.put(classz, classInstance);
                    InjectionUtil.autowire(this, classz, classInstance);
                }
            }

        }

    }

    /**
     * Get all classes for the input package
     *
     * @param packageName
     * @param recursive
     * @return
     */
    public Class<?>[] getClasses(String packageName, boolean recursive) {

        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        String packageRelPath = packageName.replace(".", "/");
        CacheableSearchConfig config = SearchConfig.forResources(packageRelPath);

        if (!recursive) {
            config.notRecursiveOnPath(packageRelPath, recursive);
        }

        try (ClassHunter.SearchResult result = classHunter.findBy(config)) {
            Collection<Class<?>> classes = result.getClasses();
            return classes.toArray(new Class[classes.size()]);
        }
    }

    /**
     * Overload getBeanInstance to and qualifer and autowire by type
     *
     * @param interfaceClass
     * @param fieldName
     * @param qualifier
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public <T> Object getBeanInstance(Class<T> interfaceClass, String fieldName, String qualifier)
            throws
            IllegalAccessException,
            InstantiationException {
        Class<?> implementationClass = getImplementationClass(interfaceClass, fieldName, qualifier);

        if (applicationScope.containsKey(implementationClass)) {
            return applicationScope.get(implementationClass);
        }

        synchronized (applicationScope) {
            Object service = implementationClass.newInstance();
            applicationScope.put(implementationClass, service);
            return service;
        }
    }

    /**
     * Create and Get the object instance of the implementation class for input service
     *
     * @param interfaceClass
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T> T getBeanInstance(Class<T> interfaceClass)
            throws
            InstantiationException,
            IllegalAccessException {
        return (T) getBeanInstance(interfaceClass, null, null);
    }

    /**
     * Get the name of the implementation class for input interface service
     *
     * @param interfaceClass
     * @param fieldName
     * @param qualifier
     * @return
     */
    public Class<?> getImplementationClass(Class<?> interfaceClass, String fieldName, String qualifier) {
        Set<Map.Entry<Class<?>, Class<?>>> implementationClasses = diMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == interfaceClass)
                .collect(Collectors.toSet());
        String errorMsg = "";
        if (implementationClasses == null || implementationClasses.size() == 0) {
            errorMsg = "No implementation found for interface " + interfaceClass.getName();
        } else if (implementationClasses.size() == 1) {
            Optional<Map.Entry<Class<?>, Class<?>>> optional = implementationClasses.stream().findFirst();
            if (optional.isPresent()) {
                return optional.get().getKey();
            } else {
                errorMsg = "There are " + implementationClasses.size() + " of interface " + interfaceClass.getName() + " Excepted single implementation or make use of @Qualifier to resolve conflicts";
            }
        }
        throw new RuntimeErrorException(new Error(errorMsg));
    }
}
