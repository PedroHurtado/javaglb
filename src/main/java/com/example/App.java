package com.example;

import java.time.LocalDateTime;

import java.util.function.Consumer;

import com.example.solid.Pinguino;
import com.example.solid.PrintAves;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {        
        
        var pinguino = new Pinguino(5);        
        PrintAves.PrintAveNoVolaora(pinguino, System.out::println);
        //PrintAves.PrintAveVoladora(pinguino);
        System.out.println( "Hello World!" );
        printDate(LocalDateTime.now(), System.out::println);
        printDate(LocalDateTime.now(), App::method);
    }
    private static void printDate(LocalDateTime date,Consumer<LocalDateTime> printer){
        printer.accept(date);
    }
    private static void method(LocalDateTime date){
        System.out.println("He entrado al metodo");
    }
}
