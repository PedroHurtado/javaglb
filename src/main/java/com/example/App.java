package com.example;

//import java.time.LocalDateTime;
//import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
//import java.util.function.Consumer;
//import java.util.Set;
//import java.util.UUID;
//import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.example.json.JsonMapper;
import com.example.paint.core.ShapeRegistry;
import com.example.paint.shapes.Circle;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.core.type.TypeReference;
//import com.example.paint.shapes.ShapeAbstract;
//import com.example.solid.Pinguino;
//import com.example.solid.PrintAves;
//import com.example.uniqueclass.Unique;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Hello world!
 *
 */
public class App {
        public static void main(String[] args) {

                var rectangle = new Rectangle(UUID.randomUUID(), 0, 0, 10, 20);
                List<Shape> shapes = List.of(
                                new Circle(UUID.randomUUID(), 10, 10, 5),
                                new Rectangle(UUID.randomUUID(), 0, 0, 10, 20));

                ObjectMapper mapper = JsonMapper.create();               
                try{
                        //String json = mapper.writeValueAsString(shapes.toArray(new Shape[0]));
                        String json = mapper.writeValueAsString(rectangle);
                        System.out.println(json);
                        
                        var result = mapper.readValue(json, new TypeReference<Rectangle>() {});
                        System.out.println("fin");
                }
                catch(Exception e){
                        System.out.println(e);
                }
                

                // Consumer<Integer> onExit = (status)->System.exit(status);

                Scanner scanner = new Scanner(System.in);
                ShapeRegistry.registerShapes(scanner);

                System.out.println("Figuras disponibles: " + ShapeRegistry.getAvailableShapes());
                System.out.print("Selecciona una figura: ");
                String type = scanner.nextLine().toLowerCase();

                Supplier<Shape> factory = ShapeRegistry.getFactory(type);
                if (factory != null) {
                        Shape shape = factory.get();
                        System.out.println(shape);
                }

                /*
                 * List<String> names = Arrays.asList("Ana", "Juan", "María", "Pedro");
                 * 
                 * // Stream secuencial
                 * names.stream()
                 * .filter(name -> name.length() > 3)
                 * .forEach(System.out::println);
                 * 
                 * // Stream paralelo
                 * names.parallelStream()
                 * .filter(name -> name.length() > 3)
                 * .forEach(System.out::println);
                 */

                /*
                 * List<Integer> lisint = new ArrayList<>();
                 * var result = lisint.stream().filter(v -> v % 2 == 0);
                 * result.forEach(System.out::println);
                 * // result.collect(Collectors.toList());
                 * 
                 * // result.forEach(System.out::println);
                 * 
                 * UUID id = UUID.randomUUID();
                 * var unique = new Unique(id);
                 * var unique1 = new Unique(UUID.randomUUID());
                 * 
                 * Set<Unique> set = new HashSet<>();
                 * 
                 * set.add(unique);
                 * set.add(unique1);
                 * 
                 * var pinguino = new Pinguino(5);
                 * PrintAves.PrintAveNoVolaora(pinguino, System.out::println);
                 * // PrintAves.PrintAveVoladora(pinguino);
                 * System.out.println("Hello World!");
                 * printDate(LocalDateTime.now(), System.out::println);
                 * printDate(LocalDateTime.now(), App::method);
                 */
        }
        /*
         * private static void printDate(LocalDateTime date, Consumer<LocalDateTime>
         * printer) {
         * printer.accept(date);
         * }
         * 
         * private static void method(LocalDateTime date) {
         * System.out.println("He entrado al metodo");
         * }
         */

        public static void streamoperations() {
                List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

                // Buscar el primero
                var first = numeros.stream().findFirst();
                if (first.isPresent()) {
                        System.out.println(first.get());
                }

                // Buscar el último
                numeros.stream().reduce((a, b) -> b).ifPresent(u -> System.out.println("Último: " + u));

                // Filtrar el primer par
                numeros.stream()
                                .filter(n -> n % 2 == 0)
                                .findFirst()
                                .ifPresent(p -> System.out.println("Primer par: " + p));

                // Filtrar el último par
                numeros.stream()
                                .filter(n -> n % 2 == 0)
                                .reduce((a, b) -> b)
                                .ifPresent(u -> System.out.println("Último par: " + u));

                // Filtrar los pares
                List<Integer> pares = numeros.stream()
                                .filter(n -> n % 2 == 0)
                                .collect(Collectors.toList());
                System.out.println("Pares: " + pares);

                // Filtrar los pares y obtener su cuadrado
                List<Integer> cuadradosPares = numeros.stream()
                                .filter(n -> n % 2 == 0)
                                .map(n -> n * n)
                                .collect(Collectors.toList());
                System.out.println("Cuadrados de pares: " + cuadradosPares);

                // Sumar los valores
                int suma = numeros.stream()
                                .mapToInt(Integer::intValue)
                                .sum();
                System.out.println("Suma: " + suma);
        }
}
