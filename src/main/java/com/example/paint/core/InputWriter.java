package com.example.paint.core;



public interface InputWriter {
    void print(Object message);
    void println(Object message);
    void printf(String format, Object ... args);
}
