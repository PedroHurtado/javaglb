package com.example.solid.segregation;

public interface Remove<T,ID> extends Get<T,ID> {
    public void remove(T entity);
}
