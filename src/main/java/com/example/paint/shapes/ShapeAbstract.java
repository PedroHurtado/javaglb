package com.example.paint.shapes;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class ShapeAbstract {
    
    private final UUID id;
    private double x,y;

    protected ShapeAbstract(UUID id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public void move(double dx,double dy){
        x+=dx;
        y+=dy;
    }    
    
}
