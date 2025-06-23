package com.example.paint.shapes;

import java.util.UUID;

public abstract class ShapeAbstract {
    public final UUID id;
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
    public UUID getId() {
        return id;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }   
    
}
