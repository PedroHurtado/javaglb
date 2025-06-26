# Guía Completa de JDBC (Java Database Connectivity)

## ¿Qué es JDBC?

JDBC (Java Database Connectivity) es una API estándar de Java que proporciona una interfaz uniforme para acceder a bases de datos relacionales desde aplicaciones Java. Permite ejecutar consultas SQL, actualizar datos y gestionar transacciones de manera independiente del sistema de gestión de base de datos específico.

## Arquitectura de JDBC

### Componentes Principales

1. **JDBC API**: Conjunto de interfaces y clases en los paquetes `java.sql` y `javax.sql`
2. **JDBC Driver Manager**: Gestiona la lista de drivers de base de datos
3. **JDBC Driver**: Implementación específica para cada SGBD
4. **Base de Datos**: El sistema de gestión de base de datos destino

### Tipos de Drivers JDBC

- **Tipo 1 (JDBC-ODBC Bridge)**: Traduce llamadas JDBC a ODBC
- **Tipo 2 (Native-API Driver)**: Utiliza bibliotecas nativas del cliente
- **Tipo 3 (Network Protocol Driver)**: Usa protocolo de red independiente
- **Tipo 4 (Thin Driver)**: Driver Java puro, más utilizado actualmente

## Clases e Interfaces Fundamentales

### Interfaces Principales

- `Connection`: Representa una sesión con la base de datos
- `Statement`: Ejecuta consultas SQL estáticas
- `PreparedStatement`: Ejecuta consultas SQL precompiladas
- `CallableStatement`: Ejecuta procedimientos almacenados
- `ResultSet`: Representa el resultado de una consulta

### Clases Importantes

- `DriverManager`: Gestiona drivers y establece conexiones
- `SQLException`: Maneja excepciones relacionadas con SQL
- `Types`: Define constantes para tipos de datos SQL

## Configuración y Conexión

### Dependencias Maven

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Establecer Conexión

```java
// Cargar el driver (opcional en versiones modernas)
Class.forName("com.mysql.cj.jdbc.Driver");

// Establecer conexión
String url = "jdbc:mysql://localhost:3306/mi_base_datos";
String usuario = "root";
String contraseña = "password";

Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
```

### URLs de Conexión Comunes

```java
// MySQL
"jdbc:mysql://localhost:3306/base_datos"

// PostgreSQL
"jdbc:postgresql://localhost:5432/base_datos"

// Oracle
"jdbc:oracle:thin:@localhost:1521:xe"

// SQL Server
"jdbc:sqlserver://localhost:1433;databaseName=base_datos"

// H2 (en memoria)
"jdbc:h2:mem:testdb"
```

## Operaciones CRUD

### Consultas SELECT

```java
public List<Usuario> obtenerUsuarios() throws SQLException {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT id, nombre, email FROM usuarios";
    
    try (Statement stmt = conexion.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuarios.add(usuario);
        }
    }
    return usuarios;
}
```

### Inserción de Datos

```java
public int insertarUsuario(Usuario usuario) throws SQLException {
    String sql = "INSERT INTO usuarios (nombre, email, edad) VALUES (?, ?, ?)";
    
    try (PreparedStatement pstmt = conexion.prepareStatement(sql, 
         Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setString(1, usuario.getNombre());
        pstmt.setString(2, usuario.getEmail());
        pstmt.setInt(3, usuario.getEdad());
        
        int filasAfectadas = pstmt.executeUpdate();
        
        // Obtener ID generado
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    }
    return 0;
}
```

### Actualización de Datos

```java
public boolean actualizarUsuario(Usuario usuario) throws SQLException {
    String sql = "UPDATE usuarios SET nombre = ?, email = ?, edad = ? WHERE id = ?";
    
    try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setString(1, usuario.getNombre());
        pstmt.setString(2, usuario.getEmail());
        pstmt.setInt(3, usuario.getEdad());
        pstmt.setInt(4, usuario.getId());
        
        return pstmt.executeUpdate() > 0;
    }
}
```

### Eliminación de Datos

