package com.example.paint.commands.interfaces;


import java.util.UUID;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandMoveShape;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "move", description = "Mover una figura")
public class CommandFactoryMoveShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        InputReader scanner = context.getScanner();
        InputWriter writer =context.getOut();
        writer.print("Introduce el ID de la figura: ");
        UUID id = UUID.fromString(scanner.nextLine());

        writer.print("Introduce desplazamiento en X: ");
        double dx = Double.parseDouble(scanner.nextLine());

        writer.print("Introduce desplazamiento en Y: ");
        double dy = Double.parseDouble(scanner.nextLine());

        return new CommandMoveShape(context.getCanvas(), id, dx, dy);
    }
}
