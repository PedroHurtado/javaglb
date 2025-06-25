package com.example.paint.storage;

import java.util.List;

import com.example.paint.shapes.Shape;

public interface Storage {
    void save(String filename, List<Shape> shapes);
    List<Shape> load(String filename);    
} 