```java
public boolean eliminarUsuario(int id) throws SQLException {
    String sql = "DELETE FROM usuarios WHERE id = ?";
    
    try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, id);
        return pstmt.executeUpdate() > 0;
    }
}
```

## Gestión de Transacciones

### Transacciones Manuales

```java
public void transferirDinero(int cuentaOrigen, int cuentaDestino, 
                           double cantidad) throws SQLException {
    conexion.setAutoCommit(false);
    
    try {
        // Retirar dinero de cuenta origen
        String sql1 = "UPDATE cuentas SET saldo = saldo - ? WHERE id = ?";
        try (PreparedStatement pstmt1 = conexion.prepareStatement(sql1)) {
            pstmt1.setDouble(1, cantidad);
            pstmt1.setInt(2, cuentaOrigen);
            pstmt1.executeUpdate();
        }
        
        // Agregar dinero a cuenta destino
        String sql2 = "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?";
        try (PreparedStatement pstmt2 = conexion.prepareStatement(sql2)) {
            pstmt2.setDouble(1, cantidad);
            pstmt2.setInt(2, cuentaDestino);
            pstmt2.executeUpdate();
        }
        
        conexion.commit();
        
    } catch (SQLException e) {
        conexion.rollback();
        throw e;
    } finally {
        conexion.setAutoCommit(true);
    }
}
```

### Puntos de Guardado (Savepoints)

```java
public void operacionCompleja() throws SQLException {
    conexion.setAutoCommit(false);
    Savepoint savepoint = null;
    
    try {
        // Operación 1
        ejecutarOperacion1();
        
        // Crear punto de guardado
        savepoint = conexion.setSavepoint("punto1");
        
        // Operación 2 (puede fallar)
        ejecutarOperacion2();
        
        conexion.commit();
        
    } catch (SQLException e) {
        if (savepoint != null) {
            conexion.rollback(savepoint);
        } else {
            conexion.rollback();
        }
        throw e;
    }
}
```

## Pool de Conexiones

### HikariCP (Recomendado)

```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>5.0.1</version>
</dependency>
```

```java
public class ConexionPool {
    private static HikariDataSource dataSource;
    
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mi_bd");
        config.setUsername("usuario");
        config.setPassword("contraseña");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }
}
```

## Manejo de Excepciones

### SQLException y sus Subclases

```java
public void manejarExcepciones() {
    try {
        // Operaciones de base de datos
        
    } catch (SQLTimeoutException e) {
        // Timeout en la consulta
        logger.error("Timeout en consulta: " + e.getMessage());
        
    } catch (SQLIntegrityConstraintViolationException e) {
        // Violación de restricción
        logger.error("Violación de integridad: " + e.getMessage());
        
    } catch (SQLSyntaxErrorException e) {
        // Error de sintaxis SQL
        logger.error("Error de sintaxis: " + e.getMessage());
        
    } catch (SQLException e) {
        // Otras excepciones SQL
        logger.error("Error SQL: " + e.getMessage());
        logger.error("Estado SQL: " + e.getSQLState());
        logger.error("Código de error: " + e.getErrorCode());
    }
}
```

## Metadatos

### Información de la Base de Datos

```java
public void obtenerMetadatos() throws SQLException {
    DatabaseMetaData metaData = conexion.getMetaData();
    
    System.out.println("Producto: " + metaData.getDatabaseProductName());
    System.out.println("Versión: " + metaData.getDatabaseProductVersion());
    System.out.println("Driver: " + metaData.getDriverName());
    
    // Obtener tablas
    ResultSet tables = metaData.getTables(null, null, "%", 
                                        new String[]{"TABLE"});
    while (tables.next()) {
        System.out.println("Tabla: " + tables.getString("TABLE_NAME"));
    }
}
```

### Información de ResultSet

```java
public void analizarResultSet(ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    
    for (int i = 1; i <= columnCount; i++) {
        System.out.println("Columna " + i + ":");
        System.out.println("  Nombre: " + rsmd.getColumnName(i));
        System.out.println("  Tipo: " + rsmd.getColumnTypeName(i));
        System.out.println("  Tamaño: " + rsmd.getColumnDisplaySize(i));
    }
}
```

