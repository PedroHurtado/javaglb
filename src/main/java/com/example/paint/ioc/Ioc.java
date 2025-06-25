package com.example.paint.ioc;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.example.paint.canvas.Canvas;
import com.example.paint.canvas.CanvasImplementation;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputReaderScanner;
import com.example.paint.core.InputWriter;
import com.example.paint.core.InputWriterImplementation;
import com.example.paint.storage.JsonStorage;
import com.example.paint.storage.Storage;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Ioc {
    public static Storage createStorage(ObjectMapper mapper){
        return new JsonStorage(mapper);
    }
    public static InputWriter createInputWriter(PrintStream out){
        return new InputWriterImplementation(out);
    }
    public static Canvas createCanvas(Storage storage, InputWriter writer){
        return new CanvasImplementation(storage, writer);
    }
    public static InputReader createInputReader(Scanner scanner){        
        return new InputReaderScanner(scanner);
    }
    public static Scanner creatScanner(InputStream in ){
        return new Scanner(in);
    } 
}
