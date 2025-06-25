package com.example.paint.storage;

import java.util.List;

import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStorage implements Storage{
    private final ObjectMapper mapper;
    
    public JsonStorage(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(String filename, List<Shape> shapes) {
        
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public List<Shape> load(String filename) {
        
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }
    
}
