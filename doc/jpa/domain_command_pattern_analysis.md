# Eliminación de Setters por Métodos de Comando en Domain-Driven Design

## Resumen Ejecutivo

La decisión de eliminar setters públicos en favor de métodos de comando (métodos void que modifican estado) representa una evolución hacia un diseño más robusto y expresivo en el contexto de Domain-Driven Design (DDD). Esta aproximación mejora el control sobre eventos de dominio, invariantes de negocio y la expresividad del modelo.

## Definición del Patrón

### Enfoque Tradicional (Con Setters)
```java
public class Pedido {
    private EstadoPedido estado;
    private LocalDateTime fechaActualizacion;
    
    // Problemas: ¿Cuándo disparar eventos? ¿Cómo validar transiciones?
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
        this.fechaActualizacion = LocalDateTime.now();
    }
}
```

### Enfoque por Comandos (Propuesto)
```java
public class Pedido {
    private EstadoPedido estado;
    private LocalDateTime fechaActualizacion;
    private List<EventoDominio> eventos = new ArrayList<>();
    
    public void confirmar() {
        validarQueSePuedeConfirmar();
        this.estado = EstadoPedido.CONFIRMADO;
        this.fechaActualizacion = LocalDateTime.now();
        this.eventos.add(new PedidoConfirmadoEvent(this.id));
    }
    
    public void cancelar(String motivo) {
        validarQueSePuedeCancelar();
        this.estado = EstadoPedido.CANCELADO;
        this.fechaActualizacion = LocalDateTime.now();
        this.eventos.add(new PedidoCanceladoEvent(this.id, motivo));
    }
}
```

## Ventajas (Pros)

### 1. **Control Explícito de Eventos de Dominio**
- **Punto único de emisión**: Cada método de comando es un lugar específico donde disparar eventos
- **Timing preciso**: Los eventos se lanzan exactamente cuando la operación de negocio se completa
- **Contexto completo**: Los eventos pueden incluir información específica de la operación realizada

### 2. **Expresividad del Modelo de Negocio**
- **Lenguaje ubicuo**: Los métodos reflejan exactamente las operaciones del dominio
- **Intención clara**: `factura.marcarComoPagada()` vs `factura.setEstado(PAGADA)`
- **Documentación viva**: El código se convierte en documentación del comportamiento del negocio

### 3. **Protección de Invariantes**
- **Validaciones centralizadas**: Cada comando puede validar precondiciones específicas
- **Estados consistentes**: Imposible poner la entidad en estados inválidos
- **Transiciones controladas**: Solo se permiten cambios de estado válidos según las reglas de negocio

### 4. **Mejor Testabilidad**
- **Pruebas semánticas**: Tests que reflejan casos de uso reales del negocio
- **Verificación de eventos**: Fácil testing de que se disparan los eventos correctos
- **Casos edge claros**: Las validaciones hacen explícitos los casos límite

### 5. **Mantenibilidad a Largo Plazo**
- **Evolución controlada**: Cambios en reglas de negocio se localizan en métodos específicos
- **Debugging facilitado**: Stack traces más claros sobre qué operación de negocio falló
- **Refactoring seguro**: IDEs pueden rastrear mejor el uso de operaciones específicas

## Desventajas (Contras)

### 1. **Mayor Complejidad Inicial**
- **Curva de aprendizaje**: Requiere que el equipo entienda los conceptos de DDD
- **Más código**: Cada operación requiere un método específico vs un simple setter
- **Decisiones de diseño**: Hay que decidir qué operaciones merecen su propio método

### 2. **Posible Rigidez**
- **Operaciones no previstas**: Cambios simples pueden requerir nuevos métodos
- **Over-engineering**: Riesgo de crear métodos para operaciones triviales
- **Refactoring costoso**: Cambiar la granularidad de comandos puede ser complejo

### 3. **Overhead de Performance**
- **Más llamadas a método**: En lugar de acceso directo a propiedades
- **Gestión de eventos**: Lista de eventos consume memoria adicional
- **Validaciones**: Cada comando ejecuta validaciones que podrían ser innecesarias

### 4. **Complejidad en Frameworks**
- **Serialización**: Algunos frameworks esperan getters/setters para serialización
- **ORM**: Mappers objeto-relacionales pueden requerir configuración adicional
- **Binding**: Formularios web pueden necesitar adaptadores

