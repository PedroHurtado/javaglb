package com.example.paint.core;

import java.util.Scanner;

public class InputReaderScanner implements InputReader {
    private final Scanner scanner;

    public InputReaderScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
