package com.example.paint.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.paint.shapes.Shape;

public class CamvasImplementation implements Canvas {
    private final List<Shape> shapes = new ArrayList<>();
    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void listShapes() {
        for (Shape shape : shapes) {
            System.out.println(shape);
        }
    }

    @Override
    public void removeShape(UUID id) {
        var shape = shapes.stream().filter(s->s.getId().equals(id))
            .findFirst();
        if(shape.isPresent()){
            shapes.remove(shape.get());
        }
    }

    @Override
    public void moveShape(UUID id, double dx, double dy) {
        var shape = shapes.stream().filter(s->s.getId().equals(id))
            .findFirst();
        if(shape.isPresent()){
           shape.get().move(dx, dy);
        }
    }

    @Override
    public void saveToJson(String filename) {       
        throw new UnsupportedOperationException("Unimplemented method 'saveToJson'");
    }
    @Override
    public void loadFromJson(String filename) {
       
        throw new UnsupportedOperationException("Unimplemented method 'loadFromJson'");
    }
    
}