### 5. **Coordinación de Equipo**
- **Consistencia**: Todo el equipo debe seguir el mismo patrón
- **Documentación**: Requiere documentar claramente qué métodos son comandos
- **Code reviews**: Revisiones más complejas para verificar cumplimiento del patrón

## Consideraciones de Implementación

### Patrón de Eventos de Dominio
```java
public abstract class EntidadBase {
    private List<EventoDominio> eventos = new ArrayList<>();
    
    protected void agregarEvento(EventoDominio evento) {
        this.eventos.add(evento);
    }
    
    public List<EventoDominio> obtenerEventos() {
        return Collections.unmodifiableList(eventos);
    }
    
    public void limpiarEventos() {
        this.eventos.clear();
    }
}
```

### Manejo de Validaciones
```java
public class Producto extends EntidadBase {
    public void activar() {
        if (this.estado == EstadoProducto.ELIMINADO) {
            throw new ReglaNegocioException("No se puede activar un producto eliminado");
        }
        
        this.estado = EstadoProducto.ACTIVO;
        this.fechaActivacion = LocalDateTime.now();
        agregarEvento(new ProductoActivadoEvent(this.id));
    }
}
```

### Compatibilidad con Frameworks
```java
// Para serialización/deserialización
@JsonCreator
public static Producto desde(
    @JsonProperty("id") Long id,
    @JsonProperty("nombre") String nombre,
    @JsonProperty("estado") EstadoProducto estado) {
    return new Producto(id, nombre, estado);
}

// Getters públicos, setters privados/protected
public EstadoProducto getEstado() { return estado; }
private void setEstado(EstadoProducto estado) { this.estado = estado; }
```

## Métricas de Decisión

### Cuándo Aplicar Este Patrón
- ✅ Dominios complejos con reglas de negocio ricas
- ✅ Necesidad de auditoría detallada de cambios
- ✅ Equipos con experiencia en DDD
- ✅ Aplicaciones con ciclo de vida largo
- ✅ Requieren eventos de dominio robustos

### Cuándo Considerar Alternativas
- ❌ Aplicaciones CRUD simples
- ❌ Prototipos o pruebas de concepto
- ❌ Equipos sin experiencia en DDD
- ❌ Restricciones severas de performance
- ❌ Frameworks que requieren setters obligatoriamente

## Estrategia de Migración

### Fase 1: Identificación
1. Mapear todas las operaciones de negocio actuales
2. Identificar puntos donde se disparan eventos
3. Documentar invariantes de dominio existentes

### Fase 2: Implementación Gradual
1. Comenzar con agregados más críticos
2. Mantener setters como `@Deprecated` temporalmente
3. Implementar métodos de comando uno por uno
4. Agregar tests para nuevos métodos

### Fase 3: Consolidación
1. Remover setters deprecated
2. Refactorizar código cliente
3. Documentar patrones establecidos
4. Entrenar al equipo en nuevos patrones

## Patrones Relacionados

### Command Pattern
Los métodos de comando siguen el patrón Command, encapsulando operaciones específicas del dominio.

### Event Sourcing
Se complementa perfectamente con Event Sourcing, donde cada comando genera eventos que representan cambios de estado.

### CQRS (Command Query Responsibility Segregation)
Los métodos de comando forman la parte "Command" de CQRS, separando claramente las operaciones de escritura.

## Conclusiones

La eliminación de setters en favor de métodos de comando representa una evolución madura hacia un diseño más expresivo y controlado. Aunque introduce complejidad inicial, los beneficios a largo plazo en mantenibilidad, expresividad y control de eventos justifican la inversión, especialmente en dominios complejos.

**Recomendación**: Implementar gradualmente, comenzando por los agregados más críticos del dominio, y establecer guías claras para el equipo sobre cuándo y cómo aplicar este patrón.

## Referencias y Lecturas Adicionales

- Evans, Eric. "Domain-Driven Design: Tackling Complexity in the Heart of Software"
- Vernon, Vaughn. "Implementing Domain-Driven Design"
- Fowler, Martin. "Patterns of Enterprise Application Architecture"
- Richardson, Chris. "Microservices Patterns"