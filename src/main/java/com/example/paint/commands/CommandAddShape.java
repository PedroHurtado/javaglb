package com.example.paint.commands;

import com.example.paint.canvas.CanvasAddShape;
import com.example.paint.shapes.Shape;

public class CommandAddShape implements Command {

    private final CanvasAddShape reciever;
    private final Shape shape;
    public CommandAddShape(CanvasAddShape reciever, Shape shape) {
        this.reciever = reciever;
        this.shape = shape;
    }
    @Override
    public void execute() {        
        reciever.addShape(shape);
    }
    
}
