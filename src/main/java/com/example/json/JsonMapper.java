package com.example.json;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Set;

import org.reflections.Reflections;

import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.databind.jsontype.NamedType;

public class JsonMapper {
    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.addMixIn(Shape.class, ShapeMixIn.class);
        
        registerSubtypesAutomatically(mapper, Shape.class, "com.example"); 

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }

    private static <T> void registerSubtypesAutomatically(ObjectMapper mapper, Class<T> baseClass, String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subtypes = reflections.getSubTypesOf(baseClass);

        for (Class<? extends T> subtype : subtypes) {
            String typeName = getTypeName(subtype);
            mapper.registerSubtypes(new NamedType(subtype, typeName));
        }
    }

    private static String getTypeName(Class<?> clazz) {
        
        JsonTypeName annotation = clazz.getAnnotation(JsonTypeName.class);
        if (annotation != null) {
            return annotation.value();
        }
        
        return clazz.getSimpleName().toLowerCase();
    }
}
