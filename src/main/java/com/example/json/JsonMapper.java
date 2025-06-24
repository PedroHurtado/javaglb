package com.example.json;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.example.paint.shapes.Circle;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;



public   class JsonMapper {  
    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();

        
        mapper.addMixIn(Shape.class, ShapeMixIn.class);

        
        mapper.registerSubtypes(
            new NamedType(Circle.class, "circle"),
            new NamedType(Rectangle.class, "rectangle")
        );

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("com.example.")
            .allowIfSubType(Shape.class)
            .build();
        
        // Configurar el resolver para usar "type" como propiedad
        StdTypeResolverBuilder typer = new StdTypeResolverBuilder()
            .init(JsonTypeInfo.Id.NAME, null)
            .inclusion(JsonTypeInfo.As.PROPERTY)
            .typeProperty("type"); // AQUÍ especificamos "type" en lugar de "@class"
        
        mapper.setDefaultTyping(typer);
        mapper.setPolymorphicTypeValidator(ptv);
        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        

        return mapper;
    }
}
