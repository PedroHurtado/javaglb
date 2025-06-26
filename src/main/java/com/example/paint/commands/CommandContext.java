package com.example.paint.commands;


import com.example.paint.canvas.Canvas;
import com.example.paint.core.InputReader;
import com.example.paint.core.InputWriter;

import lombok.Getter;

@Getter
public class CommandContext {
    private final Canvas canvas;
    private final InputReader scanner;
    private final Runnable onExit;
    private final InputWriter out;
    public CommandContext(Canvas canvas, InputReader scanner, InputWriter out, Runnable onExit) {
        this.canvas = canvas;
        this.scanner = scanner;
        this.out = out;
        this.onExit = onExit;
    
    }   
}

/*public record CommandContext(Canvas canvas, InputReader scanner, InputWriter out, Runnable onExit) {
}
 context.getCanvas() No
 context.cancas()
*/

