# Guía Completa de Principios SOLID en Java

## Introducción

Los principios SOLID son cinco principios de diseño de software que ayudan a crear código más mantenible, flexible y escalable. Fueron introducidos por Robert C. Martin (Uncle Bob) y son fundamentales en la programación orientada a objetos.

**SOLID** es un acrónimo que representa:
- **S** - Single Responsibility Principle (Principio de Responsabilidad Única)
- **O** - Open/Closed Principle (Principio Abierto/Cerrado)
- **L** - Liskov Substitution Principle (Principio de Sustitución de Liskov)
- **I** - Interface Segregation Principle (Principio de Segregación de Interfaces)
- **D** - Dependency Inversion Principle (Principio de Inversión de Dependencias)

---

## 1. Single Responsibility Principle (SRP)

### Definición
Una clase debe tener una sola razón para cambiar, es decir, debe tener una sola responsabilidad.

### ❌ Ejemplo Incorrecto

```java
// Clase que viola SRP - tiene múltiples responsabilidades
public class Usuario {
    private String nombre;
    private String email;
    
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
    
    // Responsabilidad 1: Validar datos
    public boolean validarEmail() {
        return email.contains("@") && email.contains(".");
    }
    
    // Responsabilidad 2: Enviar email
    public void enviarEmail(String mensaje) {
        // Lógica para enviar email
        System.out.println("Enviando email a: " + email);
        System.out.println("Mensaje: " + mensaje);
    }
    
    // Responsabilidad 3: Guardar en base de datos
    public void guardarEnBaseDeDatos() {
        // Lógica para guardar en BD
        System.out.println("Guardando usuario en BD: " + nombre);
    }
    
    // Responsabilidad 4: Generar reporte
    public String generarReporte() {
        return "Reporte del usuario: " + nombre + " - " + email;
    }
}
```

### ✅ Ejemplo Correcto

```java
// Clase con una sola responsabilidad: representar un usuario
public class Usuario {
    private String nombre;
    private String email;
    
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }
    
    // Getters y setters
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
}

// Responsabilidad específica: validación
public class ValidadorUsuario {
    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public boolean validarNombre(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }
}

// Responsabilidad específica: envío de emails
public class ServicioEmail {
    public void enviarEmail(Usuario usuario, String mensaje) {
        System.out.println("Enviando email a: " + usuario.getEmail());
        System.out.println("Mensaje: " + mensaje);
    }
}

// Responsabilidad específica: persistencia
public class RepositorioUsuario {
    public void guardar(Usuario usuario) {
        System.out.println("Guardando usuario en BD: " + usuario.getNombre());
    }
    
    public Usuario buscarPorEmail(String email) {
        // Lógica de búsqueda
        return null;
    }
}

// Responsabilidad específica: generación de reportes
public class GeneradorReportes {
    public String generarReporteUsuario(Usuario usuario) {
        return "Reporte del usuario: " + usuario.getNombre() + " - " + usuario.getEmail();
    }
}
```

---

## 2. Open/Closed Principle (OCP)

### Definición
Las entidades de software deben estar abiertas para extensión pero cerradas para modificación.

### ❌ Ejemplo Incorrecto

```java
// Clase que viola OCP - necesita modificación para agregar nuevos tipos
public class CalculadoraDescuento {
    public double calcularDescuento(String tipoCliente, double monto) {
        if (tipoCliente.equals("REGULAR")) {
            return monto * 0.05; // 5% descuento
        } else if (tipoCliente.equals("VIP")) {
            return monto * 0.10; // 10% descuento
        } else if (tipoCliente.equals("PREMIUM")) {
            return monto * 0.15; // 15% descuento
        }
        // Para agregar un nuevo tipo, necesitamos modificar este método
        return 0;
    }
}
```

### ✅ Ejemplo Correcto

