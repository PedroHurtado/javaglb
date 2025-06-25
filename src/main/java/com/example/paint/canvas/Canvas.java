package com.example.paint.canvas;

import java.util.UUID;


public interface Canvas extends CanvasAddShape {
    
    void listShapes();
    void removeShape(UUID id);
    void moveShape(UUID id, double dx, double dy);
    void saveToJson(String filename);
    void loadFromJson(String filename);
}
