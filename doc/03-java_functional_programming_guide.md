# Programación Funcional en Java - Guía Completa

## Tabla de Contenidos
1. [Introducción a la Programación Funcional](#introducción-a-la-programación-funcional)
2. [Características Principales](#características-principales)
3. [Funciones Lambda en Java](#funciones-lambda-en-java)
4. [Interfaces Funcionales](#interfaces-funcionales)
5. [Method References](#method-references)
6. [Closures (Clausuras)](#closures-clausuras)
7. [Callbacks y Higher-Order Functions](#callbacks-y-higher-order-functions)
8. [Stream API](#stream-api)
9. [Composición de Funciones](#composición-de-funciones)
10. [Monadas y Optional](#monadas-y-optional)
11. [Inmutabilidad](#inmutabilidad)
12. [Ejemplos Prácticos](#ejemplos-prácticos)
13. [Comparación: Imperativo vs Funcional](#comparación-imperativo-vs-funcional)

## Introducción a la Programación Funcional

La programación funcional es un paradigma que trata la computación como la evaluación de funciones matemáticas, evitando cambios de estado y datos mutables. Java 8 introdujo características funcionales que permiten escribir código más expresivo y conciso.

### Conceptos Clave:
- **Inmutabilidad**: Los datos no cambian después de su creación
- **Funciones Puras**: Funciones sin efectos secundarios
- **Higher-Order Functions**: Funciones que reciben o retornan otras funciones
- **Lazy Evaluation**: Evaluación perezosa de expresiones
- **Composición**: Combinar funciones simples para crear funciones complejas

## Características Principales

### 1. Funciones como Ciudadanos de Primera Clase
```java
// Las funciones pueden ser asignadas a variables
Function<Integer, Integer> duplicar = x -> x * 2;
Predicate<String> esVacio = String::isEmpty;
Consumer<String> imprimir = System.out::println;

// Las funciones pueden ser pasadas como parámetros
public static void procesarLista(List<Integer> lista, Function<Integer, Integer> operacion) {
    lista.stream()
         .map(operacion)
         .forEach(System.out::println);
}

// Uso
List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
procesarLista(numeros, x -> x * x); // Elevar al cuadrado
procesarLista(numeros, x -> x + 10); // Sumar 10
```

### 2. Inmutabilidad
```java
// Clase inmutable
public final class Persona {
    private final String nombre;
    private final int edad;
    private final List<String> hobbies;
    
    public Persona(String nombre, int edad, List<String> hobbies) {
        this.nombre = nombre;
        this.edad = edad;
        this.hobbies = Collections.unmodifiableList(new ArrayList<>(hobbies));
    }
    
    // Métodos que retornan nuevas instancias en lugar de modificar
    public Persona conNombre(String nuevoNombre) {
        return new Persona(nuevoNombre, this.edad, this.hobbies);
    }
    
    public Persona conEdad(int nuevaEdad) {
        return new Persona(this.nombre, nuevaEdad, this.hobbies);
    }
    
    public Persona agregarHobby(String hobby) {
        List<String> nuevosHobbies = new ArrayList<>(this.hobbies);
        nuevosHobbies.add(hobby);
        return new Persona(this.nombre, this.edad, nuevosHobbies);
    }
    
    // Getters...
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public List<String> getHobbies() { return hobbies; }
}

// Uso inmutable
Persona persona = new Persona("Juan", 25, Arrays.asList("Leer", "Correr"));
Persona personaModificada = persona.conEdad(26).agregarHobby("Nadar");
// 'persona' sigue siendo la misma, 'personaModificada' es una nueva instancia
```

### 3. Funciones Puras
```java
// Función pura - siempre retorna el mismo resultado para los mismos parámetros
public static int sumar(int a, int b) {
    return a + b;
}

// Función pura - no modifica estado externo
public static List<String> convertirAMayusculas(List<String> lista) {
    return lista.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
}

// Función impura - depende de estado externo
private static int contador = 0;
public static int incrementar() {
    return ++contador; // Modifica estado externo
}

// Función impura - tiene efectos secundarios
public static int sumarEImprimir(int a, int b) {
    int resultado = a + b;
    System.out.println(resultado); // Efecto secundario
    return resultado;
}
```

## Funciones Lambda en Java

### Sintaxis de Lambda
```java
// Sintaxis completa
(parametros) -> { cuerpo de la función }

// Ejemplos básicos
Runnable tarea = () -> System.out.println("Ejecutando tarea");

Consumer<String> imprimir = (mensaje) -> System.out.println(mensaje);
// Simplificado
Consumer<String> imprimir2 = mensaje -> System.out.println(mensaje);

Function<Integer, Integer> cuadrado = (x) -> x * x;
// Simplificado
Function<Integer, Integer> cuadrado2 = x -> x * x;

Predicate<Integer> esPar = numero -> numero % 2 == 0;

BinaryOperator<Integer> suma = (a, b) -> a + b;

// Lambda con múltiples líneas
Function<String, String> procesarTexto = texto -> {
    String limpio = texto.trim();
    String mayuscula = limpio.toUpperCase();
    return mayuscula.replaceAll("\\s+", "_");
};
```

### Captura de Variables (Variable Capture)
```java
public class EjemploCaptura {
    private String prefijo = "Mensaje: ";
    
    public void ejemploCaptura() {
        String sufijo = " - Procesado"; // Effectively final
        
        List<String> mensajes = Arrays.asList("Hola", "Mundo", "Java");
        
        // Captura de variable local (effectively final)
        mensajes.stream()
                .map(msg -> prefijo + msg + sufijo) // Captura prefijo y sufijo
                .forEach(System.out::println);
        
        // Captura de this
        Function<String, String> formatter = msg -> this.formatear(msg);
    }
    
    private String formatear(String mensaje) {
        return "[" + mensaje + "]";
    }
}
```

## Interfaces Funcionales

### Interfaces Funcionales Principales
```java
// Function<T, R> - Recibe T, retorna R
Function<String, Integer> longitud = String::length;
Function<Integer, String> toString = Object::toString;

// Predicate<T> - Recibe T, retorna boolean
Predicate<String> esVacio = String::isEmpty;
Predicate<Integer> esMayorQue10 = x -> x > 10;

// Consumer<T> - Recibe T, no retorna nada
Consumer<String> imprimir = System.out::println;
Consumer<List<String>> limpiarLista = List::clear;

// Supplier<T> - No recibe nada, retorna T
Supplier<String> obtenerFecha = () -> LocalDate.now().toString();
Supplier<List<String>> crearLista = ArrayList::new;

// BinaryOperator<T> - Recibe dos T, retorna T
BinaryOperator<Integer> sumar = Integer::sum;
BinaryOperator<String> concatenar = (a, b) -> a + b;

// UnaryOperator<T> - Recibe T, retorna T
UnaryOperator<String> aMayusculas = String::toUpperCase;
UnaryOperator<Integer> duplicar = x -> x * 2;
```

### Interfaces Funcionales Especializadas
```java
// Para primitivos - evita boxing/unboxing
IntFunction<String> intToString = Integer::toString;
IntPredicate esPar = x -> x % 2 == 0;
IntConsumer imprimirInt = System.out::println;
IntSupplier obtenerRandom = () -> (int) (Math.random() * 100);

// Para dos parámetros
BiFunction<String, String, String> concatenar = (a, b) -> a + " " + b;
BiPredicate<String, String> sonIguales = String::equals;
BiConsumer<String, Integer> imprimirConIndice = (texto, indice) -> 
    System.out.println(indice + ": " + texto);
```

### Creando Interfaces Funcionales Personalizadas
```java
@FunctionalInterface
public interface Calculadora<T> {
    T calcular(T a, T b);
    
    // Métodos default permitidos
    default T calcularTriple(T a, T b, T c) {
        return calcular(calcular(a, b), c);
    }
    
    // Métodos estáticos permitidos
    static <T> Calculadora<T> suma() {
        return (a, b) -> {
            if (a instanceof Integer && b instanceof Integer) {
                return (T) Integer.valueOf(((Integer) a) + ((Integer) b));
            }
            throw new UnsupportedOperationException("Tipo no soportado");
        };
    }
}

// Uso
Calculadora<Integer> sumadora = (a, b) -> a + b;
Calculadora<String> concatenadora = (a, b) -> a + b;
Calculadora<Double> multiplicadora = (a, b) -> a * b;

Integer resultado = sumadora.calcular(5, 3); // 8
String texto = concatenadora.calcular("Hola", " Mundo"); // "Hola Mundo"
```

## Method References

### Tipos de Method References
```java
// 1. Referencia a método estático
// Clase::metodoEstatico
Function<String, Integer> parseInt = Integer::parseInt;
Predicate<String> isEmpty = String::isEmpty;

// 2. Referencia a método de instancia de un objeto particular
// objeto::metodoInstancia
String saludo = "Hola Mundo";
Supplier<String> obtenerSaludo = saludo::toString;
Supplier<Integer> obtenerLongitud = saludo::length;

// 3. Referencia a método de instancia de un tipo arbitrario
// Clase::metodoInstancia
Function<String, String> toUpperCase = String::toUpperCase;
Function<String, Integer> length = String::length;

// 4. Referencia a constructor
// Clase::new
Supplier<ArrayList<String>> crearLista = ArrayList::new;
Function<String, StringBuilder> crearStringBuilder = StringBuilder::new;
Function<Integer, int[]> crearArray = int[]::new;
```

### Ejemplos Prácticos de Method References
```java
public class EjemplosMethodReferences {
    
    public static void ejemplosBasicos() {
        List<String> nombres = Arrays.asList("Ana", "Luis", "María", "Carlos");
        
        // Usando lambda
        nombres.stream()
               .map(nombre -> nombre.toUpperCase())
               .forEach(nombre -> System.out.println(nombre));
        
        // Usando method references - más limpio
        nombres.stream()
               .map(String::toUpperCase)
               .forEach(System.out::println);
        
        // Ordenamiento
        List<String> palabras = Arrays.asList("java", "python", "javascript", "go");
        
        // Con lambda
        palabras.sort((a, b) -> a.compareTo(b));
        
        // Con method reference
        palabras.sort(String::compareTo);
    }
    
    public static void ejemplosAvanzados() {
        List<Persona> personas = Arrays.asList(
            new Persona("Ana", 25, Arrays.asList("Leer")),
            new Persona("Luis", 30, Arrays.asList("Correr")),
            new Persona("María", 28, Arrays.asList("Pintar"))
        );
        
        // Mapear a nombres
        List<String> nombres = personas.stream()
                                      .map(Persona::getNombre)
                                      .collect(Collectors.toList());
        
        // Filtrar y transformar
        List<String> nombresJovenes = personas.stream()
                                             .filter(p -> p.getEdad() < 30)
                                             .map(Persona::getNombre)
                                             .collect(Collectors.toList());
        
        // Crear objetos usando constructor reference
        List<String> nombresPersonas = Arrays.asList("Pedro", "Laura", "José");
        List<Persona> nuevasPersonas = nombresPersonas.stream()
                                                     .map(nombre -> new Persona(nombre, 0, new ArrayList<>()))
                                                     .collect(Collectors.toList());
    }
}
```

## Closures (Clausuras)

### Concepto de Closure
```java
public class EjemploClosures {
    
    // Closure simple - captura variable del ámbito exterior
    public static Function<Integer, Integer> crearSumador(int valorBase) {
        return numero -> numero + valorBase; // Captura 'valorBase'
    }
    
    // Closure con estado - contador
    public static Supplier<Integer> crearContador() {
        // Simulamos estado mutable con array (hack para Java)
        int[] contador = {0};
        return () -> ++contador[0];
    }
    
    // Closure más complejo - configuración
    public static Function<String, String> crearFormateador(String prefijo, String sufijo) {
        return texto -> prefijo + texto + sufijo;
    }
    
    // Factory de validadores usando closures
    public static Predicate<Integer> crearValidadorRango(int min, int max) {
        return numero -> numero >= min && numero <= max;
    }
    
    public static void ejemplos() {
        // Uso de closures
        Function<Integer, Integer> sumar10 = crearSumador(10);
        Function<Integer, Integer> sumar100 = crearSumador(100);
        
        System.out.println(sumar10.apply(5));  // 15
        System.out.println(sumar100.apply(5)); // 105
        
        // Contador con estado
        Supplier<Integer> contador = crearContador();
        System.out.println(contador.get()); // 1
        System.out.println(contador.get()); // 2
        System.out.println(contador.get()); // 3
        
        // Formateador
        Function<String, String> formatearHTML = crearFormateador("<p>", "</p>");
        Function<String, String> formatearMarkdown = crearFormateador("**", "**");
        
        System.out.println(formatearHTML.apply("Hola"));     // <p>Hola</p>
        System.out.println(formatearMarkdown.apply("Hola")); // **Hola**
        
        // Validadores
        Predicate<Integer> esEdadValida = crearValidadorRango(0, 120);
        Predicate<Integer> esNotaValida = crearValidadorRango(0, 10);
        
        System.out.println(esEdadValida.test(25));  // true
        System.out.println(esNotaValida.test(15));  // false
    }
}
```

### Closures Avanzados
```java
public class ClosuresAvanzados {
    
    // Closure que retorna múltiples funciones relacionadas
    public static Map<String, Function<Double, Double>> crearCalculadoraImpuestos(double tasaImpuesto) {
        Map<String, Function<Double, Double>> calculadora = new HashMap<>();
        
        calculadora.put("calcularImpuesto", monto -> monto * tasaImpuesto);
        calculadora.put("calcularTotal", monto -> monto + (monto * tasaImpuesto));
        calculadora.put("calcularNeto", total -> total / (1 + tasaImpuesto));
        
        return calculadora;
    }
    
    // Closure con configuración compleja
    public static Function<List<Integer>, List<Integer>> crearProcesadorLista(
            Predicate<Integer> filtro,
            Function<Integer, Integer> transformacion,
            Comparator<Integer> ordenamiento) {
        
        return lista -> lista.stream()
                           .filter(filtro)
                           .map(transformacion)
                           .sorted(ordenamiento)
                           .collect(Collectors.toList());
    }
    
    // Closure para crear decoradores de funciones
    public static <T, R> Function<T, R> conLog(Function<T, R> funcion, String nombre) {
        return input -> {
            System.out.println("Ejecutando: " + nombre + " con entrada: " + input);
            R resultado = funcion.apply(input);
            System.out.println("Resultado: " + resultado);
            return resultado;
        };
    }
    
    public static void ejemplos() {
        // Calculadora de impuestos
        Map<String, Function<Double, Double>> calculadoraIVA = crearCalculadoraImpuestos(0.21);
        
        double monto = 100.0;
        double impuesto = calculadoraIVA.get("calcularImpuesto").apply(monto); // 21.0
        double total = calculadoraIVA.get("calcularTotal").apply(monto);       // 121.0
        
        // Procesador de lista configurado
        Function<List<Integer>, List<Integer>> procesador = crearProcesadorLista(
            n -> n > 0,           // Solo positivos
            n -> n * 2,           // Duplicar
            Integer::compareTo    // Orden natural
        );
        
        List<Integer> numeros = Arrays.asList(-1, 3, -2, 5, 1, -4, 2);
        List<Integer> procesados = procesador.apply(numeros); // [2, 4, 6, 10]
        
        // Decorador con log
        Function<Integer, Integer> cuadrado = x -> x * x;
        Function<Integer, Integer> cuadradoConLog = conLog(cuadrado, "Cuadrado");
        
        int resultado = cuadradoConLog.apply(5); // Imprime log y retorna 25
    }
}
```

## Callbacks y Higher-Order Functions

### Callbacks Tradicionales vs Funcionales
```java
// Callback tradicional con interfaz
interface Callback {
    void onComplete(String resultado);
    void onError(Exception error);
}

class ServicioTradicional {
    public void procesar(String datos, Callback callback) {
        try {
            Thread.sleep(1000); // Simular procesamiento
            String resultado = "Procesado: " + datos;
            callback.onComplete(resultado);
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}

// Callback funcional
class ServicioFuncional {
    public void procesar(String datos, 
                        Consumer<String> onSuccess, 
                        Consumer<Exception> onError) {
        try {
            Thread.sleep(1000);
            String resultado = "Procesado: " + datos;
            onSuccess.accept(resultado);
        } catch (Exception e) {
            onError.accept(e);
        }
    }
    
    // Versión más avanzada con CompletableFuture
    public CompletableFuture<String> procesarAsync(String datos) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return "Procesado: " + datos;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
```

### Higher-Order Functions
```java
public class HigherOrderFunctions {
    
    // Función que recibe otra función como parámetro
    public static <T, R> List<R> mapear(List<T> lista, Function<T, R> mapper) {
        return lista.stream()
                   .map(mapper)
                   .collect(Collectors.toList());
    }
    
    // Función que retorna otra función
    public static <T> Predicate<T> negar(Predicate<T> predicado) {
        return predicado.negate();
    }
    
    // Función que combina funciones
    public static <T> Predicate<T> y(Predicate<T> p1, Predicate<T> p2) {
        return p1.and(p2);
    }
    
    public static <T> Predicate<T> o(Predicate<T> p1, Predicate<T> p2) {
        return p1.or(p2);
    }
    
    // Función de orden superior para retry
    public static <T> T retry(Supplier<T> operacion, int maxIntentos) {
        Exception ultimaExcepcion = null;
        
        for (int i = 0; i < maxIntentos; i++) {
            try {
                return operacion.get();
            } catch (Exception e) {
                ultimaExcepcion = e;
                System.out.println("Intento " + (i + 1) + " falló: " + e.getMessage());
            }
        }
        
        throw new RuntimeException("Operación falló después de " + maxIntentos + " intentos", 
                                 ultimaExcepcion);
    }
    
    // Función de orden superior para memoización
    public static <T, R> Function<T, R> memoizar(Function<T, R> funcion) {
        Map<T, R> cache = new ConcurrentHashMap<>();
        return input -> cache.computeIfAbsent(input, funcion);
    }
    
    // Función de orden superior para throttling
    public static Consumer<String> throttle(Consumer<String> accion, long intervalMs) {
        AtomicLong ultimaEjecucion = new AtomicLong(0);
        
        return mensaje -> {
            long ahora = System.currentTimeMillis();
            if (ahora - ultimaEjecucion.get() >= intervalMs) {
                ultimaEjecucion.set(ahora);
                accion.accept(mensaje);
            }
        };
    }
    
    public static void ejemplos() {
        // Mapear con diferentes funciones
        List<String> palabras = Arrays.asList("java", "python", "javascript");
        
        List<String> mayusculas = mapear(palabras, String::toUpperCase);
        List<Integer> longitudes = mapear(palabras, String::length);
        
        // Combinación de predicados
        Predicate<Integer> esPar = n -> n % 2 == 0;
        Predicate<Integer> esPositivo = n -> n > 0;
        Predicate<Integer> esParYPositivo = y(esPar, esPositivo);
        
        // Retry
        Supplier<String> operacionRiesgosa = () -> {
            if (Math.random() < 0.7) {
                throw new RuntimeException("Falló aleatoriamente");
            }
            return "Éxito";
        };
        
        String resultado = retry(operacionRiesgosa, 5);
        
        // Memoización
        Function<Integer, Integer> fibonacci = memoizar(HigherOrderFunctions::fibonacciLento);
        System.out.println(fibonacci.apply(40)); // Mucho más rápido la segunda vez
        
        // Throttling
        Consumer<String> log = System.out::println;
        Consumer<String> logThrottled = throttle(log, 1000); // Máximo una vez por segundo
        
        // Solo el primer mensaje se imprimirá inmediatamente
        logThrottled.accept("Mensaje 1");
        logThrottled.accept("Mensaje 2"); // Ignorado
        logThrottled.accept("Mensaje 3"); // Ignorado
    }
    
    private static Integer fibonacciLento(Integer n) {
        if (n <= 1) return n;
        return fibonacciLento(n - 1) + fibonacciLento(n - 2);
    }
}
```

## Stream API

### Operaciones Intermedias y Terminales
```java
public class StreamExamples {
    
    public static void operacionesBasicas() {
        List<String> palabras = Arrays.asList("java", "python", "javascript", "go", "rust");
        
        // Operaciones intermedias (lazy)
        Stream<String> stream = palabras.stream()
            .filter(palabra -> palabra.length() > 3)    // Filtrar
            .map(String::toUpperCase)                   // Transformar
            .sorted()                                   // Ordenar
            .distinct();                                // Eliminar duplicados
        
        // Operación terminal (ejecuta toda la cadena)
        List<String> resultado = stream.collect(Collectors.toList());
        
        // Todo en una línea
        List<String> lenguajes = palabras.stream()
            .filter(p -> p.length() > 2)
            .map(String::toUpperCase)
            .sorted()
            .collect(Collectors.toList());
    }
    
    public static void operacionesAvanzadas() {
        List<Persona> personas = Arrays.asList(
            new Persona("Ana", 25, Arrays.asList("Leer", "Correr")),
            new Persona("Luis", 30, Arrays.asList("Correr", "Nadar")),
            new Persona("María", 28, Arrays.asList("Pintar", "Leer")),
            new Persona("Carlos", 35, Arrays.asList("Nadar", "Leer"))
        );
        
        // Grouping
        Map<Integer, List<Persona>> porEdad = personas.stream()
            .collect(Collectors.groupingBy(Persona::getEdad));
        
        // Partitioning
        Map<Boolean, List<Persona>> porEdadMayor30 = personas.stream()
            .collect(Collectors.partitioningBy(p -> p.getEdad() > 30));
        
        // FlatMap - aplanar listas
        List<String> todosHobbies = personas.stream()
            .flatMap(p -> p.getHobbies().stream())
            .distinct()
            .collect(Collectors.toList());
        
        // Reduce - operaciones de agregación
        Optional<Persona> personaMayor = personas.stream()
            .reduce((p1, p2) -> p1.getEdad() > p2.getEdad() ? p1 : p2);
        
        int sumaEdades = personas.stream()
            .mapToInt(Persona::getEdad)
            .reduce(0, Integer::sum);
        
        // Collectors personalizados
        String nombresUnidos = personas.stream()
            .map(Persona::getNombre)
            .collect(Collectors.joining(", ", "[", "]"));
        
        // Estadísticas
        IntSummaryStatistics estadisticas = personas.stream()
            .mapToInt(Persona::getEdad)
            .summaryStatistics();
        
        System.out.println("Promedio edad: " + estadisticas.getAverage());
        System.out.println("Edad máxima: " + estadisticas.getMax());
    }
    
    public static void streamsParalelos() {
        List<Integer> numeros = IntStream.rangeClosed(1, 1000000)
                                       .boxed()
                                       .collect(Collectors.toList());
        
        // Stream secuencial
        long inicio = System.currentTimeMillis();
        long suma1 = numeros.stream()
                           .mapToLong(Integer::longValue)
                           .sum();
        long tiempoSecuencial = System.currentTimeMillis() - inicio;
        
        // Stream paralelo
        inicio = System.currentTimeMillis();
        long suma2 = numeros.parallelStream()
                           .mapToLong(Integer::longValue)
                           .sum();
        long tiempoParalelo = System.currentTimeMillis() - inicio;
        
        System.out.println("Secuencial: " + tiempoSecuencial + "ms");
        System.out.println("Paralelo: " + tiempoParalelo + "ms");
    }
}
```

### Collectors Personalizados
```java
public class CollectorsPersonalizados {
    
    // Collector para crear un mapa de frecuencias
    public static <T> Collector<T, ?, Map<T, Long>> contarFrecuencias() {
        return Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        );
    }
    
    // Collector para concatenar con separador personalizado
    public static Collector<String, ?, String> unirCon(String separador) {
        return Collector.of(
            StringBuilder::new,                    // supplier
            (sb, s) -> sb.append(s).append(separador), // accumulator
            (sb1, sb2) -> sb1.append(sb2),        // combiner
            sb -> sb.length() > 0 ? 
                sb.substring(0, sb.length() - separador.length()) : "" // finisher
        );
    }
    
    // Collector para estadísticas de strings
    public static Collector<String, ?, Map<String, Object>> estadisticasString() {
        return Collector.of(
            () -> new HashMap<String, Object>() {{
                put("count", 0);
                put("totalLength", 0);
                put("minLength", Integer.MAX_VALUE);
                put("maxLength", 0);
            }},
            (map, str) -> {
                map.put("count", (Integer) map.get("count") + 1);
                map.put("totalLength", (Integer) map.get("totalLength") + str.length());
                map.put("minLength", Math.min((Integer) map.get("minLength"), str.length()));
                map.put("maxLength", Math.max((Integer) map.get("maxLength"), str.length()));
            },
            (map1, map2) -> {
                map1.put("count", (Integer) map1.get("count") + (Integer) map2.get("count"));
                map1.put("totalLength", (Integer) map1.get("totalLength") + (Integer) map2.get("totalLength"));
                