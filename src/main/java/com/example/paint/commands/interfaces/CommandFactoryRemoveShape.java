package com.example.paint.commands.interfaces;

import java.util.Scanner;
import java.util.UUID;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandRemoveShape;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "remove", description = "Eliminar una figura por ID")
public class CommandFactoryRemoveShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        Scanner scanner = context.getScanner();
        System.out.print("Introduce el ID de la figura a eliminar: ");
        UUID id = UUID.fromString(scanner.nextLine());

        return new CommandRemoveShape(context.getCanvas(), id);
    }
}
