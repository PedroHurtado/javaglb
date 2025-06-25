package com.example.paint.commands;


import com.example.paint.canvas.Canvas;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;

public class CommandContext {
    private final Canvas canvas;
    private final InputReader scanner;
    private final Runnable onExit;
    private final InputWriter out;
    public CommandContext(Canvas canvas, InputReader scanner, Runnable onExit, InputWriter out) {
        this.canvas = canvas;
        this.scanner = scanner;
        this.onExit = onExit;
        this.out = out;
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    public InputReader getScanner() {
        return scanner;
    }
    public Runnable getOnExit() {
        return onExit;
    }
    public InputWriter getOut(){
        return out;
    }
}
