package com.example.paint.canvas;

import com.example.paint.shapes.Shape;

//solid i(interface segregation)
public interface CanvasAddShape {
    void addShape(Shape shape);
}
