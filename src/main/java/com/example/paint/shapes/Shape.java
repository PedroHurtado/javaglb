package com.example.paint.shapes;

import java.util.UUID;

public interface Shape {
    UUID getId();
    double area();
    void move(double dx, double dy);
    String toString(); 
}
