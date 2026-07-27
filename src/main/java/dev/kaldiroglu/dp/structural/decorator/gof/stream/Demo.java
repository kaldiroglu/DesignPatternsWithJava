package dev.kaldiroglu.dp.structural.decorator.gof.stream;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.problem.CompressingASCII7FileStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.problem.CompressingSocketStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.ASCII7Stream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.CompressingStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.FileStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.SocketStream;

/**
 * The Sample Code example of GoF pp. 182–184, run both ways.
 */
public final class Demo {

    // The "aä" is deliberate. Folding ä to a creates a run of two that compression can
    // exploit only if compression runs *after* folding — so the two orders of the same
    // two decorators produce different bytes. Order is part of the design, not a detail.
    private static final String PAYLOAD = "Rrrésumé of a naïve café —— aä ee ccc";

    private Demo() {
    }

    public static void run() {
        System.out.println("=".repeat(72));
        System.out.println("GoF Sample Code (pp. 182–184) — compressing and encoding a stream");
        System.out.println("=".repeat(72));
        System.out.println("\npayload: " + PAYLOAD);

        System.out.println("\n--- problem: one subclass per (destination x transformation) ------------");
        CompressingASCII7FileStream file = new CompressingASCII7FileStream();
        file.putString(PAYLOAD);
        file.close();
        System.out.println("CompressingASCII7FileStream -> " + file.contents());

        CompressingSocketStream socket = new CompressingSocketStream();
        socket.putString(PAYLOAD);
        socket.close();
        System.out.println("CompressingSocketStream     -> " + socket.contents());
        System.out.println("""
                Six classes exist to cover two destinations and two transformations, and
                the two bodies above are identical apart from their superclass. The pair
                in the other order would be two classes more.""");

        System.out.println("\n--- solution: two decorators over any destination -----------------------");
        FileStream fileSink = new FileStream();
        Stream chain = new CompressingStream(new ASCII7Stream(fileSink));
        chain.putString(PAYLOAD);
        chain.close();
        System.out.println("new CompressingStream(new ASCII7Stream(new FileStream()))  -> " + fileSink.contents());

        SocketStream socketSink = new SocketStream();
        Stream socketChain = new CompressingStream(socketSink);
        socketChain.putString(PAYLOAD);
        socketChain.close();
        System.out.println("new CompressingStream(new SocketStream())                  -> " + socketSink.contents());

        FileStream reversedSink = new FileStream();
        Stream reversed = new ASCII7Stream(new CompressingStream(reversedSink));
        reversed.putString(PAYLOAD);
        reversed.close();
        System.out.println("new ASCII7Stream(new CompressingStream(new FileStream()))  -> " + reversedSink.contents());
        System.out.println("""
                Four classes cover every destination, every transformation and every
                order — including the order in the last line, which nobody had to
                anticipate when the decorators were written.

                Compare the last two file results: folding "aä" to "aa" creates a run
                that compression can only exploit if it runs after the folding, so the
                same two decorators in the other order produce different bytes. Under
                subclassing, that second order is another class; here it is another
                pair of parentheses.""");
    }

    public static void main(String[] args) {
        run();
    }
}
