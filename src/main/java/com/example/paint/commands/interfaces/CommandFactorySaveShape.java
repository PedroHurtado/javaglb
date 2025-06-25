package com.example.paint.commands.interfaces;

import java.util.Scanner;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandSave;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "save", description = "Guardar las figuras en un archivo JSON")
public class CommandFactorySaveShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        Scanner scanner = context.getScanner();
        System.out.print("Introduce el nombre del archivo para guardar: ");
        String filename = scanner.nextLine();

        return new CommandSave(context.getCanvas(), filename);
    }
}
