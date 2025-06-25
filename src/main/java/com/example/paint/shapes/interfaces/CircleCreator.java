package com.example.paint.shapes.interfaces;

import java.util.UUID;

import com.example.paint.commands.CommandContext;
import com.example.paint.core.RegisterShape;
import com.example.paint.shapes.Circle;
import com.example.paint.shapes.Shape;

@RegisterShape("circle")
public class CircleCreator implements InteractiveCreatable {

    @Override
    public Shape createFromInput(CommandContext context) {
        var writer = context.getOut();
        var input = context.getScanner();
        writer.print("x: ");
        double x = Double.parseDouble(input.nextLine());
        writer.print("y: ");
        double y = Double.parseDouble(input.nextLine());
        writer.print("radio: ");
        double r = Double.parseDouble(input.nextLine());
        return new Circle(UUID.randomUUID(), x, y, r);
    }
    
}
