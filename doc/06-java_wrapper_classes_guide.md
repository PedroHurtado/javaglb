# Type-Wrapper Classes en Java

## Introducción

Las **Type-Wrapper Classes** (Clases Envolventes) en Java son clases que encapsulan los tipos primitivos para convertirlos en objetos. Estas clases permiten trabajar con tipos primitivos como objetos cuando es necesario, proporcionando funcionalidades adicionales y compatibilidad con el paradigma orientado a objetos de Java.

## ¿Por qué existen las Wrapper Classes?

Java mantiene tipos primitivos por razones de rendimiento, pero a veces necesitamos tratarlos como objetos para:

- **Usar en colecciones**: ArrayList, HashMap, etc. solo pueden contener objetos
- **Trabajar con genéricos**: Los genéricos requieren tipos de referencia
- **Aprovechar métodos utilitarios**: Conversiones, validaciones, etc.
- **Manejar valores nulos**: Los primitivos no pueden ser null
- **Reflexión**: Para trabajar con metadatos de tipos

## Correspondencia entre Primitivos y Wrappers

| Tipo Primitivo | Clase Wrapper | Tamaño (bits) | Valor por Defecto |
|----------------|---------------|---------------|-------------------|
| `byte`         | `Byte`        | 8             | 0                 |
| `short`        | `Short`       | 16            | 0                 |
| `int`          | `Integer`     | 32            | 0                 |
| `long`         | `Long`        | 64            | 0L                |
| `float`        | `Float`       | 32            | 0.0f              |
| `double`       | `Double`      | 64            | 0.0d              |
| `char`         | `Character`   | 16            | '\u0000'          |
| `boolean`      | `Boolean`     | 1             | false             |

## Boxing y Unboxing

### Autoboxing
Conversión automática de tipo primitivo a su wrapper correspondiente:

```java
// Autoboxing ejemplos
Integer numero = 42;           // int → Integer
Double decimal = 3.14;         // double → Double
Boolean verdadero = true;      // boolean → Boolean
Character letra = 'A';         // char → Character

// En colecciones
ArrayList<Integer> numeros = new ArrayList<>();
numeros.add(10); // int se convierte automáticamente a Integer
```

### Unboxing
Conversión automática de wrapper a tipo primitivo:

```java
// Unboxing ejemplos
Integer wrapper = 100;
int primitivo = wrapper;       // Integer → int

Double wrapperDouble = 2.5;
double primitivoDouble = wrapperDouble; // Double → double

// En operaciones aritméticas
Integer a = 10;
Integer b = 20;
int suma = a + b; // Se hace unboxing automáticamente
```

### Boxing/Unboxing Manual
```java
// Boxing manual
int primitivo = 25;
Integer wrapper = Integer.valueOf(primitivo);

// Unboxing manual
Integer wrapper2 = 50;
int primitivo2 = wrapper2.intValue();
```

## Métodos Importantes de las Wrapper Classes

### Métodos de Conversión

```java
// String a primitivo
int num = Integer.parseInt("123");
double dec = Double.parseDouble("3.14");
boolean bool = Boolean.parseBoolean("true");

// String a wrapper
Integer numWrapper = Integer.valueOf("123");
Double decWrapper = Double.valueOf("3.14");

// Wrapper a String
Integer numero = 456;
String texto = numero.toString();
// o
String texto2 = Integer.toString(456);
```

### Métodos de Comparación

```java
Integer a = 100;
Integer b = 100;
Integer c = 200;

// Comparación de valores
int resultado = a.compareTo(b); // 0 (iguales)
int resultado2 = a.compareTo(c); // negativo (a < c)

// Verificar igualdad
boolean iguales = a.equals(b); // true
```

### Métodos Utilitarios

```java
// Integer
int max = Integer.max(10, 20);              // 20
int min = Integer.min(10, 20);              // 10
String binario = Integer.toBinaryString(8); // "1000"
String hex = Integer.toHexString(255);      // "ff"

// Double
boolean esInfinito = Double.isInfinite(1.0/0.0);  // true
boolean esNaN = Double.isNaN(Math.sqrt(-1));      // true

// Character
boolean esLetra = Character.isLetter('A');        // true
boolean esDigito = Character.isDigit('5');        // true
char mayuscula = Character.toUpperCase('a');      // 'A'
```

## Constantes Importantes

