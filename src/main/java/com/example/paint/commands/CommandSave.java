package com.example.paint.commands;

import com.example.paint.canvas.Canvas;

public class CommandSave implements Command {
    private final Canvas receiver;
    private final String filename;

    public CommandSave(Canvas receiver, String filename) {
        this.receiver = receiver;
        this.filename = filename;
    }

    @Override
    public void execute() {
        receiver.saveToJson(filename);
    }

}
