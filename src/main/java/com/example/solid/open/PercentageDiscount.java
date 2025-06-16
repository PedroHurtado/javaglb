package com.example.solid.open;

public class PercentageDiscount implements DiscountStrategy {

    private final double percentage;
    public PercentageDiscount(double percentage){
        this.percentage = percentage;
    }
    @Override
    public double ApplyDiscount(double total) {
        return total * (1-percentage);
    }
    
}
