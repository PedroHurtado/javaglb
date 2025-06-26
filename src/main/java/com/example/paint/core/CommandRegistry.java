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

    public static void registerCommands(Reflections reflections) {
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(RegisterCommand.class);

        for (Class<?> clazz : annotatedClasses) {
            if (!isValidCommandFactory(clazz)) continue;

            RegisterCommand annotation = clazz.getAnnotation(RegisterCommand.class);
            Optional<CommandFactory> factory = createFactoryInstance(clazz);

            factory.ifPresent(cmdFactory -> 
                registry.put(annotation.value(), new CommandEntry(cmdFactory, annotation.description()))
            );
        }
    }

    private static boolean isValidCommandFactory(Class<?> clazz) {
        return CommandFactory.class.isAssignableFrom(clazz);
    }

    private static Optional<CommandFactory> createFactoryInstance(Class<?> clazz) {
        try {
            return Optional.of((CommandFactory) clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            System.err.printf("Error instantiating CommandFactory for class %s: %s%n", clazz.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    public static Map<String, CommandEntry> getCommands() {
        return Collections.unmodifiableMap(registry);
    }
}
