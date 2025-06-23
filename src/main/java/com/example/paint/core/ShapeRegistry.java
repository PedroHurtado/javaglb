package com.example.paint.core;

import org.reflections.Reflections;

import com.example.paint.shapes.Shape;
import com.example.paint.shapes.interfaces.InteractiveCreatable;

import java.util.*;
import java.util.function.Supplier;

public class ShapeRegistry {
    private static final Map<String, Supplier<Shape>> registry = new HashMap<>();

    public static void registerShapes(Scanner scanner) {
        Reflections reflections = new Reflections("com.example"); 

        Set<Class<?>> creators = reflections.getTypesAnnotatedWith(RegisterShape.class);

        for (Class<?> clazz : creators) {
            RegisterShape annotation = clazz.getAnnotation(RegisterShape.class);
            if (!InteractiveCreatable.class.isAssignableFrom(clazz)) {
                System.err.println(clazz.getName() + " no implementa InteractiveCreatable");
                continue;
            }

            try {
                InteractiveCreatable instance = (InteractiveCreatable) clazz.getDeclaredConstructor().newInstance();
                registry.put(annotation.value().toLowerCase(), () -> instance.createFromInput(scanner));
            } catch (Exception e) {
                System.err.println("Error al instanciar " + clazz.getName());
                e.printStackTrace();
            }
        }
    }

    public static Supplier<Shape> getFactory(String name) {
        return registry.get(name.toLowerCase());
    }

    public static Set<String> getAvailableShapes() {
        return registry.keySet();
    }
}

