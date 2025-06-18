package com.example.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public class OperationsImpl {
    public static void Operations(){
        Function<Integer,Function<Integer, Integer>> clousure = a -> b->a+b;
        Function<Integer,Function<Integer, Integer>> clousure1 = OperationsImpl::clousureReference;

        List<BiFunction<Integer, Integer, Integer>> operations = new ArrayList<>();
        //List<Operations<Integer>> operations = new ArrayList<>();
        operations.add(OperationsImpl::sum);
        operations.add((a,b)->a*b);
        operations.add((a,b)->a/b);
        operations.add((a,b)->a-b);

        for (var operation : operations) {
            System.out.println(operation.apply(2, 2));
        }

        var result = clousure.apply(8);
        System.out.println(result.apply(3)); //11
        System.out.println(result.apply(100)); //108

        var result1 = clousure1.apply(8);
        System.out.println(result1.apply(3)); //11
        System.out.println(result1.apply(100)); //108

        

    }
    public static Integer sum(Integer a, Integer b){
        return a+b;
    }
    public static Function<Integer,Integer> clousureReference(final Integer a){
        return (b)->a+b;
    }

}
