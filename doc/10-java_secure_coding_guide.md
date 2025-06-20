# Guía de Secure Coding en Java

## Índice
1. [Introducción](#introducción)
2. [Validación de Entrada](#validación-de-entrada)
3. [Manejo Seguro de Strings](#manejo-seguro-de-strings)
4. [Gestión de Memoria y Recursos](#gestión-de-memoria-y-recursos)
5. [Criptografía](#criptografía)
6. [Manejo de Excepciones](#manejo-de-excepciones)
7. [Seguridad en Bases de Datos](#seguridad-en-bases-de-datos)
8. [Serialización Segura](#serialización-segura)
9. [Logging Seguro](#logging-seguro)
10. [Configuración y Deployment](#configuración-y-deployment)

---

## Introducción

La programación segura en Java requiere adoptar prácticas que prevengan vulnerabilidades comunes como inyección de código, desbordamientos de buffer, exposición de datos sensibles y ataques de denegación de servicio.

### Principios Fundamentales
- **Defensa en profundidad**: Múltiples capas de seguridad
- **Principio de menor privilegio**: Acceso mínimo necesario
- **Fail securely**: Fallar de forma segura
- **Validación completa de entrada**: No confiar en datos externos

---

## Validación de Entrada

### ❌ Código Inseguro
```java
public class InsecureInput {
    public void processUserInput(String input) {
        // Peligroso: Sin validación
        System.out.println("Processing: " + input);
        Runtime.getRuntime().exec(input); // Command injection vulnerability
    }
}
```

### ✅ Código Seguro
```java
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;

public class SecureInput {
    private static final Pattern ALPHANUMERIC = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final List<String> ALLOWED_COMMANDS = Arrays.asList("list", "help", "status");
    
    public boolean validateInput(String input) {
        // Validación de nulos
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        // Validación de longitud
        if (input.length() > 100) {
            return false;
        }
        
        // Validación de formato
        return ALPHANUMERIC.matcher(input).matches();
    }
    
    public void processUserInput(String input) {
        if (!validateInput(input)) {
            throw new IllegalArgumentException("Invalid input format");
        }
        
        // Whitelist de comandos permitidos
        if (!ALLOWED_COMMANDS.contains(input.toLowerCase())) {
            throw new SecurityException("Command not allowed");
        }
        
        System.out.println("Processing: " + input);
    }
}
```

### Validador de Email Seguro
```java
import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.length() > 254) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
```

---

## Manejo Seguro de Strings

### Datos Sensibles con char[]
```java
import java.util.Arrays;

public class SecureStringHandling {
    
    // ❌ Inseguro: String inmutable en memoria
    public boolean authenticateInsecure(String username, String password) {
        // Password queda en memoria hasta GC
        return checkCredentials(username, password);
    }
    
    // ✅ Seguro: char[] se puede limpiar
    public boolean authenticateSecure(String username, char[] password) {
        try {
            return checkCredentials(username, new String(password));
        } finally {
            // Limpiar password de memoria
            Arrays.fill(password, '\0');
        }
    }
    
    private boolean checkCredentials(String username, String password) {
        // Lógica de autenticación
        return "admin".equals(username) && "secret123".equals(password);
    }
}
```

### StringBuilder para Construcción Dinámica
```java
public class SecureStringBuilder {
    
    public String buildSafeQuery(String tableName, List<String> columns) {
        // Validar nombre de tabla
        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Invalid table name");
        }
        
        StringBuilder query = new StringBuilder("SELECT ");
        
        // Validar y construir columnas
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            if (!isValidColumnName(column)) {
                throw new IllegalArgumentException("Invalid column name: " + column);
            }
            
            query.append(column);
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        
        query.append(" FROM ").append(tableName);
        return query.toString();
    }
    
    private boolean isValidTableName(String name) {
        return name != null && name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
    
    private boolean isValidColumnName(String name) {
        return name != null && name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
}
```

---

## Gestión de Memoria y Recursos

### Try-with-Resources
```java
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecureResourceManagement {
    
    // ✅ Gestión automática de recursos
    public String readFileSecurely(String filename) throws IOException {
        // Validar path
        if (!isValidFilePath(filename)) {
            throw new SecurityException("Invalid file path");
        }
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            return content.toString();
        }
        // Recursos se cierran automáticamente
    }
    
    public void writeFileSecurely(String filename, String content) throws IOException {
        if (!isValidFilePath(filename)) {
            throw new SecurityException("Invalid file path");
        }
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write(content);
        }
    }
    
    private boolean isValidFilePath(String path) {
        // Prevenir path traversal
        return path != null && 
               !path.contains("..") && 
               !path.startsWith("/") && 
               path.matches("^[a-zA-Z0-9._-]+$");
    }
}
```

---

## Criptografía

### Hashing Seguro de Passwords
```java
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public class SecurePasswordHashing {
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 32;
    
    public static class HashedPassword {
        private final String hash;
        private final String salt;
        
        public HashedPassword(String hash, String salt) {
            this.hash = hash;
            this.salt = salt;
        }
        
        public String getHash() { return hash; }
        public String getSalt() { return salt; }
    }
    
    public static HashedPassword hashPassword(char[] password) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        // Generar salt aleatorio
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        
        // Hash con PBKDF2
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        // Limpiar password de memoria
        spec.clearPassword();
        
        return new HashedPassword(
            Base64.getEncoder().encodeToString(hash),
            Base64.getEncoder().encodeToString(salt)
        );
    }
    
    public static boolean verifyPassword(char[] password, String storedHash, String storedSalt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        spec.clearPassword();
        
        String computedHash = Base64.getEncoder().encodeToString(hash);
        return storedHash.equals(computedHash);
    }
}
```

### Cifrado AES
```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class SecureAESEncryption {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256); // AES-256
        return keyGenerator.generateKey();
    }
    
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        // Generar IV aleatorio
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        
        byte[] encryptedData = cipher.doFinal(plaintext.getBytes());
        
        // Combinar IV + datos cifrados
        byte[] result = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encryptedData, 0, result, iv.length, encryptedData.length);
        
        return Base64.getEncoder().encodeToString(result);
    }
    
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        
        // Extraer IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(decodedData, 0, iv, 0, iv.length);
        
        // Extraer datos cifrados
        byte[] cipherText = new byte[decodedData.length - GCM_IV_LENGTH];
        System.arraycopy(decodedData, GCM_IV_LENGTH, cipherText, 0, cipherText.length);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        
        byte[] plaintext = cipher.doFinal(cipherText);
        return new String(plaintext);
    }
}
```

---

## Manejo de Excepciones

### Manejo Seguro de Excepciones
```java
import java.util.logging.Logger;
import java.util.logging.Level;

public class SecureExceptionHandling {
    private static final Logger logger = Logger.getLogger(SecureExceptionHandling.class.getName());
    
    // ❌ Inseguro: Expone información sensible
    public void processPaymentInsecure(String cardNumber, double amount) {
        try {
            // Lógica de procesamiento
            processPayment(cardNumber, amount);
        } catch (Exception e) {
            // Peligroso: Podría exponer datos sensibles
            throw new RuntimeException("Payment failed: " + e.getMessage(), e);
        }
    }
    
    // ✅ Seguro: Manejo controlado de errores
    public PaymentResult processPaymentSecure(String cardNumber, double amount) {
        try {
            validatePaymentData(cardNumber, amount);
            processPayment(cardNumber, amount);
            return PaymentResult.success();
            
        } catch (ValidationException e) {
            logger.log(Level.WARNING, "Validation error in payment processing", e);
            return PaymentResult.error("Invalid payment data");
            
        } catch (PaymentException e) {
            logger.log(Level.SEVERE, "Payment processing failed", e);
            return PaymentResult.error("Payment could not be processed");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error in payment processing", e);
            return PaymentResult.error("System error occurred");
        }
    }
    
    private void validatePaymentData(String cardNumber, double amount) throws ValidationException {
        if (cardNumber == null || cardNumber.length() < 13) {
            throw new ValidationException("Invalid card number");
        }
        if (amount <= 0) {
            throw new ValidationException("Invalid amount");
        }
    }
    
    private void processPayment(String cardNumber, double amount) throws PaymentException {
        // Lógica de procesamiento
    }
    
    // Clases de excepción personalizadas
    public static class ValidationException extends Exception {
        public ValidationException(String message) { super(message); }
    }
    
    public static class PaymentException extends Exception {
        public PaymentException(String message) { super(message); }
    }
    
    public static class PaymentResult {
        private final boolean success;
        private final String message;
        
        private PaymentResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public static PaymentResult success() {
            return new PaymentResult(true, "Payment processed successfully");
        }
        
        public static PaymentResult error(String message) {
            return new PaymentResult(false, message);
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
```

---

## Seguridad en Bases de Datos

### Prevención de SQL Injection
```java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecureDatabaseAccess {
    
    // ❌ Vulnerable a SQL Injection
    public List<User> getUsersInsecure(Connection conn, String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return mapResultSetToUsers(rs);
    }
    
    // ✅ Seguro con PreparedStatement
    public List<User> getUsersSecure(Connection conn, String username) throws SQLException {
        String sql = "SELECT id, username, email, created_at FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSetToUsers(rs);
            }
        }
    }
    
    // Inserción segura con transacciones
    public boolean createUserSecure(Connection conn, String username, String email, char[] password) {
        String sql = "INSERT INTO users (username, email, password_hash, salt, created_at) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Hash del password
            SecurePasswordHashing.HashedPassword hashed = 
                SecurePasswordHashing.hashPassword(password);
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, hashed.getHash());
                stmt.setString(4, hashed.getSalt());
                stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected == 1) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                // Log rollback error
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // Log error
            }
        }
    }
    
    private List<User> mapResultSetToUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            users.add(user);
        }
        
        return users;
    }
    
    // Clase User
    public static class User {
        private Long id;
        private String username;
        private String email;
        private Timestamp createdAt;
        
        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public Timestamp getCreatedAt() { return createdAt; }
        public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    }
}
```

---

## Serialización Segura

### Prevención de Deserialization Attacks
```java
import java.io.*;
import java.util.Set;
import java.util.HashSet;

public class SecureSerialization {
    
    // Whitelist de clases permitidas
    private static final Set<String> ALLOWED_CLASSES = Set.of(
        "java.lang.String",
        "java.lang.Integer",
        "java.lang.Long",
        "java.util.ArrayList",
        "com.example.SafeDataClass"
    );
    
    // ✅ ObjectInputStream seguro con filtrado
    public static class SecureObjectInputStream extends ObjectInputStream {
        
        public SecureObjectInputStream(InputStream in) throws IOException {
            super(in);
        }
        
        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String className = desc.getName();
            
            // Verificar whitelist
            if (!ALLOWED_CLASSES.contains(className)) {
                throw new SecurityException("Deserialization of class not allowed: " + className);
            }
            
            return super.resolveClass(desc);
        }
    }
    
    // Serialización segura
    public static byte[] serializeObject(Serializable obj) throws IOException {
        if (!isAllowedClass(obj.getClass().getName())) {
            throw new SecurityException("Serialization of class not allowed: " + obj.getClass().getName());
        }
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            
            oos.writeObject(obj);
            return baos.toByteArray();
        }
    }
    
    // Deserialización segura
    public static Object deserializeObject(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             SecureObjectInputStream ois = new SecureObjectInputStream(bais)) {
            
            return ois.readObject();
        }
    }
    
    private static boolean isAllowedClass(String className) {
        return ALLOWED_CLASSES.contains(className);
    }
    
    // Clase de datos segura para serialización
    public static class SafeDataClass implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private int value;
        
        public SafeDataClass(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        // Validación durante deserialización
        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            
            // Validar datos después de deserialización
            if (name == null || name.length() > 100) {
                throw new InvalidObjectException("Invalid name");
            }
            
            if (value < 0 || value > 1000000) {
                throw new InvalidObjectException("Invalid value");
            }
        }
        
        public String getName() { return name; }
        public int getValue() { return value; }
    }
}
```

---

## Logging Seguro

### Logging sin Exposición de Datos Sensibles
```java
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class SecureLogging {
    private static final Logger logger = Logger.getLogger(SecureLogging.class.getName());
    
    // Patrones para datos sensibles
    private static final Pattern CREDIT_CARD = Pattern.compile("\\b\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}\\b");
    private static final Pattern SSN = Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b");
    private static final Pattern EMAIL = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    
    public static String sanitizeForLogging(String message) {
        if (message == null) return null;
        
        String sanitized = message;
        
        // Enmascarar números de tarjeta de crédito
        sanitized = CREDIT_CARD.matcher(sanitized).replaceAll("****-****-****-****");
        
        // Enmascarar SSN
        sanitized = SSN.matcher(sanitized).replaceAll("***-**-****");
        
        // Enmascarar emails (parcialmente)
        sanitized = EMAIL.matcher(sanitized).replaceAll(matchResult -> {
            String email = matchResult.group();
            int atIndex = email.indexOf('@');
            if (atIndex > 2) {
                return email.substring(0, 2) + "***@" + email.substring(atIndex + 1);
            }
            return "***@" + email.substring(atIndex + 1);
        });
        
        return sanitized;
    }
    
    public void logUserAction(String username, String action, String details) {
        String sanitizedDetails = sanitizeForLogging(details);
        logger.info(String.format("User action - Username: %s, Action: %s, Details: %s", 
                                username, action, sanitizedDetails));
    }
    
    public void logSecurityEvent(String eventType, String source, String details) {
        String sanitizedDetails = sanitizeForLogging(details);
        logger.warning(String.format("Security event - Type: %s, Source: %s, Details: %s", 
                                   eventType, source, sanitizedDetails));
    }
    
    public void logError(String operation, Throwable error) {
        // Log error sin stack trace completo en producción
        String errorMessage = error.getMessage();
        String sanitizedMessage = sanitizeForLogging(errorMessage);
        
        logger.severe(String.format("Operation failed - Operation: %s, Error: %s", 
                                  operation, sanitizedMessage));
        
        // En desarrollo, incluir stack trace
        if (isDevelopmentMode()) {
            logger.log(Level.SEVERE, "Full stack trace:", error);
        }
    }
    
    private boolean isDevelopmentMode() {
        return "development".equals(System.getProperty("environment"));
    }
}
```

---

## Configuración y Deployment

### Configuración Segura
```java
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class SecureConfiguration {
    private final Properties config;
    private final Properties secrets;
    
    public SecureConfiguration() throws IOException {
        this.config = loadConfiguration();
        this.secrets = loadSecrets();
    }
    
    private Properties loadConfiguration() throws IOException {
        Properties props = new Properties();
        
        // Cargar configuración desde classpath
        try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
            }
        }
        
        return props;
    }
    
    private Properties loadSecrets() throws IOException {
        Properties props = new Properties();
        
        // En producción, cargar desde variables de entorno o sistema seguro
        String dbPassword = System.getenv("DB_PASSWORD");
        String apiKey = System.getenv("API_KEY");
        String jwtSecret = System.getenv("JWT_SECRET");
        
        if (dbPassword != null) props.setProperty("db.password", dbPassword);
        if (apiKey != null) props.setProperty("api.key", apiKey);
        if (jwtSecret != null) props.setProperty("jwt.secret", jwtSecret);
        
        return props;
    }
    
    public String getConfigValue(String key) {
        return config.getProperty(key);
    }
    
    public String getSecret(String key) {
        String value = secrets.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Required secret not found: " + key);
        }
        return value;
    }
    
    public int getIntValue(String key, int defaultValue) {
        String value = config.getProperty(key);
        if (value == null) return defaultValue;
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = config.getProperty(key);
        if (value == null) return defaultValue;
        
        return Boolean.parseBoolean(value);
    }
}
```

### Security Headers para Web Applications
```java
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Prevenir clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY");
        
        // Prevenir MIME type sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // Habilitar XSS protection
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Strict Transport Security (HTTPS)
        httpResponse.setHeader("Strict-Transport-Security", 
                             "max-age=31536000; includeSubDomains");
        
        // Content Security Policy
        httpResponse.setHeader("Content-Security-Policy", 
                             "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'");
        
        // Referrer Policy
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        chain.doFilter(request, response);
    }
}
```

---

## Checklist de Seguridad

### ✅ Validación de Entrada
- [ ] Validar todos los datos de entrada
- [ ] Usar whitelists en lugar de blacklists
- [ ] Implementar límites de longitud
- [ ] Validar tipos de datos y formatos
- [ ] Sanitizar datos antes de procesamiento

### ✅ Manejo de Datos Sensibles
- [ ] Usar char[] para passwords
- [ ] Implementar hashing seguro (PBKDF2, bcrypt, scrypt)
- [ ] Cifrar datos sensibles en reposo
- [ ] Limpiar datos sensibles de memoria

### ✅ Base de Datos
- [ ] Usar PreparedStatements
- [ ] Implementar principio de menor privilegio
- [ ] Validar permisos de acceso
- [ ] Usar transacciones apropiadamente

### ✅ Manejo de Errores
- [ ] No exponer información sensible en errores
- [ ] Implementar logging seguro
- [ ] Manejar excepciones apropiadamente
- [ ] Fallar de forma segura

### ✅ Criptografía
- [ ] Usar algoritmos aprobados
- [ ] Generar claves seguras
- [ ] Manejar IVs correctamente
- [ ] No implementar criptografía propia