## Mejores Prácticas

### 1. Uso de try-with-resources

```java
// ✅ Correcto
public void buenaPractica() throws SQLException {
    String sql = "SELECT * FROM usuarios WHERE id = ?";
    
    try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setInt(1, 123);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            // Procesar resultados
        }
    } // Recursos se cierran automáticamente
}

// ❌ Incorrecto
public void malaPractica() throws SQLException {
    PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM usuarios");
    ResultSet rs = pstmt.executeQuery();
    // Los recursos no se cierran -> memory leak
}
```

### 2. Prevención de SQL Injection

```java
// ✅ Correcto - PreparedStatement
public Usuario buscarUsuario(String nombre) throws SQLException {
    String sql = "SELECT * FROM usuarios WHERE nombre = ?";
    try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setString(1, nombre);
        // Código seguro
    }
}

// ❌ Incorrecto - Concatenación de strings
public Usuario buscarUsuarioMal(String nombre) throws SQLException {
    String sql = "SELECT * FROM usuarios WHERE nombre = '" + nombre + "'";
    // Vulnerable a SQL injection
}
```

### 3. Gestión de Conexiones

```java
// ✅ Usar pool de conexiones
public class DAO {
    public void operacion() throws SQLException {
        try (Connection conn = ConexionPool.obtenerConexion()) {
            // Usar conexión
        } // Se devuelve al pool automáticamente
    }
}

// ❌ Crear conexión cada vez
public void malaPractica() throws SQLException {
    Connection conn = DriverManager.getConnection(url, user, pass);
    // Costoso en recursos
}
```

### 4. Manejo de Transacciones

```java
public void transaccionSegura() throws SQLException {
    Connection conn = obtenerConexion();
    boolean autoCommitOriginal = conn.getAutoCommit();
    
    try {
        conn.setAutoCommit(false);
        
        // Operaciones de la transacción
        ejecutarOperacion1(conn);
        ejecutarOperacion2(conn);
        
        conn.commit();
        
    } catch (Exception e) {
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(autoCommitOriginal);
        conn.close();
    }
}
```

## Ejemplo Completo: DAO Pattern

```java
public class UsuarioDAO {
    private final DataSource dataSource;
    
    public UsuarioDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, email, edad FROM usuarios WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        }
    }
    
    public List<Usuario> buscarTodos() throws SQLException {
        String sql = "SELECT id, nombre, email, edad FROM usuarios ORDER BY nombre";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        
        return usuarios;
    }
    
    public int guardar(Usuario usuario) throws SQLException {
        if (usuario.getId() == 0) {
            return insertar(usuario);
        } else {
            actualizar(usuario);
            return usuario.getId();
        }
    }
    
    private int insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, edad) VALUES (?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, 
                 Statement.RETURN_GENERATED_KEYS)) {
            
            establecerParametros(pstmt, usuario);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("No se pudo obtener el ID generado");
            }
        }
    }
    
    private void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, edad = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            establecerParametros(pstmt, usuario);
            pstmt.setInt(4, usuario.getId());
            
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Usuario no encontrado con ID: " + usuario.getId());
            }
        }
    }
    
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setEmail(rs.getString("email"));
        usuario.setEdad(rs.getInt("edad"));
        return usuario;
    }
    
    private void establecerParametros(PreparedStatement pstmt, Usuario usuario) 
            throws SQLException {
        pstmt.setString(1, usuario.getNombre());
        pstmt.setString(2, usuario.getEmail());
        pstmt.setInt(3, usuario.getEdad());
    }
}
```

## Conclusión

JDBC es una tecnología fundamental para el desarrollo de aplicaciones Java que necesitan interactuar con bases de datos. Aunque existen alternativas más modernas como JPA/Hibernate, entender JDBC es crucial para:

- Optimizar consultas complejas
- Depurar problemas de rendimiento
- Trabajar con sistemas legacy
- Comprender el funcionamiento interno de los ORMs

La clave del éxito con JDBC está en seguir las mejores prácticas, gestionar adecuadamente los recursos y mantener la seguridad en las consultas SQL.