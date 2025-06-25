package com.example.paint.core;

import org.reflections.Reflections;

import com.example.paint.commands.interfaces.CommandFactory;

import java.util.*;

public class CommandRegistry {
    private static final Map<String, CommandEntry> registry = new LinkedHashMap<>();

    public static class CommandEntry {
        public final CommandFactory factory;
        public final String description;

        public CommandEntry(CommandFactory factory, String description) {
            this.factory = factory;
            this.description = description;
        }
    }

    public static void registerCommands() {
        Reflections reflections = new Reflections("com.example"); 
        
        Set<Class<?>> commandFactories = reflections.getTypesAnnotatedWith(RegisterCommand.class);

        for (Class<?> clazz : commandFactories) {
            RegisterCommand annotation = clazz.getAnnotation(RegisterCommand.class);
            if (!CommandFactory.class.isAssignableFrom(clazz)) continue;

            try {
                CommandFactory factory = (CommandFactory) clazz.getDeclaredConstructor().newInstance();
                registry.put(annotation.value(), new CommandEntry(factory, annotation.description()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, CommandEntry> getCommands() {
        return registry;
    }
}
