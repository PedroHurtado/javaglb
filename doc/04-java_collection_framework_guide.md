# Collection Framework de Java - Guía Completa

## Tabla de Contenidos
1. [Introducción](#introducción)
2. [Jerarquía del Collection Framework](#jerarquía-del-collection-framework)
3. [Interfaces Principales](#interfaces-principales)
4. [Implementaciones Concretas](#implementaciones-concretas)
5. [Ejemplos Prácticos](#ejemplos-prácticos)
6. [Algoritmos y Utilidades](#algoritmos-y-utilidades)
7. [Comparadores](#comparadores)
8. [Iteradores](#iteradores)
9. [Best Practices](#best-practices)
10. [Casos de Uso Comunes](#casos-de-uso-comunes)

## Introducción

El Collection Framework de Java es una arquitectura unificada para representar y manipular colecciones de objetos. Proporciona interfaces, implementaciones y algoritmos para trabajar eficientemente con grupos de datos.

### Ventajas del Collection Framework
- **Consistencia**: API uniforme para diferentes tipos de colecciones
- **Reducción de esfuerzo**: Implementaciones optimizadas y probadas
- **Interoperabilidad**: Las colecciones pueden intercambiarse fácilmente
- **Algoritmos eficientes**: Implementaciones optimizadas para búsqueda, ordenamiento, etc.

## Jerarquía del Collection Framework

```
Collection (Interface)
├── List (Interface)
│   ├── ArrayList (Class)
│   ├── LinkedList (Class)
│   ├── Vector (Class)
│   └── Stack (Class)
├── Set (Interface)
│   ├── HashSet (Class)
│   ├── LinkedHashSet (Class)
│   └── SortedSet (Interface)
│       └── TreeSet (Class)
└── Queue (Interface)
    ├── PriorityQueue (Class)
    ├── ArrayDeque (Class)
    └── Deque (Interface)
        └── LinkedList (Class)

Map (Interface) - No extiende Collection
├── HashMap (Class)
├── LinkedHashMap (Class)
├── Hashtable (Class)
└── SortedMap (Interface)
    └── TreeMap (Class)
```

## Interfaces Principales

### Collection Interface
Interface raíz que define operaciones básicas para todas las colecciones.

```java
public interface Collection<E> extends Iterable<E> {
    // Operaciones básicas
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    Iterator<E> iterator();
    Object[] toArray();
    
    // Operaciones de modificación
    boolean add(E e);
    boolean remove(Object o);
    boolean addAll(Collection<? extends E> c);
    boolean removeAll(Collection<?> c);
    void clear();
}
```

### List Interface
Colección ordenada que permite elementos duplicados y acceso por índice.

```java
public interface List<E> extends Collection<E> {
    // Acceso posicional
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);
    
    // Búsqueda
    int indexOf(Object o);
    int lastIndexOf(Object o);
    
    // Sublistas
    List<E> subList(int fromIndex, int toIndex);
}
```

### Set Interface
Colección que no permite elementos duplicados.

```java
public interface Set<E> extends Collection<E> {
    // Hereda todos los métodos de Collection
    // Pero con semántica diferente: no duplicados
}
```

### Map Interface
Mapea claves únicas a valores.

```java
public interface Map<K,V> {
    // Operaciones básicas
    V put(K key, V value);
    V get(Object key);
    V remove(Object key);
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    
    // Vistas de la colección
    Set<K> keySet();
    Collection<V> values();
    Set<Map.Entry<K,V>> entrySet();
}
```

## Implementaciones Concretas

### ArrayList
Lista redimensionable basada en array dinámico.

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListExample {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>();
        
        // Agregar elementos
        lista.add("Java");
        lista.add("Python");
        lista.add("JavaScript");
        
        // Acceso por índice
        System.out.println("Elemento en índice 1: " + lista.get(1));
        
        // Modificar elemento
        lista.set(2, "C++");
        
        // Iterar
        for (String lenguaje : lista) {
            System.out.println(lenguaje);
        }
        
        // Características:
        // - Acceso O(1)
        // - Inserción/eliminación O(n) en el peor caso
        // - Buena para acceso frecuente por índice
    }
}
```

### LinkedList
Lista doblemente enlazada que implementa List y Deque.

```java
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<Integer> lista = new LinkedList<>();
        
        // Como Lista
        lista.add(1);
        lista.add(2);
        lista.add(3);
        
        // Como Deque (cola doble)
        lista.addFirst(0);
        lista.addLast(4);
        
        System.out.println("Primer elemento: " + lista.getFirst());
        System.out.println("Último elemento: " + lista.getLast());
        
        // Características:
        // - Inserción/eliminación O(1) en extremos
        // - Acceso O(n)
        // - Buena para inserciones/eliminaciones frecuentes
    }
}
```

### HashSet
Conjunto basado en tabla hash.

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetExample {
    public static void main(String[] args) {
        Set<String> conjunto = new HashSet<>();
        
        // Agregar elementos
        conjunto.add("Apple");
        conjunto.add("Banana");
        conjunto.add("Cherry");
        conjunto.add("Apple"); // Duplicado - no se agrega
        
        System.out.println("Tamaño: " + conjunto.size()); // 3
        
        // Verificar existencia
        if (conjunto.contains("Banana")) {
            System.out.println("Banana está en el conjunto");
        }
        
        // Características:
        // - No permite duplicados
        // - Operaciones O(1) promedio
        // - No mantiene orden de inserción
    }
}
```

### TreeSet
Conjunto ordenado basado en árbol rojo-negro.

```java
import java.util.TreeSet;
import java.util.Set;

public class TreeSetExample {
    public static void main(String[] args) {
        Set<Integer> conjunto = new TreeSet<>();
        
        conjunto.add(5);
        conjunto.add(2);
        conjunto.add(8);
        conjunto.add(1);
        conjunto.add(9);
        
        // Los elementos se mantienen ordenados
        System.out.println(conjunto); // [1, 2, 5, 8, 9]
        
        // Características:
        // - Elementos ordenados automáticamente
        // - Operaciones O(log n)
        // - Elementos deben ser Comparable o usar Comparator
    }
}
```

### HashMap
Mapa basado en tabla hash.

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) {
        Map<String, Integer> mapa = new HashMap<>();
        
        // Agregar pares clave-valor
        mapa.put("Java", 25);
        mapa.put("Python", 30);
        mapa.put("JavaScript", 28);
        
        // Obtener valor
        System.out.println("Años de Java: " + mapa.get("Java"));
        
        // Iterar sobre las entradas
        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
        
        // Características:
        // - Operaciones O(1) promedio
        // - Permite null en clave y valores
        // - No mantiene orden
    }
}
```

### TreeMap
Mapa ordenado basado en árbol rojo-negro.

```java
import java.util.TreeMap;
import java.util.Map;

public class TreeMapExample {
    public static void main(String[] args) {
        Map<String, Double> calificaciones = new TreeMap<>();
        
        calificaciones.put("Carlos", 8.5);
        calificaciones.put("Ana", 9.2);
        calificaciones.put("Bruno", 7.8);
        
        // Las claves se mantienen ordenadas alfabéticamente
        for (Map.Entry<String, Double> entrada : calificaciones.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
        // Salida: Ana: 9.2, Bruno: 7.8, Carlos: 8.5
        
        // Características:
        // - Claves ordenadas automáticamente
        // - Operaciones O(log n)
        // - No permite null en claves
    }
}
```

## Ejemplos Prácticos

### Ejemplo 1: Sistema de Gestión de Estudiantes

```java
import java.util.*;

class Estudiante {
    private String nombre;
    private int edad;
    private double promedio;
    
    public Estudiante(String nombre, int edad, double promedio) {
        this.nombre = nombre;
        this.edad = edad;
        this.promedio = promedio;
    }
    
    // Getters y toString()
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public double getPromedio() { return promedio; }
    
    @Override
    public String toString() {
        return String.format("%s (%d años, promedio: %.1f)", 
                           nombre, edad, promedio);
    }
}

public class GestionEstudiantes {
    public static void main(String[] args) {
        // Lista de estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante("Ana", 20, 8.5));
        estudiantes.add(new Estudiante("Carlos", 22, 7.2));
        estudiantes.add(new Estudiante("María", 21, 9.1));
        
        // Mapa para búsqueda rápida por nombre
        Map<String, Estudiante> busquedaPorNombre = new HashMap<>();
        for (Estudiante e : estudiantes) {
            busquedaPorNombre.put(e.getNombre(), e);
        }
        
        // Conjunto de promedios únicos
        Set<Double> promediosUnicos = new HashSet<>();
        for (Estudiante e : estudiantes) {
            promediosUnicos.add(e.getPromedio());
        }
        
        System.out.println("Estudiantes: " + estudiantes);
        System.out.println("Promedios únicos: " + promediosUnicos);
        System.out.println("Buscar María: " + busquedaPorNombre.get("María"));
    }
}
```

### Ejemplo 2: Análisis de Frecuencia de Palabras

```java
import java.util.*;

public class FrecuenciaPalabras {
    public static void main(String[] args) {
        String texto = "java es genial java es poderoso java es versatil";
        String[] palabras = texto.split(" ");
        
        // Contar frecuencias usando Map
        Map<String, Integer> frecuencias = new HashMap<>();
        
        for (String palabra : palabras) {
            frecuencias.put(palabra, frecuencias.getOrDefault(palabra, 0) + 1);
        }
        
        // Ordenar por frecuencia (usando TreeMap con comparador personalizado)
        Map<String, Integer> frecuenciasOrdenadas = new TreeMap<>(
            (a, b) -> frecuencias.get(b).compareTo(frecuencias.get(a))
        );
        frecuenciasOrdenadas.putAll(frecuencias);
        
        System.out.println("Frecuencia de palabras:");
        for (Map.Entry<String, Integer> entrada : frecuencias.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }
}
```

## Algoritmos y Utilidades

### Collections Class
Clase utilitaria con métodos estáticos para trabajar con colecciones.

```java
import java.util.*;

public class CollectionsUtilities {
    public static void main(String[] args) {
        List<Integer> numeros = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
        
        // Ordenamiento
        Collections.sort(numeros);
        System.out.println("Ordenados: " + numeros);
        
        // Búsqueda binaria
        int indice = Collections.binarySearch(numeros, 8);
        System.out.println("Índice de 8: " + indice);
        
        // Reversar
        Collections.reverse(numeros);
        System.out.println("Reversados: " + numeros);
        
        // Barajar
        Collections.shuffle(numeros);
        System.out.println("Barajados: " + numeros);
        
        // Máximo y mínimo
        System.out.println("Máximo: " + Collections.max(numeros));
        System.out.println("Mínimo: " + Collections.min(numeros));
        
        // Llenar con un valor
        Collections.fill(numeros, 0);
        System.out.println("Llenados con 0: " + numeros);
        
        // Crear colecciones inmutables
        List<String> inmutable = Collections.unmodifiableList(
            Arrays.asList("A", "B", "C")
        );
    }
}
```

## Comparadores

### Comparator Interface

```java
import java.util.*;

class Persona {
    private String nombre;
    private int edad;
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    
    @Override
    public String toString() {
        return nombre + " (" + edad + ")";
    }
}

public class ComparatorExample {
    public static void main(String[] args) {
        List<Persona> personas = Arrays.asList(
            new Persona("Ana", 25),
            new Persona("Carlos", 30),
            new Persona("Bruno", 20)
        );
        
        System.out.println("Original: " + personas);
        
        // Ordenar por nombre
        Collections.sort(personas, Comparator.comparing(Persona::getNombre));
        System.out.println("Por nombre: " + personas);
        
        // Ordenar por edad
        Collections.sort(personas, Comparator.comparingInt(Persona::getEdad));
        System.out.println("Por edad: " + personas);
        
        // Ordenar por edad descendente
        Collections.sort(personas, 
            Comparator.comparingInt(Persona::getEdad).reversed());
        System.out.println("Por edad desc: " + personas);
        
        // Comparador compuesto: primero por edad, luego por nombre
        Collections.sort(personas, 
            Comparator.comparingInt(Persona::getEdad)
                     .thenComparing(Persona::getNombre));
        System.out.println("Edad + nombre: " + personas);
    }
}
```

## Iteradores

### Iterator e ListIterator

```java
import java.util.*;

public class IteratorsExample {
    public static void main(String[] args) {
        List<String> lista = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        
        // Iterator básico
        System.out.println("Iterator básico:");
        Iterator<String> iter = lista.iterator();
        while (iter.hasNext()) {
            String elemento = iter.next();
            if (elemento.equals("B")) {
                iter.remove(); // Eliminación segura durante iteración
            } else {
                System.out.println(elemento);
            }
        }
        
        // ListIterator (bidireccional)
        System.out.println("\nListIterator bidireccional:");
        ListIterator<String> listIter = lista.listIterator();
        
        // Hacia adelante
        while (listIter.hasNext()) {
            System.out.println("Adelante: " + listIter.next());
        }
        
        // Hacia atrás
        while (listIter.hasPrevious()) {
            System.out.println("Atrás: " + listIter.previous());
        }
        
        // Enhanced for loop (internamente usa Iterator)
        System.out.println("\nEnhanced for:");
        for (String elemento : lista) {
            System.out.println(elemento);
        }
    }
}
```

## Best Practices

### 1. Programar hacia Interfaces

```java
// ✅ Buena práctica
List<String> lista = new ArrayList<>();
Set<Integer> conjunto = new HashSet<>();
Map<String, Object> mapa = new HashMap<>();

// ❌ Evitar
ArrayList<String> lista = new ArrayList<>();
HashSet<Integer> conjunto = new HashSet<>();
HashMap<String, Object> mapa = new HashMap<>();
```

### 2. Especificar Capacidad Inicial

```java
// Si conoces el tamaño aproximado
List<String> lista = new ArrayList<>(1000);
Set<String> conjunto = new HashSet<>(500);
Map<String, String> mapa = new HashMap<>(200);
```

### 3. Uso Correcto de equals() y hashCode()

```java
class Producto {
    private String codigo;
    private String nombre;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return Objects.equals(codigo, producto.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
```

### 4. Manejo de Colecciones Nulas

```java
public void procesarLista(List<String> lista) {
    if (lista == null || lista.isEmpty()) {
        return;
    }
    
    // Procesamiento seguro
    for (String elemento : lista) {
        // ...
    }
}
```

### 5. Uso de Generics

```java
// ✅ Con generics - type safety
List<String> lista = new ArrayList<>();
lista.add("texto");
String elemento = lista.get(0); // No cast necesario

// ❌ Sin generics - propenso a errores
List lista = new ArrayList();
lista.add("texto");
String elemento = (String) lista.get(0); // Cast necesario
```

## Casos de Uso Comunes

### 1. Cuándo usar ArrayList vs LinkedList

```java
// ArrayList: cuando necesitas acceso frecuente por índice
List<String> datosParaLectura = new ArrayList<>();
String elemento = datosParaLectura.get(index); // O(1)

// LinkedList: cuando haces muchas inserciones/eliminaciones
List<String> datosParaModificacion = new LinkedList<>();
datosParaModificacion.addFirst(elemento); // O(1)
datosParaModificacion.removeLast(); // O(1)
```

### 2. Cuándo usar HashSet vs TreeSet

```java
// HashSet: cuando solo necesitas unicidad y rendimiento
Set<String> elementosUnicos = new HashSet<>(); // O(1) operaciones

// TreeSet: cuando necesitas elementos ordenados
Set<String> elementosOrdenados = new TreeSet<>(); // O(log n) operaciones
```

### 3. Cuándo usar HashMap vs TreeMap

```java
// HashMap: para acceso rápido sin orden específico
Map<String, Integer> cache = new HashMap<>(); // O(1) operaciones

// TreeMap: cuando necesitas claves ordenadas
Map<String, Integer> datosOrdenados = new TreeMap<>(); // O(log n) operaciones
```

### 4. Patrones de Uso Comunes

```java
// Patrón: Convertir entre colecciones
Set<String> conjunto = new HashSet<>(lista);
List<String> nuevaLista = new ArrayList<>(conjunto);

// Patrón: Inicialización con datos
List<String> colores = Arrays.asList("Rojo", "Verde", "Azul");
Set<Integer> numeros = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));

// Patrón: Computación condicional en Map
Map<String, Integer> contadores = new HashMap<>();
String clave = "ejemplo";
contadores.put(clave, contadores.getOrDefault(clave, 0) + 1);

// Patrón: Operaciones de conjunto
Set<String> interseccion = new HashSet<>(conjunto1);
interseccion.retainAll(conjunto2);

Set<String> union = new HashSet<>(conjunto1);
union.addAll(conjunto2);
```

## Conclusión

El Collection Framework de Java es una herramienta poderosa y versátil que proporciona estructuras de datos optimizadas para diferentes necesidades. La clave está en elegir la implementación correcta según los patrones de uso específicos de tu aplicación.

### Resumen de Selección:
- **ArrayList**: Acceso frecuente por índice, pocas modificaciones
- **LinkedList**: Muchas inserciones/eliminaciones, especialmente en extremos
- **HashSet**: Unicidad sin orden, máximo rendimiento
- **TreeSet**: Unicidad con orden automático
- **HashMap**: Mapeo clave-valor sin orden, máximo rendimiento  
- **TreeMap**: Mapeo clave-valor con claves ordenadas

### Recursos Adicionales:
- Documentación oficial de Oracle
- Java Collections Framework Tutorial
- Effective Java por Joshua Bloch
- Java: The Complete Reference

---
