# Iterators en Java - Guía Completa

## Tabla de Contenidos
1. [Introducción a los Iterators](#introducción-a-los-iterators)
2. [Tipos de Iterators](#tipos-de-iterators)
3. [Iterator Interface](#iterator-interface)
4. [ListIterator Interface](#listiterator-interface)
5. [Enumeration Interface](#enumeration-interface)
6. [Enhanced For Loop (For-Each)](#enhanced-for-loop-for-each)
7. [Stream API y Iteración](#stream-api-y-iteración)
8. [Comparación de Métodos de Iteración](#comparación-de-métodos-de-iteración)
9. [Implementación Personalizada](#implementación-personalizada)


## Introducción a los Iterators

Los **Iterators** en Java son objetos que permiten recorrer elementos de una colección de manera secuencial sin exponer la representación interna de la estructura de datos. Proporcionan una interfaz uniforme para acceder a los elementos independientemente del tipo de colección.

### Ventajas de los Iterators:
- **Abstracción**: Interfaz uniforme para diferentes tipos de colecciones
- **Seguridad**: Permiten modificación segura durante la iteración
- **Flexibilidad**: Diferentes tipos de iteración (unidireccional, bidireccional)
- **Lazy Loading**: Los elementos se procesan bajo demanda
- **Fail-Fast**: Detección temprana de modificaciones concurrentes

### Jerarquía de Interfaces:
```
Iterable<T>
└── Collection<T>
    ├── List<T>
    ├── Set<T>
    └── Queue<T>

Iterator<T>
└── ListIterator<T>

Enumeration<T> (Legacy)
```

## Tipos de Iterators

### 1. Iterator (Unidireccional)
- Solo permite avanzar hacia adelante
- Permite eliminación segura de elementos
- Disponible para todas las colecciones

### 2. ListIterator (Bidireccional)
- Permite avanzar y retroceder
- Permite agregar, eliminar y modificar elementos
- Solo disponible para listas

### 3. Enumeration (Legacy)
- Versión antigua de Iterator
- Solo lectura, no permite modificaciones
- Usado principalmente en clases legacy como Vector y Hashtable

## Iterator Interface

### Métodos Principales

```java
public interface Iterator<E> {
    boolean hasNext();      // ¿Hay más elementos?
    E next();              // Obtener siguiente elemento
    void remove();         // Eliminar elemento actual (opcional)
    
    // Métodos default (Java 8+)
    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

### Ejemplo Básico de Iterator

```java
import java.util.*;

public class BasicIteratorExample {
    public static void main(String[] args) {
        List<String> frutas = Arrays.asList("Manzana", "Banana", "Cereza", "Durazno");
        
        // Obtener iterator
        Iterator<String> iterator = frutas.iterator();
        
        // Iterar usando while
        System.out.println("=== Iteración con while ===");
        while (iterator.hasNext()) {
            String fruta = iterator.next();
            System.out.println("Fruta: " + fruta);
        }
        
        // Nuevo iterator para otra iteración
        iterator = frutas.iterator();
        
        // Usar forEachRemaining (Java 8+)
        System.out.println("\n=== Usando forEachRemaining ===");
        iterator.forEachRemaining(fruta -> 
            System.out.println("Procesando: " + fruta.toUpperCase())
        );
    }
}
```

### Eliminación Segura con Iterator

```java
import java.util.*;

public class SafeRemovalExample {
    public static void main(String[] args) {
        List<Integer> numeros = new ArrayList<>(
            Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        );
        
        System.out.println("Lista original: " + numeros);
        
        // ❌ INCORRECTO: ConcurrentModificationException
        // for (Integer num : numeros) {
        //     if (num % 2 == 0) {
        //         numeros.remove(num); // Lanza excepción
        //     }
        // }
        
        // ✅ CORRECTO: Usar Iterator para eliminación segura
        Iterator<Integer> iterator = numeros.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if (num % 2 == 0) {
                iterator.remove(); // Eliminación segura
                System.out.println("Eliminado: " + num);
            }
        }
        
        System.out.println("Lista después de eliminar pares: " + numeros);
    }
}
```

### Iterator con Diferentes Colecciones

```java
import java.util.*;

public class IteratorWithCollections {
    public static void main(String[] args) {
        // ArrayList
        System.out.println("=== ArrayList ===");
        List<String> arrayList = new ArrayList<>(Arrays.asList("A", "B", "C"));
        iterateAndPrint(arrayList);
        
        // LinkedList
        System.out.println("\n=== LinkedList ===");
        List<String> linkedList = new LinkedList<>(Arrays.asList("X", "Y", "Z"));
        iterateAndPrint(linkedList);
        
        // HashSet
        System.out.println("\n=== HashSet ===");
        Set<String> hashSet = new HashSet<>(Arrays.asList("1", "2", "3"));
        iterateAndPrint(hashSet);
        
        // TreeSet
        System.out.println("\n=== TreeSet ===");
        Set<String> treeSet = new TreeSet<>(Arrays.asList("C", "A", "B"));
        iterateAndPrint(treeSet);
        
        // PriorityQueue
        System.out.println("\n=== PriorityQueue ===");
        Queue<Integer> priorityQueue = new PriorityQueue<>(Arrays.asList(5, 1, 3, 2, 4));
        iterateAndPrint(priorityQueue);
    }
    
    private static <T> void iterateAndPrint(Iterable<T> collection) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }
}
```

## ListIterator Interface

### Métodos de ListIterator

```java
public interface ListIterator<E> extends Iterator<E> {
    // Métodos heredados de Iterator
    boolean hasNext();
    E next();
    void remove();
    
    // Métodos adicionales de ListIterator
    boolean hasPrevious();      // ¿Hay elemento anterior?
    E previous();              // Obtener elemento anterior
    int nextIndex();           // Índice del siguiente elemento
    int previousIndex();       // Índice del elemento anterior
    void set(E e);            // Reemplazar elemento actual
    void add(E e);            // Agregar elemento en posición actual
}
```

### Ejemplo Completo de ListIterator

```java
import java.util.*;

public class ListIteratorExample {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>(
            Arrays.asList("Uno", "Dos", "Tres", "Cuatro", "Cinco")
        );
        
        System.out.println("Lista original: " + lista);
        
        // Obtener ListIterator
        ListIterator<String> listIterator = lista.listIterator();
        
        // === NAVEGACIÓN HACIA ADELANTE ===
        System.out.println("\n=== Navegación hacia adelante ===");
        while (listIterator.hasNext()) {
            int index = listIterator.nextIndex();
            String elemento = listIterator.next();
            System.out.println("Índice " + index + ": " + elemento);
        }
        
        // === NAVEGACIÓN HACIA ATRÁS ===
        System.out.println("\n=== Navegación hacia atrás ===");
        while (listIterator.hasPrevious()) {
            int index = listIterator.previousIndex();
            String elemento = listIterator.previous();
            System.out.println("Índice " + index + ": " + elemento);
        }
        
        // === MODIFICACIONES DURANTE ITERACIÓN ===
        System.out.println("\n=== Modificaciones durante iteración ===");
        
        // Reiniciar iterator al inicio
        listIterator = lista.listIterator();
        
        while (listIterator.hasNext()) {
            String elemento = listIterator.next();
            
            // Reemplazar elementos que contienen "o"
            if (elemento.toLowerCase().contains("o")) {
                listIterator.set(elemento.toUpperCase());
                System.out.println("Reemplazado: " + elemento + " -> " + elemento.toUpperCase());
            }
            
            // Agregar elemento después de "DOS"
            if (elemento.equalsIgnoreCase("DOS")) {
                listIterator.add("DOS Y MEDIO");
                System.out.println("Agregado: DOS Y MEDIO después de " + elemento);
            }
        }
        
        System.out.println("Lista después de modificaciones: " + lista);
        
        // === ELIMINACIÓN SELECTIVA ===
        System.out.println("\n=== Eliminación selectiva ===");
        listIterator = lista.listIterator();
        
        while (listIterator.hasNext()) {
            String elemento = listIterator.next();
            if (elemento.contains("MEDIO")) {
                listIterator.remove();
                System.out.println("Eliminado: " + elemento);
            }
        }
        
        System.out.println("Lista final: " + lista);
    }
}
```

### ListIterator con Posición Específica

```java
import java.util.*;

public class ListIteratorPositionExample {
    public static void main(String[] args) {
        List<Integer> numeros = new ArrayList<>(
            Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
        );
        
        System.out.println("Lista: " + numeros);
        
        // Crear ListIterator comenzando en posición específica
        int posicionInicio = 3;
        ListIterator<Integer> listIterator = numeros.listIterator(posicionInicio);
        
        System.out.println("\nIterando desde posición " + posicionInicio + ":");
        
        // Mostrar elementos hacia adelante desde la posición inicial
        System.out.println("Hacia adelante:");
        for (int i = 0; i < 4 && listIterator.hasNext(); i++) {
            System.out.println("  Posición " + listIterator.nextIndex() + 
                             ": " + listIterator.next());
        }
        
        // Mostrar elementos hacia atrás
        System.out.println("\nHacia atrás:");
        while (listIterator.hasPrevious()) {
            System.out.println("  Posición " + listIterator.previousIndex() + 
                             ": " + listIterator.previous());
        }
        
        // Insertar elementos en posiciones específicas
        System.out.println("\nInsertando elementos:");
        listIterator = numeros.listIterator(2); // Posición 2
        listIterator.add(25); // Insertar 25 en posición 2
        
        listIterator = numeros.listIterator(6); // Nueva posición después de inserción
        listIterator.add(55); // Insertar 55
        
        System.out.println("Lista después de inserciones: " + numeros);
    }
}
```

## Enumeration Interface

### Uso de Enumeration (Legacy)

```java
import java.util.*;

public class EnumerationExample {
    public static void main(String[] args) {
        // Vector es una clase legacy que usa Enumeration
        Vector<String> vector = new Vector<>(
            Arrays.asList("Alpha", "Beta", "Gamma", "Delta")
        );
        
        // Obtener Enumeration
        Enumeration<String> enumeration = vector.elements();
        
        System.out.println("=== Usando Enumeration ===");
        while (enumeration.hasMoreElements()) {
            String elemento = enumeration.nextElement();
            System.out.println("Elemento: " + elemento);
        }
        
        // Hashtable también usa Enumeration
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Uno", 1);
        hashtable.put("Dos", 2);
        hashtable.put("Tres", 3);
        
        System.out.println("\n=== Enumeration de claves ===");
        Enumeration<String> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            System.out.println(key + " = " + hashtable.get(key));
        }
        
        System.out.println("\n=== Enumeration de valores ===");
        Enumeration<Integer> values = hashtable.elements();
        while (values.hasMoreElements()) {
            System.out.println("Valor: " + values.nextElement());
        }
    }
}
```

### Convertir Enumeration a Iterator

```java
import java.util.*;

public class EnumerationToIterator {
    public static void main(String[] args) {
        Vector<String> vector = new Vector<>(
            Arrays.asList("Item1", "Item2", "Item3", "Item4")
        );
        
        // Usar Enumeration tradicional
        Enumeration<String> enumeration = vector.elements();
        
        // Convertir Enumeration a Iterator usando Collections
        Iterator<String> iterator = Collections.list(enumeration).iterator();
        
        System.out.println("Usando Iterator convertido:");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        
        // Alternativa: usar iterator() directamente del Vector
        System.out.println("\nUsando iterator() directamente:");
        Iterator<String> directIterator = vector.iterator();
        while (directIterator.hasNext()) {
            System.out.println(directIterator.next());
        }
    }
}
```

## Enhanced For Loop (For-Each)

### Sintaxis y Uso

```java
import java.util.*;

public class EnhancedForLoopExample {
    public static void main(String[] args) {
        List<String> lenguajes = Arrays.asList("Java", "Python", "JavaScript", "C++");
        
        // Enhanced for loop (internamente usa Iterator)
        System.out.println("=== Enhanced For Loop ===");
        for (String lenguaje : lenguajes) {
            System.out.println("Lenguaje: " + lenguaje);
        }
        
        // Equivalente con Iterator tradicional
        System.out.println("\n=== Iterator Tradicional Equivalente ===");
        Iterator<String> iterator = lenguajes.iterator();
        while (iterator.hasNext()) {
            String lenguaje = iterator.next();
            System.out.println("Lenguaje: " + lenguaje);
        }
        
        // Con arrays también funciona
        System.out.println("\n=== Con Arrays ===");
        int[] numeros = {1, 2, 3, 4, 5};
        for (int numero : numeros) {
            System.out.println("Número: " + numero);
        }
        
        // Con Map (iterando sobre entrySet)
        System.out.println("\n=== Con Map ===");
        Map<String, Integer> edades = new HashMap<>();
        edades.put("Ana", 25);
        edades.put("Carlos", 30);
        edades.put("María", 28);
        
        for (Map.Entry<String, Integer> entrada : edades.entrySet()) {
            System.out.println(entrada.getKey() + " tiene " + entrada.getValue() + " años");
        }
        
        // Solo las claves
        System.out.println("\n=== Solo claves del Map ===");
        for (String nombre : edades.keySet()) {
            System.out.println("Nombre: " + nombre);
        }
        
        // Solo los valores
        System.out.println("\n=== Solo valores del Map ===");
        for (Integer edad : edades.values()) {
            System.out.println("Edad: " + edad);
        }
    }
}
```

### Limitaciones del Enhanced For Loop

```java
import java.util.*;

public class EnhancedForLimitations {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        
        System.out.println("Lista original: " + lista);
        
        // ❌ NO se puede modificar la colección durante enhanced for
        try {
            for (String elemento : lista) {
                if (elemento.equals("C")) {
                    lista.remove(elemento); // ConcurrentModificationException
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Error: " + e.getClass().getSimpleName());
        }
        
        // ✅ Usar Iterator para modificaciones
        System.out.println("\nEliminando 'C' con Iterator:");
        Iterator<String> iterator = lista.iterator();
        while (iterator.hasNext()) {
            String elemento = iterator.next();
            if (elemento.equals("C")) {
                iterator.remove();
                System.out.println("Eliminado: " + elemento);
            }
        }
        
        System.out.println("Lista después: " + lista);
        
        // ❌ NO se puede obtener el índice en enhanced for
        System.out.println("\n=== Enhanced for sin índice ===");
        for (String elemento : lista) {
            System.out.println("Elemento: " + elemento); // No sabemos el índice
        }
        
        // ✅ Usar iterator tradicional o for clásico para índices
        System.out.println("\n=== For clásico con índice ===");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("Índice " + i + ": " + lista.get(i));
        }
    }
}
```

## Stream API y Iteración

### Iteración Funcional con Streams

```java
import java.util.*;
import java.util.stream.*;

public class StreamIterationExample {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("Lista original: " + numeros);
        
        // forEach - iteración con acción
        System.out.println("\n=== forEach ===");
        numeros.stream()
               .forEach(n -> System.out.print(n + " "));
        
        // filter + forEach - iteración condicional
        System.out.println("\n\n=== filter + forEach ===");
        numeros.stream()
               .filter(n -> n % 2 == 0)
               .forEach(n -> System.out.print(n + " "));
        
        // map + forEach - transformación e iteración
        System.out.println("\n\n=== map + forEach ===");
        numeros.stream()
               .map(n -> n * n)
               .forEach(n -> System.out.print(n + " "));
        
        // collect - iteración con acumulación
        System.out.println("\n\n=== collect ===");
        List<Integer> pares = numeros.stream()
                                   .filter(n -> n % 2 == 0)
                                   .collect(Collectors.toList());
        System.out.println("Números pares: " + pares);
        
        // reduce - iteración con reducción
        System.out.println("\n=== reduce ===");
        int suma = numeros.stream()
                         .reduce(0, Integer::sum);
        System.out.println("Suma total: " + suma);
        
        // Iteración con índice usando IntStream
        System.out.println("\n=== Iteración con índice ===");
        IntStream.range(0, numeros.size())
                .forEach(i -> System.out.println("Índice " + i + ": " + numeros.get(i)));
    }
}
```

### Comparación: Iterator vs Stream vs Enhanced For

```java
import java.util.*;
import java.util.stream.*;

public class IterationComparison {
    public static void main(String[] args) {
        List<String> palabras = Arrays.asList("java", "python", "javascript", "go", "rust");
        
        // Tarea: Filtrar palabras con más de 4 caracteres y convertir a mayúsculas
        
        // 1. Con Iterator tradicional
        System.out.println("=== Iterator Tradicional ===");
        List<String> resultado1 = new ArrayList<>();
        Iterator<String> iterator = palabras.iterator();
        while (iterator.hasNext()) {
            String palabra = iterator.next();
            if (palabra.length() > 4) {
                resultado1.add(palabra.toUpperCase());
            }
        }
        System.out.println(resultado1);
        
        // 2. Con Enhanced For Loop
        System.out.println("\n=== Enhanced For Loop ===");
        List<String> resultado2 = new ArrayList<>();
        for (String palabra : palabras) {
            if (palabra.length() > 4) {
                resultado2.add(palabra.toUpperCase());
            }
        }
        System.out.println(resultado2);
        
        // 3. Con Stream API
        System.out.println("\n=== Stream API ===");
        List<String> resultado3 = palabras.stream()
                                         .filter(p -> p.length() > 4)
                                         .map(String::toUpperCase)
                                         .collect(Collectors.toList());
        System.out.println(resultado3);
        
        // 4. Medición de rendimiento (ejemplo básico)
        System.out.println("\n=== Comparación de Rendimiento ===");
        List<Integer> numerosBig = IntStream.range(1, 1000000)
                                          .boxed()
                                          .collect(Collectors.toList());
        
        // Iterator
        long inicio = System.nanoTime();
        long suma1 = 0;
        Iterator<Integer> iter = numerosBig.iterator();
        while (iter.hasNext()) {
            suma1 += iter.next();
        }
        long tiempoIterator = System.nanoTime() - inicio;
        
        // Enhanced For
        inicio = System.nanoTime();
        long suma2 = 0;
        for (Integer num : numerosBig) {
            suma2 += num;
        }
        long tiempoEnhancedFor = System.nanoTime() - inicio;
        
        // Stream
        inicio = System.nanoTime();
        long suma3 = numerosBig.stream()
                              .mapToLong(Integer::longValue)
                              .sum();
        long tiempoStream = System.nanoTime() - inicio;
        
        System.out.println("Iterator: " + tiempoIterator / 1_000_000 + " ms");
        System.out.println("Enhanced For: " + tiempoEnhancedFor / 1_000_000 + " ms");
        System.out.println("Stream: " + tiempoStream / 1_000_000 + " ms");
        System.out.println("Todas las sumas son iguales: " + 
                          (suma1 == suma2 && suma2 == suma3));
    }
}
```

## Comparación de Métodos de Iteración

### Tabla Comparativa

| Método | Dirección | Modificación | Índice | Rendimiento | Legibilidad |
|--------|-----------|--------------|---------|-------------|-------------|
| Iterator | → | ✅ (remove) | ❌ | Alto | Media |
| ListIterator | ↔ | ✅ (add/remove/set) | ✅ | Alto | Media |
| Enhanced For | → | ❌ | ❌ | Alto | Alta |
| Stream API | → | ❌ (inmutable) | Con IntStream | Variable | Alta |
| For tradicional | → | ✅ | ✅ | Muy Alto | Media |
| Enumeration | → | ❌ | ❌ | Alto | Baja |

### Cuándo Usar Cada Método

```java
import java.util.*;

public class WhenToUseWhat {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        
        // 1. Solo lectura sin modificación -> Enhanced For Loop
        System.out.println("=== Solo lectura -> Enhanced For ===");
        for (String elemento : lista) {
            System.out.print(elemento + " ");
        }
        
        // 2. Necesitas eliminar elementos -> Iterator
        System.out.println("\n\n=== Eliminación -> Iterator ===");
        List<String> copia1 = new ArrayList<>(lista);
        Iterator<String> iter = copia1.iterator();
        while (iter.hasNext()) {
            String elemento = iter.next();
            if (elemento.equals("C")) {
                iter.remove();
            }
        }
        System.out.println("Después de eliminar 'C': " + copia1);
        
        // 3. Navegación bidireccional -> ListIterator
        System.out.println("\n=== Navegación bidireccional -> ListIterator ===");
        ListIterator<String> listIter = lista.listIterator(lista.size());
        System.out.print("Hacia atrás: ");
        while (listIter.hasPrevious()) {
            System.out.print(listIter.previous() + " ");
        }
        
        // 4. Transformaciones funcionales -> Stream
        System.out.println("\n\n=== Transformaciones -> Stream ===");
        String resultado = lista.stream()
                               .filter(s -> !s.equals("C"))
                               .map(String::toLowerCase)
                               .collect(Collectors.joining("-"));
        System.out.println("Resultado: " + resultado);
        
        // 5. Necesitas índice -> For tradicional
        System.out.println("\n=== Necesitas índice -> For tradicional ===");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("Índice " + i + ": " + lista.get(i));
        }
        
        // 6. Máximo rendimiento con acceso simple -> For tradicional
        System.out.println("\n=== Máximo rendimiento -> For tradicional ===");
        long suma = 0;
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
        for (int i = 0; i < numeros.size(); i++) {
            suma += numeros.get(i);
        }
        System.out.println("Suma: " + suma);
    }
}
```

## Implementación Personalizada

### Iterator Personalizado

```java
import java.util.*;

// Clase que implementa Iterable para poder usar enhanced for loop
class MiColeccion<T> implements Iterable<T> {
    private List<T> elementos;
    
    public MiColeccion() {
        this.elementos = new ArrayList<>();
    }
    
    public void agregar(T elemento) {
        elementos.add(elemento);
    }
    
    public int tamaño() {
        return elementos.size();
    }
    
    // Implementar el método iterator() de Iterable
    @Override
    public Iterator<T> iterator() {
        return new MiIterator();
    }
    
    // Iterator personalizado como clase interna
    private class MiIterator implements Iterator<T> {
        private int indiceActual = 0;
        private boolean puedeEliminar = false;
        
        @Override
        public boolean hasNext() {
            return indiceActual < elementos.size();
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos");
            }
            
            puedeEliminar = true;
            return elementos.get(indiceActual++);
        }
        
        @Override
        public void remove() {
            if (!puedeEliminar) {
                throw new IllegalStateException("remove() solo puede llamarse después de next()");
            }
            
            elementos.remove(--indiceActual);
            puedeEliminar = false;
        }
    }
    
    // Iterator personalizado que salta elementos nulos
    public Iterator<T> iteratorSinNulos() {
        return new Iterator<T>() {
            private int indice = 0;
            private T siguienteElemento = null;
            private boolean siguienteEncontrado = false;
            
            @Override
            public boolean hasNext() {
                if (siguienteEncontrado) {
                    return true;
                }
                
                while (indice < elementos.size()) {
                    T elemento = elementos.get(indice++);
                    if (elemento != null) {
                        siguienteElemento = elemento;
                        siguienteEncontrado = true;
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                
                siguienteEncontrado = false;
                return siguienteElemento;
            }
        };
    }
}

