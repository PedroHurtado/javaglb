package com.example.paint.shapes.interfaces;

import java.util.Scanner;
import java.util.UUID;

import com.example.paint.core.RegisterShape;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;


@RegisterShape("rectangle")
public class RectangleCreator implements InteractiveCreatable{

    @Override
    public Shape createFromInput(Scanner scanner) {
        System.out.print("x: ");
        double x = Double.parseDouble(scanner.nextLine());
        System.out.print("y: ");
        double y = Double.parseDouble(scanner.nextLine());
        System.out.print("ancho: ");
        double w = Double.parseDouble(scanner.nextLine());
        System.out.print("alto: ");
        double h = Double.parseDouble(scanner.nextLine());
        return new Rectangle(UUID.randomUUID(),x, y, w, h);
    }
    
}
