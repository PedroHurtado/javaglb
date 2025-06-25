package com.example.paint.commands.interfaces;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;
import com.example.paint.commands.CommandExit;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "exit", description = "salir de la applicacion")
public class CommandFactoryExit implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        return new CommandExit(context.getOnExit());
    }
    
}