```java
// Interface que define el contrato
public interface EstrategiaDescuento {
    double calcularDescuento(double monto);
}

// Implementaciones específicas (extensiones)
public class DescuentoRegular implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double monto) {
        return monto * 0.05;
    }
}

public class DescuentoVIP implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double monto) {
        return monto * 0.10;
    }
}

public class DescuentoPremium implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double monto) {
        return monto * 0.15;
    }
}

// Nueva extensión sin modificar código existente
public class DescuentoEmpresarial implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double monto) {
        return monto * 0.20;
    }
}

// Clase cerrada para modificación, abierta para extensión
public class CalculadoraDescuento {
    private EstrategiaDescuento estrategia;
    
    public CalculadoraDescuento(EstrategiaDescuento estrategia) {
        this.estrategia = estrategia;
    }
    
    public double calcularDescuento(double monto) {
        return estrategia.calcularDescuento(monto);
    }
    
    public void setEstrategia(EstrategiaDescuento estrategia) {
        this.estrategia = estrategia;
    }
}
```

---

## 3. Liskov Substitution Principle (LSP)

### Definición
Los objetos de una clase derivada deben poder reemplazar a los objetos de la clase base sin alterar el correcto funcionamiento del programa.

### ❌ Ejemplo Incorrecto

```java
// Clase base
public class Ave {
    public void volar() {
        System.out.println("El ave está volando");
    }
    
    public void comer() {
        System.out.println("El ave está comiendo");
    }
}

// Violación de LSP - el Pingüino no puede volar
public class Pinguino extends Ave {
    @Override
    public void volar() {
        // Viola LSP - cambia el comportamiento esperado
        throw new UnsupportedOperationException("Los pingüinos no pueden volar");
    }
}

// Uso problemático
public class EjemploProblematico {
    public void hacerVolarAve(Ave ave) {
        ave.volar(); // Falla si ave es un Pingüino
    }
}
```

### ✅ Ejemplo Correcto

```java
// Clase base más general
public abstract class Ave {
    public abstract void comer();
    public abstract void moverse();
}

// Interface específica para aves que vuelan
public interface Volador {
    void volar();
}

// Interface específica para aves que nadan
public interface Nadador {
    void nadar();
}

// Implementaciones que respetan LSP
public class Aguila extends Ave implements Volador {
    @Override
    public void comer() {
        System.out.println("El águila está cazando");
    }
    
    @Override
    public void moverse() {
        volar();
    }
    
    @Override
    public void volar() {
        System.out.println("El águila está volando");
    }
}

public class Pinguino extends Ave implements Nadador {
    @Override
    public void comer() {
        System.out.println("El pingüino está comiendo pescado");
    }
    
    @Override
    public void moverse() {
        nadar();
    }
    
    @Override
    public void nadar() {
        System.out.println("El pingüino está nadando");
    }
}

// Uso correcto
public class EjemploLSP {
    public void alimentarAve(Ave ave) {
        ave.comer(); // Funciona con cualquier Ave
    }
    
    public void hacerVolarAve(Volador volador) {
        volador.volar(); // Solo acepta aves que pueden volar
    }
}
```

---

## 4. Interface Segregation Principle (ISP)

### Definición
Los clientes no deben ser forzados a depender de interfaces que no usan.

### ❌ Ejemplo Incorrecto

```java
// Interface "gorda" que viola ISP
public interface Trabajador {
    void trabajar();
    void comer();
    void dormir();
    void cobrarSalario();
    void tomarVacaciones();
}

// Robot que no necesita comer, dormir ni tomar vacaciones
public class Robot implements Trabajador {
    @Override
    public void trabajar() {
        System.out.println("Robot trabajando");
    }
    
    @Override
    public void comer() {
        // Robot no come - método inútil
        throw new UnsupportedOperationException("Robot no come");
    }
    
    @Override
    public void dormir() {
        // Robot no duerme - método inútil
        throw new UnsupportedOperationException("Robot no duerme");
    }
    
    @Override
    public void cobrarSalario() {
        // Robot no cobra salario - método inútil
        throw new UnsupportedOperationException("Robot no cobra salario");
    }
    
    @Override
    public void tomarVacaciones() {
        // Robot no toma vacaciones - método inútil
        throw new UnsupportedOperationException("Robot no toma vacaciones");
    }
}
```

### ✅ Ejemplo Correcto

