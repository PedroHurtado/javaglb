package com.example.paint.canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.paint.core.InputWriter;
import com.example.paint.shapes.Shape;
import com.example.paint.storage.Storage;

public class CanvasImplementation implements Canvas {

    private final List<Shape> shapes = new ArrayList<>();
    private final Storage storage;
    private final InputWriter writer;

    public CanvasImplementation(Storage storage, InputWriter writer) {
        this.storage = storage;
        this.writer = writer;
    }

    @Override
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public void listShapes() {
        if (shapes.size()>0){
            shapes.forEach(writer::println);
        }
        else{
            writer.println("No existen figuras");
        }
        
    }

    @Override
    public void removeShape(UUID id) {
        findShapeById(id).ifPresent(shapes::remove);
    }

    @Override
    public void moveShape(UUID id, double dx, double dy) {
        findShapeById(id).ifPresent(shape -> shape.move(dx, dy));
    }

    @Override
    public void saveToJson(String filename) {
        storage.save(filename, shapes);
    }

    @Override
    public void loadFromJson(String filename) {
        shapes.clear();
        shapes.addAll(storage.load(filename));
    }

    private Optional<Shape> findShapeById(UUID id) {
        return shapes.stream()
                     .filter(s -> s.getId().equals(id))
                     .findFirst();
    }
}
