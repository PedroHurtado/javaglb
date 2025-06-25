package com.example.paint.shapes.interfaces;


import java.util.UUID;

import com.example.paint.commands.CommandContext;
import com.example.paint.core.RegisterShape;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;


@RegisterShape("rectangle")
public class RectangleCreator implements InteractiveCreatable{

    @Override
    public Shape createFromInput(CommandContext context) {
        var reader = context.getScanner();
        var writer = context.getOut();
        writer.print("x: ");
        double x = Double.parseDouble(reader.nextLine());
        writer.print("y: ");
        double y = Double.parseDouble(reader.nextLine());
        writer.print("ancho: ");
        double w = Double.parseDouble(reader.nextLine());
        writer.print("alto: ");
        double h = Double.parseDouble(reader.nextLine());
        return new Rectangle(UUID.randomUUID(),x, y, w, h);
    }
    
}
