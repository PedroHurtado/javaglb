# Guía Completa de Localización en Java

## Introducción

La localización (i18n - internationalization) en Java permite crear aplicaciones que soporten múltiples idiomas y regiones usando `ResourceBundle` y archivos de propiedades.

## Estructura de Archivos de Recursos

### 1. Ubicación de Archivos
Los archivos de recursos se ubican típicamente en:
```
src/main/resources/
├── messages.properties (idioma por defecto)
├── messages_es.properties (español)
├── messages_en.properties (inglés)
├── messages_fr.properties (francés)
└── messages_de.properties (alemán)
```

### 2. Contenido de los Archivos de Propiedades

**messages.properties** (por defecto - inglés)
```properties
# Mensajes generales
app.title=My Application
app.welcome=Welcome to our application
app.goodbye=Goodbye!

# Formularios
form.name=Name
form.email=Email
form.submit=Submit
form.cancel=Cancel

# Mensajes de error
error.required=This field is required
error.email.invalid=Please enter a valid email address
error.server=Server error occurred

# Mensajes de éxito
success.saved=Data saved successfully
success.deleted=Item deleted successfully

# Números y fechas
currency.symbol=$
date.format=MM/dd/yyyy
```

**messages_es.properties** (español)
```properties
# Mensajes generales
app.title=Mi Aplicación
app.welcome=Bienvenido a nuestra aplicación
app.goodbye=¡Adiós!

# Formularios
form.name=Nombre
form.email=Correo electrónico
form.submit=Enviar
form.cancel=Cancelar

# Mensajes de error
error.required=Este campo es obligatorio
error.email.invalid=Por favor ingrese un email válido
error.server=Ocurrió un error en el servidor

# Mensajes de éxito
success.saved=Datos guardados exitosamente
success.deleted=Elemento eliminado exitosamente

# Números y fechas
currency.symbol=€
date.format=dd/MM/yyyy
```

**messages_fr.properties** (francés)
```properties
# Mensajes generales
app.title=Mon Application
app.welcome=Bienvenue dans notre application
app.goodbye=Au revoir!

# Formularios
form.name=Nom
form.email=Email
form.submit=Soumettre
form.cancel=Annuler

# Mensajes de error
error.required=Ce champ est obligatoire
error.email.invalid=Veuillez saisir une adresse email valide
error.server=Une erreur serveur s'est produite

# Mensajes de éxito
success.saved=Données sauvegardées avec succès
success.deleted=Élément supprimé avec succès

# Números y fechas
currency.symbol=€
date.format=dd/MM/yyyy
```

## Implementación en Código Java

### 1. Clase Utilitaria para Localización

```java
package com.example.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static final String BUNDLE_NAME = "messages";
    private static LocalizationManager instance;
    private ResourceBundle resourceBundle;
    private Locale currentLocale;
    
    private LocalizationManager() {
        // Idioma por defecto
        setLocale(Locale.getDefault());
    }
    
    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }
    
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }
    
    public String getMessage(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!"; // Indicador de clave no encontrada
        }
    }
    
    public String getMessage(String key, Object... params) {
        try {
            String message = resourceBundle.getString(key);
            return MessageFormat.format(message, params);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }
    
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    
    // Métodos de conveniencia para idiomas específicos
    public void setSpanish() {
        setLocale(new Locale("es", "ES"));
    }
    
    public void setEnglish() {
        setLocale(new Locale("en", "US"));
    }
    
    public void setFrench() {
        setLocale(new Locale("fr", "FR"));
    }
    
    public void setGerman() {
        setLocale(new Locale("de", "DE"));
    }
}
```

### 2. Ejemplo de Uso Básico

