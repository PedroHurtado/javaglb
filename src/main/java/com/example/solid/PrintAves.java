package com.example.solid;

import java.util.function.Consumer;

public class PrintAves {
    public static void PrintAve(Ave ave, Consumer<Object> printer){  
        printer.accept("Ave");      
        printer.accept(ave);
    }
    public static void PrintAveVoladora(AveVoladora ave,Consumer<Object> printer){
        System.out.println("Voladoras");
        PrintAve(ave,printer);
    }
    public static void PrintAveNoVolaora(AveNoVoladora ave,Consumer<Object> printer){
        System.out.println("NoVoladoras");
        PrintAve(ave,printer);
    }
}
