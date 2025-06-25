package com.example.paint.core;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterCommand {
    String value(); // clave del menú
    String description(); // descripción que aparecerá en el menú
}
