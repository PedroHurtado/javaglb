package com.example.lambda;

@FunctionalInterface
public interface Operations<T> {
    T apply(T a,T b);    
} 
