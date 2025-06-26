package com.example.paint.app;

import com.example.paint.canvas.Canvas;
import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.core.CommandRegistry;
import com.example.paint.core.ConsoleUtils;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;
import com.example.paint.core.ShapeRegistry;
import com.example.paint.ioc.Ioc;

public class AppPaintImplementation implements AppPaint {

    private Boolean isRunning;
    private CommandContext context = null;

    public AppPaintImplementation(InputReader reader, InputWriter writer, Canvas canvas) {
        this.context = new CommandContext(
                canvas,
                reader,
                writer,
                () -> isRunning = false);

    }

    @Override
    public void run() {
        var writer = context.getOut();
        var reader = context.getScanner();

        initialize();

        while (isRunning) {
            showMenu(writer);
            String choice = promptUserChoice(writer, reader);
            try {                
                ExecutionResult result = executeChoice(choice);
                switch (result) {
                    case INVALID -> pause("Opción inválida.", writer, reader);
                    case VALID -> pause("Pulse una tecla para continuar", writer, reader);
                    case EXIT -> {
                    }
                }
            } catch (Exception e) {
                writer.println(e.getMessage());
                reader.nextLine();
            }

        }
        reader.close();
    }

    private void initialize() {
        var reflections = Ioc.createReflection("com.example");
        isRunning = true;
        ShapeRegistry.registerShapes(context, reflections);
        CommandRegistry.registerCommands(reflections);
    }

    private void showMenu(InputWriter writer) {
        ConsoleUtils.clearConsole();
        writer.println("\n--- MENÚ ---");
        CommandRegistry.getCommands().forEach(
                (key, entry) -> writer.printf("%s: %s%n", key, entry.description));
    }

    private String promptUserChoice(InputWriter writer, InputReader reader) {
        writer.print("Seleccione una opción: ");
        return reader.nextLine();
    }

    private ExecutionResult executeChoice(String choice) {
        var cmdEntry = CommandRegistry.getCommands().get(choice);
        if (cmdEntry == null)
            return ExecutionResult.INVALID;

        Command command = cmdEntry.factory.create(context);
        command.execute();

        return isRunning ? ExecutionResult.VALID : ExecutionResult.EXIT;
    }

    private void pause(String message, InputWriter writer, InputReader reader) {
        writer.print(message);
        reader.nextLine();
    }

}