```java
// Interfaces específicas y cohesivas
public interface Trabajable {
    void trabajar();
}

public interface Alimentable {
    void comer();
}

public interface Descansable {
    void dormir();
}

public interface Empleado {
    void cobrarSalario();
    void tomarVacaciones();
}

// Robot implementa solo lo que necesita
public class Robot implements Trabajable {
    @Override
    public void trabajar() {
        System.out.println("Robot trabajando 24/7");
    }
}

// Empleado humano implementa todas las interfaces relevantes
public class EmpleadoHumano implements Trabajable, Alimentable, Descansable, Empleado {
    @Override
    public void trabajar() {
        System.out.println("Empleado trabajando");
    }
    
    @Override
    public void comer() {
        System.out.println("Empleado comiendo");
    }
    
    @Override
    public void dormir() {
        System.out.println("Empleado durmiendo");
    }
    
    @Override
    public void cobrarSalario() {
        System.out.println("Empleado cobrando salario");
    }
    
    @Override
    public void tomarVacaciones() {
        System.out.println("Empleado tomando vacaciones");
    }
}

// Freelancer que no toma vacaciones
public class Freelancer implements Trabajable, Alimentable, Descansable {
    @Override
    public void trabajar() {
        System.out.println("Freelancer trabajando");
    }
    
    @Override
    public void comer() {
        System.out.println("Freelancer comiendo");
    }
    
    @Override
    public void dormir() {
        System.out.println("Freelancer durmiendo");
    }
    
    // No implementa Empleado porque no aplica
}
```

---

## 5. Dependency Inversion Principle (DIP)

### Definición
Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones. Las abstracciones no deben depender de detalles; los detalles deben depender de abstracciones.

### ❌ Ejemplo Incorrecto

```java
// Clase de bajo nivel (detalle concreto)
public class BaseDeDatosMySQL {
    public void guardarUsuario(String usuario) {
        System.out.println("Guardando usuario en MySQL: " + usuario);
    }
    
    public String obtenerUsuario(String id) {
        return "Usuario desde MySQL: " + id;
    }
}

// Clase de alto nivel que depende directamente de la implementación concreta
public class ServicioUsuario {
    private BaseDeDatosMySQL baseDatos; // Dependencia directa - VIOLA DIP
    
    public ServicioUsuario() {
        this.baseDatos = new BaseDeDatosMySQL(); // Acoplamiento fuerte
    }
    
    public void registrarUsuario(String usuario) {
        // Lógica de negocio
        if (usuario != null && !usuario.isEmpty()) {
            baseDatos.guardarUsuario(usuario);
        }
    }
    
    public String buscarUsuario(String id) {
        return baseDatos.obtenerUsuario(id);
    }
}
```

### ✅ Ejemplo Correcto

```java
// Abstracción (interface)
public interface RepositorioUsuario {
    void guardarUsuario(String usuario);
    String obtenerUsuario(String id);
}

// Implementaciones concretas (detalles)
public class RepositorioUsuarioMySQL implements RepositorioUsuario {
    @Override
    public void guardarUsuario(String usuario) {
        System.out.println("Guardando usuario en MySQL: " + usuario);
    }
    
    @Override
    public String obtenerUsuario(String id) {
        return "Usuario desde MySQL: " + id;
    }
}

public class RepositorioUsuarioMongoDB implements RepositorioUsuario {
    @Override
    public void guardarUsuario(String usuario) {
        System.out.println("Guardando usuario en MongoDB: " + usuario);
    }
    
    @Override
    public String obtenerUsuario(String id) {
        return "Usuario desde MongoDB: " + id;
    }
}

public class RepositorioUsuarioMemoria implements RepositorioUsuario {
    private java.util.Map<String, String> usuarios = new java.util.HashMap<>();
    
    @Override
    public void guardarUsuario(String usuario) {
        usuarios.put(usuario, usuario);
        System.out.println("Guardando usuario en memoria: " + usuario);
    }
    
    @Override
    public String obtenerUsuario(String id) {
        return usuarios.get(id);
    }
}

// Clase de alto nivel que depende de la abstracción
public class ServicioUsuario {
    private RepositorioUsuario repositorio; // Depende de abstracción
    
    // Inyección de dependencia a través del constructor
    public ServicioUsuario(RepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }
    
    public void registrarUsuario(String usuario) {
        // Lógica de negocio
        if (usuario != null && !usuario.isEmpty()) {
            repositorio.guardarUsuario(usuario);
        }
    }
    
    public String buscarUsuario(String id) {
        return repositorio.obtenerUsuario(id);
    }
}

// Ejemplo de uso con inversión de dependencias
public class Main {
    public static void main(String[] args) {
        // Podemos cambiar fácilmente la implementación
        RepositorioUsuario repo1 = new RepositorioUsuarioMySQL();
        ServicioUsuario servicio1 = new ServicioUsuario(repo1);
        
        RepositorioUsuario repo2 = new RepositorioUsuarioMongoDB();
        ServicioUsuario servicio2 = new ServicioUsuario(repo2);
        
        RepositorioUsuario repo3 = new RepositorioUsuarioMemoria();
        ServicioUsuario servicio3 = new ServicioUsuario(repo3);
        
        // Todos funcionan igual desde la perspectiva del ServicioUsuario
        servicio1.registrarUsuario("Juan");
        servicio2.registrarUsuario("Ana");
        servicio3.registrarUsuario("Pedro");
    }
}
```

