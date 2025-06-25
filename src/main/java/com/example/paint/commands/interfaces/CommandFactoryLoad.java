package com.example.paint.commands.interfaces;



import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandLoad;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "load", description = "Cargar figuras desde un archivo JSON")
public class CommandFactoryLoad implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        InputReader scanner = context.getScanner();
        InputWriter writer =context.getOut();
        writer.print("Introduce el nombre del archivo para cargar: ");
        String filename = scanner.nextLine();

        return new CommandLoad(context.getCanvas(), filename);
    }
}
