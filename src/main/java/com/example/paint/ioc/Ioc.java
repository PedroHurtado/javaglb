package com.example.paint.ioc;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.example.paint.app.AppPaint;
import com.example.paint.app.AppPaintImplementation;
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
    private Ioc(){}
    private static Storage createStorage(ObjectMapper mapper){
        return new JsonStorage(mapper);
    }
    private static InputWriter createInputWriter(PrintStream out){
        return new InputWriterImplementation(out);
    }
    private static Canvas createCanvas(Storage storage, InputWriter writer){
        return new CanvasImplementation(storage, writer);
    }
    private static InputReader createInputReader(Scanner scanner){        
        return new InputReaderScanner(scanner);
    }
    private static Scanner creatScanner(InputStream in ){
        return new Scanner(in);
    } 
    public static AppPaint createApp(
        ObjectMapper mapper,
        InputStream in,
        PrintStream out
    ){
        var writer = createInputWriter(out);
        var reader = createInputReader(creatScanner(in));
        var canvas = createCanvas(createStorage(mapper), writer);
        return new AppPaintImplementation(reader,writer,canvas);
    }
}
