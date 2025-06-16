package com.example.solid.open;

public class CheckoutService {
    private final DiscountStrategy discountStrategy;

    public CheckoutService(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double finalPrice(double total){
        return discountStrategy.ApplyDiscount(total);
    }
}
