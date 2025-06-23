package com.example.paint.shapes.interfaces;

import java.util.Scanner;

import com.example.paint.shapes.Shape;

public interface InteractiveCreatable {
    Shape createFromInput(Scanner scanner);
}
