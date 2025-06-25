package com.example.paint.commands;

import com.example.paint.canvas.Canvas;

public class CommandListShape implements Command{
    private final Canvas receiver;

    public CommandListShape(Canvas receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        receiver.listShapes();
    }
}