```java
// Valores máximos y mínimos
System.out.println(Integer.MAX_VALUE);    // 2147483647
System.out.println(Integer.MIN_VALUE);    // -2147483648
System.out.println(Double.MAX_VALUE);     // 1.7976931348623157E308
System.out.println(Double.MIN_VALUE);     // 4.9E-324

// Tamaños en bytes
System.out.println(Integer.BYTES);        // 4
System.out.println(Double.BYTES);         // 8
System.out.println(Character.BYTES);      // 2
```

## Pool de Objects y Cache

Java mantiene un cache para ciertos valores de wrapper classes para optimizar memoria:

```java
// Estos objetos se reutilizan del cache
Integer a = 127;
Integer b = 127;
System.out.println(a == b); // true (mismo objeto en memoria)

// Estos son objetos diferentes
Integer c = 128;
Integer d = 128;
System.out.println(c == d); // false (objetos diferentes)

// Siempre usar equals() para comparar valores
System.out.println(c.equals(d)); // true
```

**Rangos de cache:**
- `Integer`: -128 a 127
- `Byte`: -128 a 127
- `Short`: -128 a 127
- `Long`: -128 a 127
- `Character`: 0 a 127
- `Boolean`: true y false

## Ejemplos Prácticos

### Ejemplo 1: Uso en Colecciones
```java
import java.util.*;

public class EjemploColecciones {
    public static void main(String[] args) {
        // Lista de enteros
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);
        
        // Mapa con wrappers
        Map<String, Double> precios = new HashMap<>();
        precios.put("Producto A", 19.99);
        precios.put("Producto B", 29.99);
        
        // Stream con wrappers
        OptionalDouble promedio = numeros.stream()
            .mapToDouble(Integer::doubleValue)
            .average();
    }
}
```

### Ejemplo 2: Validación y Conversión
```java
public class ValidacionConversion {
    public static Integer convertirTexto(String texto) {
        try {
            return Integer.valueOf(texto);
        } catch (NumberFormatException e) {
            return null; // Wrapper puede ser null
        }
    }
    
    public static void main(String[] args) {
        Integer numero = convertirTexto("123");
        if (numero != null) {
            System.out.println("Número válido: " + numero);
        } else {
            System.out.println("Número inválido");
        }
    }
}
```

### Ejemplo 3: Operaciones Matemáticas
```java
import java.util.List;
import java.util.Arrays;

public class OperacionesMatematicas {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(10, 20, 30, 40, 50);
        
        // Suma usando streams
        int suma = numeros.stream()
            .mapToInt(Integer::intValue)
            .sum();
        
        // Encontrar máximo
        Integer maximo = numeros.stream()
            .max(Integer::compareTo)
            .orElse(0);
        
        System.out.println("Suma: " + suma);
        System.out.println("Máximo: " + maximo);
    }
}
```

## Consideraciones de Rendimiento

### Cuándo usar primitivos vs wrappers

**Usar primitivos cuando:**
- Se realizan cálculos intensivos
- Se requiere máximo rendimiento
- No se necesita funcionalidad de objeto
- Se trabaja con arrays grandes

**Usar wrappers cuando:**
- Se trabaja con colecciones
- Se necesita representar ausencia de valor (null)
- Se requieren métodos utilitarios
- Se usa en genéricos

### Impacto en memoria
```java
// Primitivo: 4 bytes
int primitivo = 100;

// Wrapper: ~16 bytes (objeto + referencia + overhead)
Integer wrapper = 100;
```

## Buenas Prácticas

1. **Usar equals() para comparar wrappers:**
```java
Integer a = 1000;
Integer b = 1000;
// Incorrecto
if (a == b) { /* puede fallar */ }
// Correcto
if (a.equals(b)) { /* siempre funciona */ }
```

2. **Validar null antes de unboxing:**
```java
Integer numero = obtenerNumero(); // puede retornar null
if (numero != null) {
    int valor = numero; // safe unboxing
}
```

3. **Usar valueOf() en lugar de constructores:**
```java
// Deprecated
Integer numero = new Integer(10);
// Recomendado
Integer numero = Integer.valueOf(10);
```

4. **Preferir primitivos en loops intensivos:**
```java
// Menos eficiente
for (Integer i = 0; i < 1000000; i++) {
    // boxing/unboxing en cada iteración
}

// Más eficiente
for (int i = 0; i < 1000000; i++) {
    // solo primitivos
}
```

## Conclusión

Las Type-Wrapper Classes son fundamentales en Java moderno, proporcionando un puente entre el mundo de los primitivos y los objetos. Su uso correcto mejora la flexibilidad del código mientras que su uso inadecuado puede impactar el rendimiento. La clave está en entender cuándo y cómo usarlas apropiadamente.

---

