package com.example.paint.commands;

import java.util.Scanner;

import com.example.paint.canvas.Canvas;

public class CommandContext {
    private final Canvas canvas;
    private final Scanner scanner;
    private final Runnable onExit;
    public CommandContext(Canvas canvas, Scanner scanner, Runnable onExit) {
        this.canvas = canvas;
        this.scanner = scanner;
        this.onExit = onExit;
    }
    public Canvas getCanvas() {
        return canvas;
    }
    public Scanner getScanner() {
        return scanner;
    }
    public Runnable getOnExit() {
        return onExit;
    }
}
