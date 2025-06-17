package com.example.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public class OperationsImpl {
    public static void Operations(){
        List<BiFunction<Integer, Integer, Integer>> operations = new ArrayList<>();
        //List<Operations<Integer>> operations = new ArrayList<>();
        operations.add(OperationsImpl::sum);
        operations.add((a,b)->a*b);
        operations.add((a,b)->a/b);
        operations.add((a,b)->a-b);

        for (var operation : operations) {
            System.out.println(operation.apply(2, 2));
        }
    }
    public static Integer sum(Integer a, Integer b){
        return a+b;
    }

}
