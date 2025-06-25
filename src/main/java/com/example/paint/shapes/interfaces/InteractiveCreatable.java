package com.example.paint.shapes.interfaces;


import com.example.paint.commands.CommandContext;
import com.example.paint.shapes.Shape;

public interface InteractiveCreatable {
    Shape createFromInput(CommandContext context);
}
