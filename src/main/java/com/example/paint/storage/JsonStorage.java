package com.example.paint.storage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonStorage implements Storage {
    private final ObjectMapper mapper;

    public JsonStorage(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(String filename, List<Shape> shapes) {

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), shapes.toArray(new Shape[0]));
        } catch (IOException e) {
            throw new RuntimeException("Error saving shapes to file", e);
        }
    }

    @Override
    public List<Shape> load(String filename) {

        try {
            return mapper.readValue(new File(filename), new TypeReference<List<Shape>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Error loading shapes from file", e);
        }
    }

}
