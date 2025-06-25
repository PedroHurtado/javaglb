package com.example.paint.commands;

public class CommandExit implements Command {

    private final Runnable receiver;
    public CommandExit(Runnable receiver) {
        this.receiver = receiver;
    }
    @Override
    public void execute() {
        receiver.run();
    }
    
}
