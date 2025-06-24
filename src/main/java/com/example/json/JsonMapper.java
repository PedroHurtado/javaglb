package com.example.json;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.example.paint.shapes.Circle;
import com.example.paint.shapes.Rectangle;
import com.example.paint.shapes.Shape;
import com.fasterxml.jackson.databind.jsontype.NamedType;




public   class JsonMapper {  
    public static ObjectMapper create() {
        ObjectMapper mapper = new ObjectMapper();

        
        mapper.addMixIn(Shape.class, ShapeMixIn.class);

        
        mapper.registerSubtypes(
            new NamedType(Circle.class, "circle"),
            new NamedType(Rectangle.class, "rectangle")
        );        
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        

        return mapper;
    }
}
