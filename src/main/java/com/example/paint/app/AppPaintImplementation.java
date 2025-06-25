package com.example.paint.app;

import com.example.paint.canvas.Canvas;
import com.example.paint.commands.CommandContext;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;


public class AppPaintImplementation implements AppPaint {
    
    private Boolean isRunning;
    private CommandContext context = null;
    public AppPaintImplementation(InputReader reader, InputWriter writer, Canvas canvas) {        
        this.context = new CommandContext(
            canvas, 
            reader, 
            writer, ()->isRunning=false);

    }
    @Override
    public void run() {
        isRunning =true;
        //registrar los shapes
        //registar los commands
        while (isRunning) {
            //limpiar la consola
            //pintar el menu
            //obtener la entrada de menu
            //comprobar que el comando existe
            //conmmand.execute()
            //limpiar la consola
        }
        context.getScanner().close();
        
    }
    
    
}
