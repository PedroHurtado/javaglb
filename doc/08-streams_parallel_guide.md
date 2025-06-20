# Streams y Parallel Streams en Java

## Índice
1. [Introducción a Streams](#introducción-a-streams)
2. [Concurrency](#concurrency)
3. [Parallel Streams](#parallel-streams)
4. [Terminal Operations: Collectors](#terminal-operations-collectors)
5. [Creating Custom Streams](#creating-custom-streams)
6. [Mejores Prácticas](#mejores-prácticas)
7. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## Introducción a Streams

Los Streams en Java 8+ son una abstracción que permite procesar colecciones de datos de manera funcional. Proporcionan una forma declarativa de trabajar con datos, enfocándose en **qué** hacer en lugar de **cómo** hacerlo.

### Características principales:
- **Inmutables**: No modifican la fuente original
- **Lazy**: Las operaciones intermedias se ejecutan solo cuando se invoca una operación terminal
- **Funcionales**: Permiten el uso de programación funcional
- **Componibles**: Se pueden encadenar operaciones

---

## Concurrency

### ¿Qué es la Concurrencia?

La concurrencia es la capacidad de ejecutar múltiples tareas simultáneamente. En el contexto de Streams, esto se refiere a la capacidad de procesar elementos de una colección en paralelo, aprovechando múltiples núcleos del procesador.

### Tipos de Concurrencia en Java:

#### 1. Concurrencia basada en Threads
```java
// Ejemplo tradicional con threads
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
ExecutorService executor = Executors.newFixedThreadPool(4);

numbers.forEach(num -> {
    executor.submit(() -> {
        System.out.println("Procesando: " + num + 
                          " en thread: " + Thread.currentThread().getName());
    });
});
```

#### 2. Concurrencia con Fork/Join Framework
```java
// Fork/Join es la base de los Parallel Streams
ForkJoinPool customThreadPool = new ForkJoinPool(4);
customThreadPool.submit(() -> 
    numbers.parallelStream().forEach(System.out::println)
).get();
```

### Beneficios de la Concurrencia:
- **Mejor rendimiento** en operaciones CPU-intensivas
- **Aprovechamiento** de múltiples núcleos
- **Escalabilidad** en aplicaciones de alto rendimiento

### Consideraciones:
- **Overhead** de creación y gestión de threads
- **Sincronización** y posibles condiciones de carrera
- **Complejidad** adicional en el código

---

## Parallel Streams

### Introducción a Parallel Streams

Los Parallel Streams son una extensión de los Streams regulares que permiten el procesamiento paralelo automático de los elementos. Utilizan el Fork/Join Framework internamente.

### Creación de Parallel Streams

#### Método 1: Desde una colección
```java
List<String> names = Arrays.asList("Ana", "Juan", "María", "Pedro");

// Stream secuencial
names.stream()
     .filter(name -> name.length() > 3)
     .forEach(System.out::println);

// Stream paralelo
names.parallelStream()
     .filter(name -> name.length() > 3)
     .forEach(System.out::println);
```

#### Método 2: Conversión de Stream a Parallel
```java
List<Integer> numbers = IntStream.rangeClosed(1, 1000)
                                .boxed()
                                .collect(Collectors.toList());

// Convertir stream secuencial a paralelo
numbers.stream()
       .parallel()
       .filter(n -> n % 2 == 0)
       .mapToInt(Integer::intValue)
       .sum();
```

### Cuándo usar Parallel Streams

#### ✅ Casos apropiados:
- **Grandes volúmenes de datos** (>10,000 elementos aproximadamente)
- **Operaciones CPU-intensivas** (cálculos complejos)
- **Operaciones independientes** entre elementos
- **Funciones puras** sin efectos secundarios

```java
// Ejemplo: Cálculo de números primos
List<Integer> numbers = IntStream.rangeClosed(1, 100000)
                                .boxed()
                                .collect(Collectors.toList());

long primeCount = numbers.parallelStream()
                        .filter(this::isPrime)
                        .count();

private boolean isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) return false;
    }
    return true;
}
```

#### ❌ Casos no apropiados:
- **Pequeños volúmenes de datos**
- **Operaciones rápidas** (el overhead supera el beneficio)
- **Operaciones con efectos secundarios**
- **Operaciones que requieren orden específico**

### Configuración del ForkJoinPool

```java
// Configurar el pool de threads personalizado
ForkJoinPool customThreadPool = new ForkJoinPool(8);
try {
    Integer result = customThreadPool.submit(() ->
        numbers.parallelStream()
               .filter(n -> n % 2 == 0)
               .mapToInt(Integer::intValue)
               .sum()
    ).get();
    
    System.out.println("Resultado: " + result);
} finally {
    customThreadPool.shutdown();
}
```

### Comparación de Rendimiento

```java
// Benchmark simple
public void benchmarkStreams() {
    List<Integer> largeList = IntStream.rangeClosed(1, 10_000_000)
                                     .boxed()
                                     .collect(Collectors.toList());
    
    // Stream secuencial
    long startTime = System.currentTimeMillis();
    long sequentialSum = largeList.stream()
                                 .mapToLong(i -> i * i)
                                 .sum();
    long sequentialTime = System.currentTimeMillis() - startTime;
    
    // Stream paralelo
    startTime = System.currentTimeMillis();
    long parallelSum = largeList.parallelStream()
                               .mapToLong(i -> i * i)
                               .sum();
    long parallelTime = System.currentTimeMillis() - startTime;
    
    System.out.println("Secuencial: " + sequentialTime + "ms");
    System.out.println("Paralelo: " + parallelTime + "ms");
    System.out.println("Speedup: " + (double)sequentialTime / parallelTime);
}
```

---

## Terminal Operations: Collectors

### Introducción a Collectors

Los Collectors son operaciones terminales especializadas que transforman los elementos de un Stream en diferentes tipos de colecciones o estructuras de datos.

### Collectors Básicos

#### 1. Colecciones Básicas
```java
List<String> names = Arrays.asList("Ana", "Juan", "María", "Pedro", "Ana");

// Recolectar en List
List<String> namesList = names.stream()
                             .collect(Collectors.toList());

// Recolectar en Set (elimina duplicados)
Set<String> namesSet = names.stream()
                           .collect(Collectors.toSet());

// Recolectar en TreeSet (ordenado)
TreeSet<String> sortedNames = names.stream()
                                  .collect(Collectors.toCollection(TreeSet::new));
```

#### 2. Collector toMap
```java
List<Person> people = Arrays.asList(
    new Person("Ana", 25),
    new Person("Juan", 30),
    new Person("María", 28)
);

// Crear Map: nombre -> edad
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge
    ));

// Crear Map con función de merge para duplicados
Map<String, Integer> nameToAgeWithMerge = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge,
        Integer::max  // En caso de duplicados, tomar el mayor
    ));
```

### Collectors de Agrupación

#### 1. groupingBy
```java
List<Person> people = Arrays.asList(
    new Person("Ana", 25, "Desarrollo"),
    new Person("Juan", 30, "Desarrollo"),
    new Person("María", 28, "Marketing"),
    new Person("Pedro", 35, "Marketing")
);

// Agrupar por departamento
Map<String, List<Person>> peopleByDepartment = people.stream()
    .collect(Collectors.groupingBy(Person::getDepartment));

// Agrupar y contar
Map<String, Long> countByDepartment = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.counting()
    ));

// Agrupar y calcular promedio de edad
Map<String, Double> avgAgeByDepartment = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.averagingInt(Person::getAge)
    ));
```

#### 2. partitioningBy
```java
// Particionar por condición (true/false)
Map<Boolean, List<Person>> partitionedByAge = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 30));

List<Person> youngPeople = partitionedByAge.get(false);
List<Person> olderPeople = partitionedByAge.get(true);
```

### Collectors de Reducción

#### 1. Collectors Numéricos
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Suma
Integer sum = numbers.stream()
                    .collect(Collectors.summingInt(Integer::intValue));

// Promedio
Double average = numbers.stream()
                       .collect(Collectors.averagingInt(Integer::intValue));

// Estadísticas
IntSummaryStatistics stats = numbers.stream()
                                   .collect(Collectors.summarizingInt(Integer::intValue));

System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Average: " + stats.getAverage());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
```

#### 2. joining
```java
List<String> words = Arrays.asList("Java", "Stream", "API");

// Unir con separador
String joined = words.stream()
                    .collect(Collectors.joining(", "));
// Resultado: "Java, Stream, API"

// Unir con separador, prefijo y sufijo
String formattedJoined = words.stream()
                             .collect(Collectors.joining(", ", "[", "]"));
// Resultado: "[Java, Stream, API]"
```

### Collectors Personalizados

#### 1. Collector.of()
```java
// Collector personalizado para crear un Set inmutable
Collector<String, ?, Set<String>> toImmutableSet = Collector.of(
    HashSet::new,           // supplier
    Set::add,               // accumulator
    (set1, set2) -> {       // combiner
        set1.addAll(set2);
        return set1;
    },
    Collections::unmodifiableSet // finisher
);

Set<String> immutableSet = names.stream()
                                .collect(toImmutableSet);
```

#### 2. Collector complejo para estadísticas personalizadas
```java
public class PersonStats {
    private int count;
    private int totalAge;
    private String oldestName;
    private int maxAge;
    
    // constructors, getters, setters...
}

Collector<Person, ?, PersonStats> personStatsCollector = Collector.of(
    PersonStats::new,
    (stats, person) -> {
        stats.setCount(stats.getCount() + 1);
        stats.setTotalAge(stats.getTotalAge() + person.getAge());
        if (person.getAge() > stats.getMaxAge()) {
            stats.setMaxAge(person.getAge());
            stats.setOldestName(person.getName());
        }
    },
    (stats1, stats2) -> {
        // Combinar dos estadísticas
        PersonStats combined = new PersonStats();
        combined.setCount(stats1.getCount() + stats2.getCount());
        combined.setTotalAge(stats1.getTotalAge() + stats2.getTotalAge());
        
        if (stats1.getMaxAge() > stats2.getMaxAge()) {
            combined.setMaxAge(stats1.getMaxAge());
            combined.setOldestName(stats1.getOldestName());
        } else {
            combined.setMaxAge(stats2.getMaxAge());
            combined.setOldestName(stats2.getOldestName());
        }
        return combined;
    }
);

PersonStats stats = people.stream()
                          .collect(personStatsCollector);
```

---

## Creating Custom Streams

### Métodos para Crear Streams

#### 1. Stream.of()
```java
// Crear stream desde elementos individuales
Stream<String> stream = Stream.of("a", "b", "c");

// Crear stream desde array
String[] array = {"x", "y", "z"};
Stream<String> streamFromArray = Stream.of(array);
// O también: Arrays.stream(array)
```

#### 2. Stream.generate()
```java
// Generar stream infinito
Stream<Double> randomNumbers = Stream.generate(Math::random);

// Generar stream infinito con límite
Stream<String> hellos = Stream.generate(() -> "Hello")
                             .limit(5);

// Generar números secuenciales
AtomicInteger counter = new AtomicInteger(0);
Stream<Integer> numbers = Stream.generate(counter::incrementAndGet)
                                .limit(10);
```

#### 3. Stream.iterate()
```java
// Secuencia infinita con función
Stream<Integer> evenNumbers = Stream.iterate(0, n -> n + 2)
                                   .limit(10);

// Con condición de parada (Java 9+)
Stream<Integer> numbersUntil100 = Stream.iterate(0, n -> n < 100, n -> n + 2);

// Fibonacci
Stream<BigInteger> fibonacci = Stream.iterate(
    new BigInteger[]{BigInteger.ZERO, BigInteger.ONE},
    pair -> new BigInteger[]{pair[1], pair[0].add(pair[1])}
).map(pair -> pair[0]);
```

#### 4. Stream.builder()
```java
// Builder pattern para crear streams
Stream.Builder<String> builder = Stream.builder();
builder.add("Java")
       .add("Python")
       .add("JavaScript");

Stream<String> languages = builder.build();
```

### Streams desde Rangos

#### 1. IntStream, LongStream, DoubleStream
```java
// Rango de enteros
IntStream intRange = IntStream.range(1, 10);        // 1 a 9
IntStream intRangeClosed = IntStream.rangeClosed(1, 10); // 1 a 10

// Convertir a Stream de objetos
Stream<Integer> boxedStream = IntStream.range(1, 10).boxed();

// Generar doubles aleatorios
DoubleStream randomDoubles = DoubleStream.generate(Math::random)
                                        .limit(5);
```

### Streams desde Archivos

#### 1. Files.lines()
```java
// Leer archivo línea por línea
try (Stream<String> lines = Files.lines(Paths.get("archivo.txt"))) {
    lines.filter(line -> !line.isEmpty())
         .map(String::toUpperCase)
         .forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

#### 2. Files.walk()
```java
// Recorrer directorio
try (Stream<Path> paths = Files.walk(Paths.get("src"))) {
    paths.filter(Files::isRegularFile)
         .filter(path -> path.toString().endsWith(".java"))
         .forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

### Streams Personalizados con Spliterator

```java
// Crear un Spliterator personalizado
public class FibonacciSpliterator implements Spliterator<Long> {
    private long current = 0;
    private long next = 1;
    private final long limit;

    public FibonacciSpliterator(long limit) {
        this.limit = limit;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Long> action) {
        if (current <= limit) {
            action.accept(current);
            long temp = current + next;
            current = next;
            next = temp;
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Long> trySplit() {
        return null; // No se puede dividir
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return ORDERED | IMMUTABLE | NONNULL;
    }
}

// Usar el Spliterator personalizado
Stream<Long> fibonacciStream = StreamSupport.stream(
    new FibonacciSpliterator(100), false);

fibonacciStream.forEach(System.out::println);
```

### Streams Asíncronos (CompletableFuture)

```java
// Crear stream de CompletableFuture
List<CompletableFuture<String>> futures = Arrays.asList(
    CompletableFuture.supplyAsync(() -> "Tarea 1"),
    CompletableFuture.supplyAsync(() -> "Tarea 2"),
    CompletableFuture.supplyAsync(() -> "Tarea 3")
);

// Procesar resultados cuando estén listos
List<String> results = futures.stream()
    .map(CompletableFuture::join)
    .collect(Collectors.toList());

// Stream de resultados asíncronos
Stream<String> asyncResults = futures.stream()
    .map(future -> {
        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    });
```

---

## Mejores Prácticas

### 1. Cuándo usar Parallel Streams
- **Grandes volúmenes de datos** (>10,000 elementos)
- **Operaciones CPU-intensivas**
- **Operaciones independientes**
- **Medición de rendimiento** antes de decidir

### 2. Evitar Efectos Secundarios
```java
// ❌ Malo: efectos secundarios
List<String> results = new ArrayList<>();
words.parallelStream()
     .filter(word -> word.length() > 3)
     .forEach(results::add); // ¡Condición de carrera!

// ✅ Bueno: sin efectos secundarios
List<String> results = words.parallelStream()
                           .filter(word -> word.length() > 3)
                           .collect(Collectors.toList());
```

### 3. Usar Collectors Apropiados
```java
// ✅ Eficiente para parallel streams
Map<String, List<Person>> grouped = people.parallelStream()
    .collect(Collectors.groupingBy(Person::getDepartment));

// ❌ Menos eficiente
Map<String, List<Person>> grouped = people.parallelStream()
    .collect(Collectors.toConcurrentMap(
        Person::getDepartment,
        Arrays::asList,
        (list1, list2) -> { 
            list1.addAll(list2); 
            return list1; 
        }
    ));
```

### 4. Gestión de Recursos
```java
// ✅ Usar try-with-resources para streams que requieren cierre
try (Stream<String> lines = Files.lines(path)) {
    return lines.filter(line -> line.contains("error"))
                .count();
}
```

---

## Ejemplos Prácticos

### Ejemplo 1: Procesamiento de Archivos Log
```java
public class LogProcessor {
    
    public void processLogFile(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            Map<String, Long> errorCounts = lines.parallelStream()
                .filter(line -> line.contains("ERROR"))
                .map(this::extractErrorType)
                .collect(Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()
                ));
            
            errorCounts.entrySet().stream()
                      .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                      .limit(10)
                      .forEach(entry -> 
                          System.out.println(entry.getKey() + ": " + entry.getValue())
                      );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String extractErrorType(String logLine) {
        // Lógica para extraer tipo de error
        return logLine.split(" ")[2]; // Simplificado
    }
}
```

### Ejemplo 2: Análisis de Datos de Ventas
```java
public class SalesAnalyzer {
    
    public void analyzeSales(List<Sale> sales) {
        // Ventas por región
        Map<String, Double> salesByRegion = sales.parallelStream()
            .collect(Collectors.groupingBy(
                Sale::getRegion,
                Collectors.summingDouble(Sale::getAmount)
            ));
        
        // Top 5 productos más vendidos
        List<Map.Entry<String, Long>> topProducts = sales.parallelStream()
            .collect(Collectors.groupingBy(
                Sale::getProduct,
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toList());
        
        // Estadísticas por mes
        Map<String, DoubleSummaryStatistics> monthlyStats = sales.parallelStream()
            .collect(Collectors.groupingBy(
                sale -> sale.getDate().getMonth().toString(),
                Collectors.summarizingDouble(Sale::getAmount)
            ));
        
        // Mostrar resultados
        System.out.println("=== Ventas por Región ===");
        salesByRegion.forEach((region, total) -> 
            System.out.printf("%s: $%.2f%n", region, total));
        
        System.out.println("\n=== Top 5 Productos ===");
        topProducts.forEach(entry -> 
            System.out.printf("%s: %d ventas%n", entry.getKey(), entry.getValue()));
        
        System.out.println("\n=== Estadísticas Mensuales ===");
        monthlyStats.forEach((month, stats) -> 
            System.out.printf("%s: Promedio $%.2f, Total $%.2f%n", 
                             month, stats.getAverage(), stats.getSum()));
    }
}
```

### Ejemplo 3: Procesamiento de Datos en Tiempo Real
```java
public class RealTimeProcessor {
    
    public void processDataStream() {
        // Simular stream de datos en tiempo real
        Stream<SensorData> dataStream = Stream.generate(this::generateSensorData)
                                             .limit(1000);
        
        // Procesar datos en paralelo
        Map<String, DoubleSummaryStatistics> sensorStats = dataStream
            .parallel()
            .filter(data -> data.getTimestamp().isAfter(LocalDateTime.now().minusHours(1)))
            .filter(data -> data.getValue() > 0) // Filtrar lecturas válidas
            .collect(Collectors.groupingBy(
                SensorData::getSensorId,
                Collectors.summarizingDouble(SensorData::getValue)
            ));
        
        // Detectar anomalías
        List<String> anomalouseSensors = sensorStats.entrySet().parallelStream()
            .filter(entry -> {
                DoubleSummaryStatistics stats = entry.getValue();
                double mean = stats.getAverage();
                double max = stats.getMax();
                double min = stats.getMin();
                return (max - mean) > 2 * (mean - min); // Detección simple de outliers
            })
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        System.out.println("Sensores con anomalías: " + anomalouseSensors);
    }
    
    private SensorData generateSensorData() {
        // Generar datos de sensor simulados
        return new SensorData(
            "SENSOR_" + (int)(Math.random() * 10),
            Math.random() * 100,
            LocalDateTime.now()
        );
    }
}
```

---

## Conclusión

Los Streams y Parallel Streams en Java proporcionan una API poderosa y elegante para el procesamiento de datos. La clave está en entender cuándo y cómo usar cada característica:

- **Streams secuenciales** para la mayoría de operaciones
- **Parallel Streams** para grandes volúmenes de datos y operaciones CPU-intensivas
- **Collectors** para transformar y agrupar datos eficientemente
- **Custom Streams** para casos específicos y fuentes de datos personalizadas

Recuerda siempre medir el rendimiento y considerar las características de tus datos antes de decidir usar paralelización. La programación funcional con Streams hace el código más legible y mantenible, pero debe usarse con conocimiento de sus implicaciones de rendimiento.

---

## Recursos Adicionales

- [Documentación oficial de Oracle](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Guía de Parallel Streams](https://docs.oracle.com/javase/tutorial/collections/streams/parallelism.html)
- [Collectors API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html)

---
