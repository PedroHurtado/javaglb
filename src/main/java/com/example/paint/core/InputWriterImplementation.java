package com.example.paint.core;

import java.io.PrintStream;

public class InputWriterImplementation implements InputWriter {
    private final PrintStream out;

    public InputWriterImplementation(PrintStream out) {
        this.out = out;
    }

    @Override
    public void print(Object message) {
        out.print(message);
    }

    @Override
    public void println(Object message) {
        out.println(message);
    }

    @Override
    public void printf(String format, Object... args) {
        out.printf(format, args);
    }
}