---

## Ejemplo Integrado: Sistema de Notificaciones

### ❌ Versión que Viola Varios Principios SOLID

```java
public class SistemaNotificaciones {
    // Viola SRP - maneja múltiples responsabilidades
    // Viola OCP - cerrado para extensión
    // Viola DIP - depende de implementaciones concretas
    
    public void enviarNotificacion(String tipo, String mensaje, String destinatario) {
        if (tipo.equals("EMAIL")) {
            // Lógica específica de email
            System.out.println("Configurando servidor SMTP...");
            System.out.println("Enviando email a: " + destinatario);
            System.out.println("Mensaje: " + mensaje);
        } else if (tipo.equals("SMS")) {
            // Lógica específica de SMS
            System.out.println("Conectando con API de SMS...");
            System.out.println("Enviando SMS a: " + destinatario);
            System.out.println("Mensaje: " + mensaje);
        } else if (tipo.equals("PUSH")) {
            // Lógica específica de push notification
            System.out.println("Configurando push notification...");
            System.out.println("Enviando push a: " + destinatario);
            System.out.println("Mensaje: " + mensaje);
        }
        
        // Viola SRP - también maneja logging
        System.out.println("Notificación enviada - Tipo: " + tipo + " - Hora: " + java.time.LocalDateTime.now());
    }
}
```

### ✅ Versión que Respeta los Principios SOLID

