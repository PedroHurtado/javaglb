package com.example.paint.commands.interfaces;

import com.example.paint.commands.Command;
import com.example.paint.commands.CommandContext;

public interface CommandFactory {
    Command create(CommandContext context);
}
