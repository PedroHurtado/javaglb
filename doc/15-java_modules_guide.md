# Guía Completa de Módulos en Java (Project Jigsaw)

## Índice
1. [Introducción](#introducción)
2. [¿Qué son los módulos?](#qué-son-los-módulos)
3. [Características principales](#características-principales)
4. [Estructura de un módulo](#estructura-de-un-módulo)
5. [Tipos de módulos](#tipos-de-módulos)
6. [Ventajas de la modularización](#ventajas-de-la-modularización)
7. [Ejemplo práctico: Antes vs Después](#ejemplo-práctico-antes-vs-después)
8. [Compilación y ejecución](#compilación-y-ejecución)
9. [Herramientas útiles](#herramientas-útiles)
10. [Mejores prácticas](#mejores-prácticas)
11. [Casos de uso ideales](#casos-de-uso-ideales)

## Introducción

El sistema de módulos de Java, conocido como **Project Jigsaw**, fue introducido en Java 9 como una de las características más importantes del lenguaje. Permite organizar y encapsular código a un nivel superior que los paquetes, proporcionando un control granular sobre qué partes de tu aplicación son accesibles desde el exterior.

## ¿Qué son los módulos?

Los módulos son unidades de organización de código que:
- Agrupan paquetes relacionados
- Definen explícitamente sus dependencias
- Controlan qué clases y paquetes son accesibles públicamente
- Proporcionan encapsulación fuerte más allá de los modificadores de acceso tradicionales

## Características principales

### Encapsulación fuerte
- Solo las clases y paquetes que explícitamente exportes serán accesibles desde otros módulos
- Verdadero ocultamiento de implementación interna

### Dependencias explícitas
- Debes declarar qué módulos necesitas para funcionar
- Elimina las dependencias implícitas y el "classpath hell"

### Servicios desacoplados
- Puedes definir interfaces de servicios y sus implementaciones de manera desacoplada
- Permite arquitecturas plugin-based

### Reflexión controlada
- Puedes controlar qué módulos pueden acceder a tus clases mediante reflexión
- Mayor seguridad y control

## Estructura de un módulo

Cada módulo debe tener un archivo `module-info.java` en su directorio raíz:

```java
module com.ejemplo.mimodulo {
    // Módulos que necesitas
    requires java.base;        // Implícito, siempre presente
    requires java.sql;
    requires transitive java.logging; // Dependencia transitiva
    
    // Paquetes que exportas
    exports com.ejemplo.api;
    exports com.ejemplo.util to com.ejemplo.cliente; // Exportación cualificada
    
    // Servicios que proporcionas
    provides com.ejemplo.Service with com.ejemplo.ServiceImpl;
    
    // Servicios que consumes
    uses com.ejemplo.Plugin;
    
    // Permite reflexión
    opens com.ejemplo.internal to com.fasterxml.jackson.databind;
    opens com.ejemplo.internal; // Abierto a todos
}
```

### Directivas del module-info.java

| Directiva | Descripción | Ejemplo |
|-----------|-------------|---------|
| `requires` | Declara dependencia de otro módulo | `requires java.logging;` |
| `requires transitive` | Dependencia transitiva (quien use tu módulo también tendrá acceso) | `requires transitive java.sql;` |
| `exports` | Hace un paquete accesible a otros módulos | `exports com.ejemplo.api;` |
| `exports...to` | Exportación cualificada a módulos específicos | `exports com.ejemplo.internal to com.ejemplo.test;` |
| `opens` | Permite reflexión profunda en runtime | `opens com.ejemplo.entity;` |
| `opens...to` | Permite reflexión a módulos específicos | `opens com.ejemplo.entity to hibernate.core;` |
| `uses` | Declara que usa un servicio | `uses com.ejemplo.spi.Plugin;` |
| `provides...with` | Proporciona implementación de un servicio | `provides com.ejemplo.spi.Plugin with com.ejemplo.impl.MyPlugin;` |

## Tipos de módulos

### 1. Módulos nombrados (Named modules)
- Tienen `module-info.java`
- Nombre explícito definido
- Control total sobre exports e imports

### 2. Módulos automáticos (Automatic modules)
- JARs tradicionales colocados en el module path
- Se convierten automáticamente en módulos
- Exportan todos sus paquetes
- Pueden leer todos los otros módulos

### 3. Módulos sin nombre (Unnamed module)
- Código en el classpath tradicional
- Solo puede leer módulos automáticos y otros código del classpath
- No puede ser leído por módulos nombrados

## Ventajas de la modularización

### 🔒 Mayor seguridad
- Encapsulación real más allá de los paquetes
- Control granular sobre accesibilidad
- Superficie de ataque reducida

### ⚡ Mejor rendimiento
- JVM puede optimizar mejor el código modular
- Carga más rápida de aplicaciones
- Menor uso de memoria

### 📦 Dependencias claras
- Elimina el "JAR hell"
- Dependencias explícitas y verificables
- Detección temprana de problemas de dependencias

### 🔧 Imágenes más pequeñas
- Con `jlink` puedes crear runtime customizados
- Solo incluye los módulos necesarios
- Aplicaciones más ligeras

### 📈 Mantenibilidad
- Arquitectura más clara y organizada
- Separación de responsabilidades
- Evolución independiente de módulos

## Ejemplo práctico: Antes vs Después

### Aplicación ANTES de modularizar

#### Estructura de directorios
```
src/
  main/
    java/
      com/
        ejemplo/
          app/
            Main.java
            UserService.java
          database/
            DatabaseConnection.java
            UserRepository.java
          utils/
            StringUtils.java
            DateUtils.java
```

#### Problemas identificados
- Todas las clases son públicas y accesibles desde cualquier lugar
- No hay control sobre qué partes de la API son estables
- Dependencias implícitas - no está claro qué necesita cada parte
- Difícil de mantener y evolucionar
- No hay encapsulación real más allá de los paquetes

### Aplicación DESPUÉS de modularizar

#### Nueva estructura de directorios
```
modules/
  app/
    src/main/java/
      module-info.java
      com/ejemplo/app/
        Main.java
        UserService.java
  database/
    src/main/java/
      module-info.java
      com/ejemplo/database/
        UserRepository.java
        DatabaseConnection.java
  utils/
    src/main/java/
      module-info.java
      com/ejemplo/utils/
        StringUtils.java
        DateUtils.java
```

#### Módulo Utils
```java
// module-info.java
module com.ejemplo.utils {
    exports com.ejemplo.utils;
}
```

#### Módulo Database
```java
// module-info.java
module com.ejemplo.database {
    requires com.ejemplo.utils;
    exports com.ejemplo.database to com.ejemplo.app;
}
```

#### Módulo App
```java
// module-info.java
module com.ejemplo.app {
    requires com.ejemplo.database;
    requires com.ejemplo.utils;
    exports com.ejemplo.app;
}
```

#### Beneficios obtenidos
1. **Encapsulación fuerte**: `DatabaseConnection` ya no es accesible desde fuera del módulo database
2. **Dependencias explícitas**: Está claro qué módulo necesita qué
3. **Mantenibilidad**: Cada módulo tiene responsabilidades claras
4. **Reutilización**: El módulo utils puede usarse en otros proyectos
5. **Seguridad**: Control total sobre qué clases son accesibles

## Compilación y ejecución

### Compilación manual
```bash
# 1. Compilar módulo utils
javac -d modules/utils/target/classes \
  --module-path modules \
  modules/utils/src/main/java/module-info.java \
  modules/utils/src/main/java/com/ejemplo/utils/*.java

# 2. Compilar módulo database
javac -d modules/database/target/classes \
  --module-path modules:modules/utils/target/classes \
  modules/database/src/main/java/module-info.java \
  modules/database/src/main/java/com/ejemplo/database/*.java

# 3. Compilar módulo app
javac -d modules/app/target/classes \
  --module-path modules:modules/utils/target/classes:modules/database/target/classes \
  modules/app/src/main/java/module-info.java \
  modules/app/src/main/java/com/ejemplo/app/*.java
```

### Ejecución
```bash
java --module-path modules/utils/target/classes:modules/database/target/classes:modules/app/target/classes \
  --module com.ejemplo.app/com.ejemplo.app.Main
```

### Crear imagen personalizada con jlink
```bash
# Crear imagen
jlink --module-path modules/utils/target/classes:modules/database/target/classes:modules/app/target/classes \
  --add-modules com.ejemplo.app \
  --output dist/myapp

# Ejecutar imagen
dist/myapp/bin/java --module com.ejemplo.app/com.ejemplo.app.Main
```

### Con Maven
```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
        </plugin>
        
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M5</version>
        </plugin>
    </plugins>
</build>
```

### Con Gradle
```gradle
plugins {
    id 'java'
}

java {
    modularity.inferModulePath = true
}

compileJava {
    inputs.property("moduleName", "com.ejemplo.app")
    doFirst {
        options.compilerArgs = [
            '--module-path', classpath.asPath,
        ]
        classpath = files()
    }
}
```

## Herramientas útiles

### jdeps - Análisis de dependencias
```bash
# Analizar dependencias de un JAR
jdeps --module-path libs myapp.jar

# Generar module-info.java automáticamente
jdeps --generate-module-info src myapp.jar

# Analizar dependencias de JDK
jdeps --jdk-internals myapp.jar
```

### jlink - Crear runtime personalizado
```bash
# Crear runtime con módulos específicos
jlink --module-path $JAVA_HOME/jmods:mymods \
  --add-modules com.ejemplo.app \
  --output myapp-runtime

# Con compresión
jlink --module-path $JAVA_HOME/jmods:mymods \
  --add-modules com.ejemplo.app \
  --compress=2 \
  --output myapp-runtime
```

### jmod - Herramienta para módulos
```bash
# Crear archivo .jmod
jmod create --class-path classes \
  --cmds commands \
  --config config \
  mymodule.jmod

# Listar contenido
jmod list mymodule.jmod

# Describir módulo
jmod describe mymodule.jmod
```

## Mejores prácticas

### Diseño de módulos

1. **Un módulo por responsabilidad**
   - Cada módulo debe tener una responsabilidad clara
   - Evita módulos demasiado grandes o pequeños

2. **API estable**
   - Solo exporta lo que realmente necesitas
   - Mantén las APIs públicas estables

3. **Dependencias mínimas**
   - Minimiza las dependencias entre módulos
   - Evita dependencias circulares

### Naming conventions

```java
// Nombres descriptivos
module com.empresa.producto.modulo {
    // No com.empresa.modulo
}

// Consistencia con paquetes
module com.ejemplo.database {
    exports com.ejemplo.database.api;
    // No exports com.ejemplo.db.api;
}
```

### Versionado
```java
// Considera versioning en nombres
module com.ejemplo.api.v1 {
    exports com.ejemplo.api.v1;
}

module com.ejemplo.api.v2 {
    exports com.ejemplo.api.v2;
}
```

### Testing
```java
// Módulo de test
module com.ejemplo.app.test {
    requires com.ejemplo.app;
    requires org.junit.jupiter.api;
    
    // Abrir para reflexión de frameworks de testing
    opens com.ejemplo.app.test to org.junit.platform.commons;
}
```

## Casos de uso ideales

### ✅ Cuándo usar módulos

- **Aplicaciones grandes** con múltiples equipos
- **Bibliotecas públicas** que quieren ofrecer APIs estables
- **Sistemas críticos** que requieren alta seguridad
- **Aplicaciones distribuidas** como imágenes nativas
- **Arquitecturas microservicios** con componentes bien definidos

### ❌ Cuándo NO usar módulos

- **Aplicaciones pequeñas** con pocos paquetes
- **Prototipios rápidos** o código experimental
- **Equipos pequeños** sin experiencia en módulos
- **Dependencias legacy** que no soportan módulos

## Migración gradual

### Estrategia bottom-up
1. Identifica módulos de utilidades sin dependencias
2. Modulariza bibliotecas de bajo nivel
3. Avanza hacia capas superiores
4. Finalmente modulariza la aplicación principal

### Estrategia top-down
1. Define la arquitectura modular objetivo
2. Crea módulos vacíos con sus interfaces
3. Mueve código gradualmente
4. Refactoriza dependencias

### Herramientas de migración
```bash
# Analizar aplicación existente
jdeps --generate-module-info . myapp.jar

# Identificar dependencias problemáticas
jdeps --jdk-internals myapp.jar
```

## Troubleshooting común

### Error: Module not found
```
Error: Module com.ejemplo.database not found
```
**Solución**: Verificar module-path y que el módulo esté compilado

### Error: Package not visible
```
Error: Package com.ejemplo.internal is not visible
```
**Solución**: Agregar `exports` en module-info.java

### Error: Cyclic dependence
```
Error: Cyclic dependence involving com.ejemplo.a
```
**Solución**: Reestructurar módulos para eliminar dependencias circulares

### Split packages
```
Warning: Split package com.ejemplo.util
```
**Solución**: Mover todas las clases del paquete al mismo módulo

## Recursos adicionales

### Documentación oficial
- [Oracle Java Module System](https://docs.oracle.com/javase/9/docs/api/java.base/java/lang/module/package-summary.html)
- [JEP 261: Module System](https://openjdk.java.net/jeps/261)

### Herramientas
- [ModiTect](https://github.com/moditect/moditect) - Plugin Maven/Gradle para migración
- [jlink](https://docs.oracle.com/en/java/javase/11/tools/jlink.html) - Documentación oficial

### Libros recomendados
- "Java 9 Modularity" by Sander Mak and Paul Bakker
- "The Java Module System" by Nicolai Parlog

---

## Conclusión

El sistema de módulos de Java representa un cambio paradigmático en cómo organizamos y estructuramos aplicaciones Java. Aunque requiere una curva de aprendizaje inicial, los beneficios a largo plazo en términos de mantenibilidad, seguridad y rendimiento son significativos.

La clave está en aplicar módulos de manera gradual y estratégica, comenzando con nuevos proyectos o componentes bien definidos, y expandiendo su uso conforme el equipo gana experiencia.

**¿Cuándo empezar?** Si tu aplicación tiene más de 10 paquetes principales o múltiples desarrolladores, considera la modularización. Los beneficios superan la inversión inicial de tiempo y aprendizaje.

---

*Guía creada para desarrolladores Java interesados en adoptar el sistema de módulos. Actualizada para Java 11+.*