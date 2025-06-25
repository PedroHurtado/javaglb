package com.example.paint.shapes.interfaces;
import java.util.Scanner;
import java.util.UUID;

import com.example.paint.core.RegisterShape;
import com.example.paint.shapes.Circle;
import com.example.paint.shapes.Shape;

@RegisterShape("circle")
public class CircleCreator implements InteractiveCreatable {

    @Override
    public Shape createFromInput(Scanner scanner) {
        System.out.print("x: ");
        double x = Double.parseDouble(scanner.nextLine());
        System.out.print("y: ");
        double y = Double.parseDouble(scanner.nextLine());
        System.out.print("radio: ");
        double r = Double.parseDouble(scanner.nextLine());
        return new Circle(UUID.randomUUID(), x, y, r);
    }
    
}
