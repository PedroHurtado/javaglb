package com.example.paint.core;

import java.util.UUID;

public final class InputValidator {
    private InputValidator() {
    }

    public static double readDouble(InputWriter writer, InputReader reader, String message) {
        while (true) {
            writer.print(message);
            var entry = reader.nextLine();
            try {
                return Double.parseDouble(entry);
            } catch (NullPointerException | NumberFormatException e) {
                writer.println("El valor introducido no es correcto");
            }
        }
    }

    public static UUID readUUID(InputWriter writer, InputReader reader, String message) {
        while (true) {
            writer.print(message);
            var entry = reader.nextLine();
            try {
                return UUID.fromString(entry);
            } catch (IllegalArgumentException e) {
                writer.println("El valor introducido no es correcto");
            }
        }
    }

    public static String readNonEmpty(InputReader scanner, InputWriter writer, String prompt) {
        while (true) {
            writer.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input;
            }
            writer.println("No puede estar vacío.");
        }
    }

}
