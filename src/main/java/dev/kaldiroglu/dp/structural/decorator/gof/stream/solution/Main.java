package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Payload;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

/** The Sample Code example of GoF pp. 182–184, by decorating. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("--- solution: two decorators over any destination -----------------------");
        FileStream fileSink = new FileStream();
        Stream chain = new CompressingStream(new ASCII7Stream(fileSink));
        chain.putString(Payload.TEXT);
        chain.close();
        System.out.println("new CompressingStream(new ASCII7Stream(new FileStream()))  -> " + fileSink.contents());

        SocketStream socketSink = new SocketStream();
        Stream socketChain = new CompressingStream(socketSink);
        socketChain.putString(Payload.TEXT);
        socketChain.close();
        System.out.println("new CompressingStream(new SocketStream())                  -> " + socketSink.contents());

        FileStream reversedSink = new FileStream();
        Stream reversed = new ASCII7Stream(new CompressingStream(reversedSink));
        reversed.putString(Payload.TEXT);
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
}
