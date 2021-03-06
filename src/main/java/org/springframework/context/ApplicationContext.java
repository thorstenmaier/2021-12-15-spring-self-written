package org.springframework.context;

import com.trivadis.springselfwritten.B;
import com.trivadis.springselfwritten.PerformanceAspect;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class ApplicationContext {

    private Map<String, Object> allBeans = new HashMap<>();

    public <T> T getBean(Class<T> clazz) {
        return (T) allBeans.get(clazz.getTypeName());
    }

    public ApplicationContext(Class config) {
        try {
            createAllBeans(config);
            dependencyInjection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAllBeans(Class config) throws Exception {
        for (Class aClass : getClasses(config.getPackageName())) {
            if (aClass.isAnnotationPresent(Component.class)) {
                Object newBean = aClass.getDeclaredConstructor().newInstance();
                allBeans.put(aClass.getTypeName(), newBean);
            }
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private void dependencyInjection() throws IllegalAccessException {
        for (Object bean : allBeans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String type = field.getType().getTypeName();
                    Object dependentBean = allBeans.get(type);
                    if (type.equals("com.trivadis.springselfwritten.B")) {
                        Enhancer enhancer = new Enhancer();
                        enhancer.setSuperclass(B.class);
                        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
                            new PerformanceAspect().performance(new ProceedingJoinPoint() {
                                @Override
                                public String call() {
                                    try {
                                        methodProxy.invokeSuper(o, objects);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            });
                            return null;
                        });
                        dependentBean = enhancer.create();
                    }
                    field.setAccessible(true);
                    field.set(bean, dependentBean);
                }
            }
        }
    }
}
