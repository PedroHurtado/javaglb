package com.example.paint.commands;

import java.util.UUID;

import com.example.paint.canvas.Canvas;

public class CommandMoveShape implements Command {
    private final Canvas receiver;
    private final UUID id;
    private final Double dx;
    private final Double dy;

    public CommandMoveShape(Canvas receiver, UUID id, Double dx, Double dy) {
        this.receiver = receiver;
        this.id = id;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        receiver.moveShape(id, dx, dy);
    }
}
