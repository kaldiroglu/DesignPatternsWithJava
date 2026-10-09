package dev.kaldiroglu.dp.behavioral.state.gof;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import dev.kaldiroglu.dp.behavioral.state.gof.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.state.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GoF's TCP connection (Design Patterns, pp. 305-313), before and after the pattern. The
 * Part 2 slides say the two versions give identical logs; this class checks it.
 */
class TCPConnectionTest {

    private static final String PROBLEM =
            "src/main/java/dev/kaldiroglu/dp/behavioral/state/gof/problem/TCPConnection.java";

    private static final List<String> REQUESTS =
            List.of("activeOpen", "passiveOpen", "send", "acknowledge", "close");

    private record Pair(dev.kaldiroglu.dp.behavioral.state.gof.problem.TCPConnection before,
                        TCPConnection after) {
        Pair() {
            this(new dev.kaldiroglu.dp.behavioral.state.gof.problem.TCPConnection(),
                    new TCPConnection());
        }

        void run(String request) {
            switch (request) {
                case "activeOpen" -> { before.activeOpen(); after.activeOpen(); }
                case "passiveOpen" -> { before.passiveOpen(); after.passiveOpen(); }
                case "send" -> { before.send("data"); after.send("data"); }
                case "acknowledge" -> { before.acknowledge(); after.acknowledge(); }
                case "close" -> { before.close(); after.close(); }
                default -> throw new IllegalArgumentException(request);
            }
        }
    }

    @Test
    @DisplayName("both versions give the same log and the same state for every pair of requests")
    void bothVersionsAgree() {
        for (String first : REQUESTS) {
            for (String second : REQUESTS) {
                for (String third : REQUESTS) {
                    Pair pair = new Pair();
                    pair.run(first);
                    pair.run(second);
                    pair.run(third);
                    String path = first + ", " + second + ", " + third;
                    assertEquals(pair.before().log(), pair.after().log(), path);
                    assertEquals(pair.before().state(), pair.after().state(), path);
                }
            }
        }
    }

    @Test
    @DisplayName("an established connection sends, acknowledges, and goes back to listening when closed")
    void established() {
        TCPConnection connection = new TCPConnection();
        connection.activeOpen();
        assertEquals("ESTABLISHED", connection.state());

        connection.send("hello");
        connection.acknowledge();
        connection.close();

        assertEquals(List.of("send SYN", "sent: hello", "ACK", "send FIN"), connection.log());
        assertEquals("LISTEN", connection.state());
    }

    @Test
    @DisplayName("a listening connection opens when it sends, and a closed one ignores send")
    void listenAndClosed() {
        TCPConnection connection = new TCPConnection();
        connection.send("x");
        connection.passiveOpen();
        assertEquals("LISTEN", connection.state());
        connection.send("x");

        assertEquals(List.of("ignored: send", "send SYN, SYN-ACK"), connection.log());
        assertEquals("ESTABLISHED", connection.state());
    }

    @Test
    @DisplayName("Main prints the same log for the switch and for State objects")
    void mainOutput() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        String log = "[ignored: acknowledge, send SYN, sent: hello, ACK, send FIN] -> LISTEN";
        assertEquals(List.of("Switch:       " + log, "State objects: " + log), lines);
    }

    @Test
    @DisplayName("before the pattern, five requests and three states: every request is a switch")
    void fiveRequestsThreeStates() {
        String code = codeOf(PROBLEM);

        assertEquals(5, code.split("switch \\(state\\)", -1).length - 1);
        assertEquals(3, dev.kaldiroglu.dp.behavioral.state.gof.problem.TCPConnection.class
                .getDeclaredClasses()[0].getEnumConstants().length);
        long established = Arrays.stream(code.split("public void "))
                .skip(1)
                .limit(5)
                .filter(method -> method.contains("ESTABLISHED"))
                .count();
        assertEquals(5, established, "the rules of the established state are in all five methods");
    }

    @Test
    @DisplayName("the state ignores every request by default, and only the states may change the connection")
    void defaultsAndChangeState() throws Exception {
        assertTrue(Modifier.isAbstract(TCPState.class.getModifiers()));
        for (String request : REQUESTS) {
            Method method = Arrays.stream(TCPState.class.getDeclaredMethods())
                    .filter(m -> m.getName().equals(request)).findFirst().orElseThrow();
            assertFalse(Modifier.isAbstract(method.getModifiers()), request);
        }

        Method changeState = TCPConnection.class.getDeclaredMethod("changeState", TCPState.class);
        int modifiers = changeState.getModifiers();
        assertFalse(Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)
                || Modifier.isPrivate(modifiers), "changeState is package-private");
    }

    @Test
    @DisplayName("each concrete state is one shared object, because it holds no data")
    void sharedStates() {
        for (Class<?> state : List.of(TCPClosed.class, TCPListen.class, TCPEstablished.class)) {
            assertTrue(Arrays.stream(state.getDeclaredFields())
                    .allMatch(f -> Modifier.isStatic(f.getModifiers())), state.getSimpleName());
            assertTrue(Arrays.stream(state.getDeclaredConstructors())
                    .allMatch(c -> Modifier.isPrivate(c.getModifiers())), state.getSimpleName());
        }
        assertEquals("CLOSED", new TCPConnection().state());
    }
}
