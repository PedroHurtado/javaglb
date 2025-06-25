package com.example.paint.commands.interfaces;



import com.example.paint.commands.Command;
import com.example.paint.commands.CommandAddShape;
import com.example.paint.commands.CommandContext;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;
import com.example.paint.core.RegisterCommand;
import com.example.paint.core.ShapeRegistry;
import com.example.paint.shapes.Shape;

@RegisterCommand(value = "add", description = "Agregar una figura")
public class CommandFactoryAddShape implements CommandFactory {

    @Override
    public Command create(CommandContext context) {
        InputReader scanner = context.getScanner();
        InputWriter writer = context.getOut();
        writer.println("Figuras disponibles: " + ShapeRegistry.getAvailableShapes());
        writer.print("Selecciona una figura: ");
        String tipo = scanner.nextLine();        
        var factory = ShapeRegistry.getFactory(tipo);
        if (factory == null) {
            writer.println("Figura no encontrada.");
            return () -> {}; // Comando vacío
        }
        Shape shape = factory.get();
        return new CommandAddShape(context.getCanvas(), shape);
    }
    
}
