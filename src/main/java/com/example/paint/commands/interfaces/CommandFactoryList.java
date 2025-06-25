package com.example.paint.commands.interfaces;


import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;

import com.example.paint.commands.CommandListShape;
import com.example.paint.core.RegisterCommand;

@RegisterCommand(value = "list", description = "Listar figuras")
public class CommandFactoryList implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        return new CommandListShape(context.getCanvas());
    }
}

