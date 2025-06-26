package com.example.paint.core;

import java.util.UUID;

public final class InputValidator {
    private InputValidator() {
    }

    public static double inputDouble(InputWriter writer, InputReader reader, String message) {
        while (true) {
            try {
                writer.print(message);
                var entry = reader.nextLine();
                return Double.parseDouble(entry);
            } catch (NullPointerException | NumberFormatException e) {
                writer.println("El valor introducido no es correcto");
            }
        }
    }

    public static UUID inputUUID(InputWriter writer, InputReader reader, String message) {
        while (true) {
            try {
                writer.print(message);
                var entry = reader.nextLine();
                return UUID.fromString(entry);
            } catch (IllegalArgumentException e) {
                writer.println("El valor introducido no es correcto");
            }
        }
    }
}
