package com.example.paint.shapes;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rectangle extends ShapeAbstract implements Shape {

    private double width, height;

    @JsonCreator
    public Rectangle(
            @JsonProperty("id") UUID id,
            @JsonProperty("x") double x,
            @JsonProperty("y") double y,
            @JsonProperty("width") double width,
            @JsonProperty("height") double height) {
        super(id, x, y);
        this.width = width;
        this.height = height;

    }

    @Override
    public double area() {
        return height * width;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Rectangle [getId()=" + getId() + "area()=" + area() + ", getX()=" + getX() + ", getWidth()="
                + getWidth() + ", getY()="
                + getY() + ", getHeight()=" + getHeight() + "]";
    }

}
