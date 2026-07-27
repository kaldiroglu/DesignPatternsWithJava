package dev.kaldiroglu.dp.structural.decorator.gof.stream;

import dev.kaldiroglu.dp.structural.decorator.gof.stream.problem.CompressingASCII7FileStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.problem.CompressingSocketStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.ASCII7Stream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.CompressingStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.FileStream;
import dev.kaldiroglu.dp.structural.decorator.gof.stream.solution.SocketStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StreamTest {

    private static final String PAYLOAD = "aaa bbb";

    @Test
    @DisplayName("the two codecs do what they claim")
    void codecs() {
        assertEquals("3a 3b", Codecs.compress(PAYLOAD));
        assertEquals("resume cafe", Codecs.toAscii7("résumé café"));
        assertEquals("SS", Codecs.toAscii7("ß").toUpperCase());
        assertEquals("?", Codecs.toAscii7("—"));
    }

    @Test
    @DisplayName("problem: a subclass per destination, with identical bodies")
    void problemSubclasses() {
        CompressingSocketStream socket = new CompressingSocketStream();
        socket.putString(PAYLOAD);
        socket.close();
        assertEquals("3a 3b", socket.contents());

        CompressingASCII7FileStream file = new CompressingASCII7FileStream();
        file.putString("aaa café");
        file.close();
        assertEquals("3a cafe", file.contents());
    }

    @Test
    @DisplayName("solution: one decorator serves every destination")
    void oneDecoratorManyDestinations() {
        FileStream file = new FileStream();
        Stream toFile = new CompressingStream(file);
        toFile.putString(PAYLOAD);
        toFile.close();

        SocketStream socket = new SocketStream();
        Stream toSocket = new CompressingStream(socket); // no CompressingSocketStream needed
        toSocket.putString(PAYLOAD);
        toSocket.close();

        assertEquals("3a 3b", file.contents());
        assertEquals("3a 3b", socket.contents());
    }

    @Test
    @DisplayName("solution: decorators nest, exactly as GoF writes it on p. 184")
    void decoratorsNest() {
        FileStream file = new FileStream();
        Stream stream = new CompressingStream(new ASCII7Stream(file));
        stream.putInt(12);
        stream.putString(" aaa café");
        stream.close();

        assertEquals("12 3a cafe", file.contents());
    }

    @Test
    @DisplayName("order matters: the same two decorators, swapped, produce different bytes")
    void orderMatters() {
        FileStream compressFirst = new FileStream();
        Stream a = new CompressingStream(new ASCII7Stream(compressFirst));
        a.putString("aä");
        a.close();

        FileStream foldFirst = new FileStream();
        Stream b = new ASCII7Stream(new CompressingStream(foldFirst));
        b.putString("aä");
        b.close();

        // Compressing first sees 'a' and 'ä' as different characters and finds no run.
        assertEquals("aa", compressFirst.contents());
        // Folding first turns them into "aa", which compression then collapses.
        assertEquals("2a", foldFirst.contents());
        assertNotEquals(compressFirst.contents(), foldFirst.contents());
    }

    @Test
    @DisplayName("a decorator must flush its own buffer before closing what it wraps")
    void closePropagatesOutward() {
        FileStream file = new FileStream(1000);
        Stream stream = new CompressingStream(new ASCII7Stream(file, 1000), 1000);
        stream.putString("aaa");

        // Nothing has reached the file yet: every buffer in the chain is still filling.
        assertEquals("", file.contents());

        stream.close();
        assertEquals("3a", file.contents());
    }

    @Test
    @DisplayName("a full buffer flushes on its own, without close()")
    void bufferFullFlushes() {
        FileStream file = new FileStream(1);
        Stream stream = new CompressingStream(file, 4);
        stream.putString("aaaa"); // fills the decorator's buffer of 4, which flushes outward
        assertEquals("4a", file.contents());
    }

    @Test
    @DisplayName("every link in the chain buffers independently")
    void eachLinkHasItsOwnBuffer() {
        FileStream file = new FileStream(8);
        Stream stream = new CompressingStream(file, 4);

        stream.putString("aaaa");
        // The decorator's buffer filled and it forwarded "4a" — but that is only two
        // characters, so the file stream's own buffer of 8 is not full and nothing has
        // been written. Data moves outward one buffer at a time, and only close()
        // guarantees it has arrived.
        assertEquals("", file.contents());

        stream.close();
        assertEquals("4a", file.contents());
    }

    @Test
    @DisplayName("both designs produce the same bytes")
    void designsAgree() {
        CompressingASCII7FileStream bySubclassing = new CompressingASCII7FileStream();
        bySubclassing.putString("aaa café");
        bySubclassing.close();

        FileStream sink = new FileStream();
        Stream byDecorating = new CompressingStream(new ASCII7Stream(sink));
        byDecorating.putString("aaa café");
        byDecorating.close();

        assertEquals(bySubclassing.contents(), sink.contents());
    }
}
