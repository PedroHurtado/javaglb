package com.example.paint.commands.interfaces;


import java.util.UUID;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandMoveShape;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputValidator;
import com.example.paint.core.InputWriter;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "move", description = "Mover una figura")
public class CommandFactoryMoveShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        InputReader scanner = context.getScanner();
        InputWriter writer =context.getOut();

        UUID id = InputValidator.readUUID(writer, scanner,"Introduce el ID de la figura: ");
        double dx = InputValidator.readDouble(writer, scanner,"Introduce desplazamiento en X:" );
        double dy = InputValidator.readDouble(writer, scanner,"Introduce desplazamiento en Y:" );              

        return new CommandMoveShape(context.getCanvas(), id, dx, dy);
    }
}
