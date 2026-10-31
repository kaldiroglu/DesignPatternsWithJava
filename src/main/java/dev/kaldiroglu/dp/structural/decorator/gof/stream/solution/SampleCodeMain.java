package dev.kaldiroglu.dp.structural.decorator.gof.stream.solution;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.Stream;

/** The client code of GoF p. 184: one chain over a file, one decorator over a socket. */
public final class SampleCodeMain {

    private SampleCodeMain() {
    }

    public static void main(String[] args) {
        FileStream file = new FileStream();

        Stream out =
                new CompressingStream(
                        new ASCII7Stream(file));

        out.putInt(12);
        out.putString(" aaa café");
        out.close();
        System.out.println("file now holds   : " + file.contents());

        SocketStream socket = new SocketStream();
        Stream sameDecorator = new CompressingStream(socket);
        sameDecorator.putInt(12);
        sameDecorator.putString(" aaa café");
        sameDecorator.close();
        System.out.println("socket now holds : " + socket.contents());
    }
}
