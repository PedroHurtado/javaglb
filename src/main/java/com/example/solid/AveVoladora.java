package com.example.solid;

public abstract class AveVoladora extends Ave {
    private double velocidad;

    public AveVoladora(double peso, double velocidad) {
        super(peso);
        this.velocidad = velocidad;
    }

    public double getVelocidad() {
        return velocidad;
    }
}

