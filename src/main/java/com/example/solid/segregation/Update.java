package com.example.solid.segregation;

public interface Update<T,ID> extends Get<T,ID> {
    void update(T id);
}
