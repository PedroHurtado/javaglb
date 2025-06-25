package com.example.paint.commands;

import java.util.UUID;

import com.example.paint.canvas.Canvas;

public class CommandRemoveShape implements Command {
    private final Canvas receiver;
    private UUID id;

    public CommandRemoveShape(Canvas receiver, UUID id) {
        this.receiver = receiver;
        this.id = id;
    }

    @Override
    public void execute() {
        receiver.removeShape(id);
    }

}