```java
package com.example.demo;

import com.example.i18n.LocalizationManager;
import java.util.Locale;

public class BasicLocalizationExample {
    
    public static void main(String[] args) {
        LocalizationManager i18n = LocalizationManager.getInstance();
        
        // Usar idioma por defecto
        System.out.println("=== Idioma por defecto ===");
        printMessages(i18n);
        
        // Cambiar a español
        System.out.println("\n=== Español ===");
        i18n.setSpanish();
        printMessages(i18n);
        
        // Cambiar a francés
        System.out.println("\n=== Français ===");
        i18n.setFrench();
        printMessages(i18n);
        
        // Ejemplo con parámetros
        System.out.println("\n=== Mensajes con parámetros ===");
        demonstrateParameterizedMessages(i18n);
    }
    
    private static void printMessages(LocalizationManager i18n) {
        System.out.println("Título: " + i18n.getMessage("app.title"));
        System.out.println("Bienvenida: " + i18n.getMessage("app.welcome"));
        System.out.println("Nombre: " + i18n.getMessage("form.name"));
        System.out.println("Email: " + i18n.getMessage("form.email"));
        System.out.println("Error requerido: " + i18n.getMessage("error.required"));
    }
    
    private static void demonstrateParameterizedMessages(LocalizationManager i18n) {
        // Agregar estas claves a los archivos de propiedades:
        // welcome.user=Welcome, {0}!
        // items.count=You have {0} items in your cart
        
        String userName = "Juan";
        int itemCount = 5;
        
        System.out.println(i18n.getMessage("welcome.user", userName));
        System.out.println(i18n.getMessage("items.count", itemCount));
    }
}
```

### 3. Ejemplo con Aplicación Swing

```java
package com.example.swing;

import com.example.i18n.LocalizationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocalizedSwingApp extends JFrame {
    private LocalizationManager i18n;
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JTextField nameField;
    private JTextField emailField;
    private JButton submitButton;
    private JButton cancelButton;
    private JComboBox<String> languageCombo;
    
    public LocalizedSwingApp() {
        i18n = LocalizationManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateTexts();
    }
    
    private void initializeComponents() {
        titleLabel = new JLabel();
        nameLabel = new JLabel();
        emailLabel = new JLabel();
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        submitButton = new JButton();
        cancelButton = new JButton();
        
        languageCombo = new JComboBox<>(new String[]{
            "English", "Español", "Français", "Deutsch"
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con título y selector de idioma
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(titleLabel);
        topPanel.add(new JLabel("Language:"));
        topPanel.add(languageCombo);
        
        // Panel central con formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        languageCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = languageCombo.getSelectedIndex();
                switch (selectedIndex) {
                    case 0: i18n.setEnglish(); break;
                    case 1: i18n.setSpanish(); break;
                    case 2: i18n.setFrench(); break;
                    case 3: i18n.setGerman(); break;
                }
                updateTexts();
            }
        });
        
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    i18n.getMessage("error.required"));
                return;
            }
            
            JOptionPane.showMessageDialog(this, 
                i18n.getMessage("success.saved"));
        });
        
        cancelButton.addActionListener(e -> {
            nameField.setText("");
            emailField.setText("");
        });
    }
    
    private void updateTexts() {
        setTitle(i18n.getMessage("app.title"));
        titleLabel.setText(i18n.getMessage("app.welcome"));
        nameLabel.setText(i18n.getMessage("form.name") + ":");
        emailLabel.setText(i18n.getMessage("form.email") + ":");
        submitButton.setText(i18n.getMessage("form.submit"));
        cancelButton.setText(i18n.getMessage("form.cancel"));
        
        // Actualizar la ventana
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            LocalizedSwingApp app = new LocalizedSwingApp();
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.pack();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}
```

