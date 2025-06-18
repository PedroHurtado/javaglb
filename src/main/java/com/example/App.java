package com.example;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.example.solid.Pinguino;
import com.example.solid.PrintAves;
import com.example.uniqueclass.Unique;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {        
        UUID id = UUID.randomUUID();
        var unique = new Unique(id);
        var unique1 = new Unique(UUID.randomUUID());

        Set<Unique> set = new HashSet<>();

        set.add(unique);
        set.add(unique1);

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
