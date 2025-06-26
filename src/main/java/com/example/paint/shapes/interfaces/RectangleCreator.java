package com.example.paint.shapes.interfaces;


import java.util.UUID;

import com.example.paint.commands.CommandContext;
import com.example.paint.core.InputValidator;
import com.example.paint.core.RegisterShape;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;


@RegisterShape("rectangle")
public class RectangleCreator implements InteractiveCreatable{

    @Override
    public Shape createFromInput(CommandContext context) {
        var reader = context.getScanner();
        var writer = context.getOut();

        double x = InputValidator.readDouble(writer, reader, "x: ");
        double y = InputValidator.readDouble(writer, reader, "y: ");
        double w = InputValidator.readDouble(writer, reader, "ancho: ");
        double h = InputValidator.readDouble(writer, reader, "alto: ");

        return new Rectangle(UUID.randomUUID(),x, y, w, h);
    }
    
}
