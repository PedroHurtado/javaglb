package com.example.paint.canvas;

import java.util.UUID;

import com.example.paint.shapes.Shape;

public interface Canvas {
    void addShape(Shape shape);
    void listShapes();
    void removeShape(UUID id);
    void moveShape(UUID id, double dx, double dy);
    void saveToJson(String filename);
    void loadFromJson(String filename);
}
