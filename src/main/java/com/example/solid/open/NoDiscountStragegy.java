package com.example.solid.open;

public class NoDiscountStragegy  implements DiscountStrategy {

    @Override
    public double ApplyDiscount(double total) {
       return total;
    }
    
}
