package com.example.paint.shapes;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Circle extends ShapeAbstract implements Shape {
    public double radio;

    @JsonCreator
    public Circle(
            @JsonProperty("id") UUID id,
            @JsonProperty("x") double x,
            @JsonProperty("y") double y,
            @JsonProperty("radio") double radio) {
        super(id, x, y);
        this.radio = radio;
    }

    public double getRadio() {
        return radio;
    }

    @Override
    public double area() {
        return Math.PI * radio * radio;
    }

    @Override
    public String toString() {
        return "Circle [getId()=" + getId() + " getRadio()=" + getRadio() + ", getX()=" + getX() + ", getY()=" + getY()
                + "]";
    }
}
