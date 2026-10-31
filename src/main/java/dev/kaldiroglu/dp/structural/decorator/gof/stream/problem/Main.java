package dev.kaldiroglu.dp.structural.decorator.gof.stream.problem;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Payload;

/** The Sample Code example of GoF pp. 182–184, by subclassing. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("--- problem: one subclass per (destination x transformation) ------------");
        CompressingASCII7FileStream file = new CompressingASCII7FileStream();
        file.putString(Payload.TEXT);
        file.close();
        System.out.println("CompressingASCII7FileStream -> " + file.contents());

        CompressingSocketStream socket = new CompressingSocketStream();
        socket.putString(Payload.TEXT);
        socket.close();
        System.out.println("CompressingSocketStream     -> " + socket.contents());
        System.out.println("""
                Six classes exist to cover two destinations and two transformations, and
                the two bodies above are identical apart from their superclass. The pair
                in the other order would be two classes more.""");
    }
}
