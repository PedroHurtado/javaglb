package com.example.paint.commands;

import com.example.paint.canvas.Canvas;

public class CommandLoad implements Command {
    private final Canvas receiver;
    private final String filename;

    public CommandLoad(Canvas receiver, String filename) {
        this.receiver = receiver;
        this.filename = filename;
    }

    @Override
    public void execute() {
        receiver.loadFromJson(filename);
    }
}
