package com.example.solid.open;

public class FidexStrategy implements DiscountStrategy {

    private final double fixed;
    public FidexStrategy(double fixed){
        this.fixed = fixed;
    }
    @Override
    public double ApplyDiscount(double total) {
        return total -fixed;
    }
    
}
