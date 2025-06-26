package com.example.paint.shapes;

import java.util.UUID;

import lombok.Getter;



@Getter
public class Circle extends ShapeAbstract implements Shape {
    public double radio;

    
    public Circle(
            UUID id,
            double x,
            double y,
            double radio) {
        super(id, x, y);
        this.radio = radio;
    }

    

    @Override
    public double area() {
        return Math.PI * radio * radio;
    }

    @Override
    public String toString() {
        return "Circle [id=" + getId() + ", radio=" + getRadio() + ", x=" + getX() + ", y=" + getY()
                + "]";
    }
}