### 4. Ejemplo con Spring Boot

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocalizationConfig implements WebMvcConfigurer {
    
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
```

```java
package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class HomeController {
    
    @Autowired
    private MessageSource messageSource;
    
    @GetMapping("/")
    public String home(Model model, Locale locale) {
        model.addAttribute("title", messageSource.getMessage("app.title", null, locale));
        model.addAttribute("welcome", messageSource.getMessage("app.welcome", null, locale));
        return "index";
    }
    
    @GetMapping("/form")
    public String form(Model model, Locale locale) {
        model.addAttribute("nameLabel", messageSource.getMessage("form.name", null, locale));
        model.addAttribute("emailLabel", messageSource.getMessage("form.email", null, locale));
        model.addAttribute("submitButton", messageSource.getMessage("form.submit", null, locale));
        return "form";
    }
}
```

## Formateo de Números, Fechas y Monedas

```java
package com.example.formatting;

import java.text.NumberFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Currency;

public class LocalizedFormattingExample {
    
    public static void main(String[] args) {
        demonstrateNumberFormatting();
        demonstrateDateFormatting();
        demonstrateCurrencyFormatting();
    }
    
    private static void demonstrateNumberFormatting() {
        double number = 1234567.89;
        
        System.out.println("=== Formateo de Números ===");
        
        Locale[] locales = {
            Locale.US, 
            new Locale("es", "ES"), 
            Locale.FRANCE, 
            Locale.GERMANY
        };
        
        for (Locale locale : locales) {
            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            System.out.println(locale.getDisplayName() + ": " + nf.format(number));
        }
    }
    
    private static void demonstrateDateFormatting() {
        Date now = new Date();
        
        System.out.println("\n=== Formateo de Fechas ===");
        
        Locale[] locales = {
            Locale.US, 
            new Locale("es", "ES"), 
            Locale.FRANCE, 
            Locale.GERMANY
        };
        
        for (Locale locale : locales) {
            DateFormat df = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.SHORT, locale);
            System.out.println(locale.getDisplayName() + ": " + df.format(now));
        }
    }
    
    private static void demonstrateCurrencyFormatting() {
        double amount = 1234.56;
        
        System.out.println("\n=== Formateo de Monedas ===");
        
        Locale[] locales = {
            Locale.US, 
            new Locale("es", "ES"), 
            Locale.FRANCE, 
            Locale.GERMANY
        };
        
        for (Locale locale : locales) {
            NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
            System.out.println(locale.getDisplayName() + ": " + cf.format(amount));
        }
    }
}
```

## Mejores Prácticas

### 1. Organización de Claves
- Usa una convención consistente para nombrar claves
- Agrupa claves por funcionalidad (form.*, error.*, success.*)
- Evita claves muy largas o muy cortas

### 2. Manejo de Caracteres Especiales
```properties
# Para caracteres especiales, usa Unicode escape sequences
message.with.special=Caf\u00e9 con leche
# O configura el encoding UTF-8 en tu IDE/build tool
```

### 3. Valores por Defecto
```java
public String getMessageWithDefault(String key, String defaultValue) {
    try {
        return resourceBundle.getString(key);
    } catch (Exception e) {
        return defaultValue;
    }
}
```

### 4. Lazy Loading para Mejor Performance
```java
public class OptimizedLocalizationManager {
    private final Map<Locale, ResourceBundle> bundleCache = new ConcurrentHashMap<>();
    
    private ResourceBundle getBundle(Locale locale) {
        return bundleCache.computeIfAbsent(locale, 
            l -> ResourceBundle.getBundle(BUNDLE_NAME, l));
    }
}
```

## Estructura de Proyecto Maven

```xml
<project>
    <dependencies>
        <!-- Para aplicaciones Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Para aplicaciones Swing -->
        <dependency>
            <groupId>com.formdev</groupId>
            <artifactId>flatlaf</artifactId>
            <version>3.2.5</version>
        </dependency>
    </dependencies>
    
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>false</filtering>
                <includes>
                    <include>**/*.properties</include>
                </includes>
            </resource>
        </resources>
    </build>
</project>
```

## Conclusión

La localización en Java proporciona una manera robusta de crear aplicaciones multiidioma. Los componentes clave son:

1. **ResourceBundle**: Para cargar mensajes localizados
2. **Locale**: Para especificar idioma y región
3. **MessageFormat**: Para mensajes con parámetros
4. **NumberFormat/DateFormat**: Para formateo localizado

Esta implementación te permite crear aplicaciones verdaderamente internacionales que se adapten automáticamente al idioma y región del usuario.