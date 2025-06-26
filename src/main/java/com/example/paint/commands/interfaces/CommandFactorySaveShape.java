package com.example.paint.commands.interfaces;



import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandSave;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "save", description = "Guardar las figuras en un archivo JSON")
public class CommandFactorySaveShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {        
        InputReader scanner = context.getScanner();
        InputWriter writer = context.getOut();
        writer.print("Introduce el nombre del archivo para guardar: ");
        String filename = scanner.nextLine();

        return new CommandSave(context.getCanvas(), filename);
    }
}