```java
// Abstracción para DIP
public interface ServicioNotificacion {
    void enviar(String mensaje, String destinatario);
    String getTipo();
}

// Implementaciones específicas (OCP - extensibles)
public class ServicioEmail implements ServicioNotificacion {
    @Override
    public void enviar(String mensaje, String destinatario) {
        System.out.println("Configurando servidor SMTP...");
        System.out.println("Enviando email a: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "EMAIL";
    }
}

public class ServicioSMS implements ServicioNotificacion {
    @Override
    public void enviar(String mensaje, String destinatario) {
        System.out.println("Conectando con API de SMS...");
        System.out.println("Enviando SMS a: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "SMS";
    }
}

public class ServicioPush implements ServicioNotificacion {
    @Override
    public void enviar(String mensaje, String destinatario) {
        System.out.println("Configurando push notification...");
        System.out.println("Enviando push a: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "PUSH";
    }
}

// SRP - Responsabilidad única: logging
public interface ServicioLog {
    void log(String mensaje);
}

public class ServicioLogImpl implements ServicioLog {
    @Override
    public void log(String mensaje) {
        System.out.println("LOG: " + mensaje + " - " + java.time.LocalDateTime.now());
    }
}

// SRP - Responsabilidad única: gestionar notificaciones
public class GestorNotificaciones {
    private ServicioLog servicioLog;
    
    public GestorNotificaciones(ServicioLog servicioLog) {
        this.servicioLog = servicioLog;
    }
    
    public void enviarNotificacion(ServicioNotificacion servicio, String mensaje, String destinatario) {
        try {
            servicio.enviar(mensaje, destinatario);
            servicioLog.log("Notificación enviada exitosamente - Tipo: " + servicio.getTipo());
        } catch (Exception e) {
            servicioLog.log("Error enviando notificación - Tipo: " + servicio.getTipo() + " - Error: " + e.getMessage());
        }
    }
}

// Uso del sistema mejorado
public class EjemploUso {
    public static void main(String[] args) {
        // Configuración de dependencias
        ServicioLog logger = new ServicioLogImpl();
        GestorNotificaciones gestor = new GestorNotificaciones(logger);
        
        // Diferentes servicios de notificación
        ServicioNotificacion email = new ServicioEmail();
        ServicioNotificacion sms = new ServicioSMS();
        ServicioNotificacion push = new ServicioPush();
        
        // Uso flexible
        gestor.enviarNotificacion(email, "Hola desde email", "user@example.com");
        gestor.enviarNotificacion(sms, "Hola desde SMS", "+1234567890");
        gestor.enviarNotificacion(push, "Hola desde push", "device123");
        
        // Fácil agregar nuevos tipos sin modificar código existente
        ServicioNotificacion slack = new ServicioSlack();
        gestor.enviarNotificacion(slack, "Hola desde Slack", "#general");
    }
}

// Nueva extensión sin modificar código existente (OCP)
class ServicioSlack implements ServicioNotificacion {
    @Override
    public void enviar(String mensaje, String destinatario) {
        System.out.println("Enviando mensaje a Slack canal: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "SLACK";
    }
}
```

---

## Beneficios de Aplicar SOLID

### Mantenibilidad
- **Código más fácil de entender**: Cada clase tiene una responsabilidad clara
- **Cambios localizados**: Modificaciones en una funcionalidad no afectan otras partes
- **Debugging simplificado**: Es más fácil encontrar y corregir errores

### Extensibilidad
- **Agregar nuevas funcionalidades**: Sin modificar código existente
- **Diferentes implementaciones**: Fácil intercambio de componentes
- **Evolución gradual**: El sistema puede crecer de manera orgánica

### Testabilidad
- **Aislamiento de componentes**: Cada parte se puede probar independientemente
- **Mocking sencillo**: Interfaces facilitan la creación de objetos mock
- **Cobertura de pruebas**: Mayor facilidad para escribir pruebas unitarias

### Flexibilidad
- **Bajo acoplamiento**: Componentes independientes
- **Alta cohesión**: Elementos relacionados están juntos
- **Configuración dinámica**: Cambio de comportamiento en tiempo de ejecución

---

## Consejos Prácticos

### 1. Identifica Responsabilidades
Antes de escribir una clase, pregúntate: "¿Qué hace esta clase?" Si la respuesta incluye "y" o "o", probablemente viola SRP.

### 2. Usa Interfaces Abundantemente
Las interfaces son la clave para cumplir OCP, LSP, ISP y DIP. No tengas miedo de crear muchas interfaces pequeñas y específicas.

### 3. Inyección de Dependencias
Utiliza frameworks como Spring para manejar la inyección de dependencias automáticamente.

### 4. Refactoring Gradual
No intentes aplicar todos los principios de una vez. Refactoriza gradualmente, comenzando con las violaciones más evidentes.

### 5. Pruebas Como Guía
Si es difícil escribir pruebas para tu código, probablemente viola algún principio SOLID.

---

## Conclusión

Los principios SOLID no son reglas absolutas, sino guías que nos ayudan a escribir mejor código. Su aplicación debe ser pragmática, considerando el contexto y las necesidades específicas del proyecto. Un código que sigue estos principios es más fácil de mantener, extender y probar, lo que resulta en un desarrollo más eficiente y productos de mayor calidad.

Recuerda que la práctica hace al maestro. Comienza aplicando estos principios en proyectos pequeños y gradualmente incorpóralos en proyectos más complejos. Con el tiempo, seguir estos principios se convertirá en algo natural en tu proceso de desarrollo.

