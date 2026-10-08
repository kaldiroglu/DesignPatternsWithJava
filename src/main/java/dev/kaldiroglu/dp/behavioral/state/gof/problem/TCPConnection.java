package dev.kaldiroglu.dp.behavioral.state.gof.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Before the pattern: a TCP connection that switches on its own state in every operation.
 * <p>
 * GoF's motivation (p. 305): a connection can be established, listening or closed, and it
 * answers each request differently in each state. Here every operation is a switch over the
 * three states, so the rules of one state are spread over five methods.
 */
public final class TCPConnection {

    enum State { CLOSED, LISTEN, ESTABLISHED }

    private State state = State.CLOSED;
    private final List<String> log = new ArrayList<>();

    public void activeOpen() {
        switch (state) {
            case CLOSED -> { log.add("send SYN"); state = State.ESTABLISHED; }
            case LISTEN, ESTABLISHED -> log.add("ignored: activeOpen");
        }
    }

    public void passiveOpen() {
        switch (state) {
            case CLOSED -> state = State.LISTEN;
            case LISTEN, ESTABLISHED -> log.add("ignored: passiveOpen");
        }
    }

    public void send(String data) {
        switch (state) {
            case LISTEN -> { log.add("send SYN, SYN-ACK"); state = State.ESTABLISHED; }
            case ESTABLISHED -> log.add("sent: " + data);
            case CLOSED -> log.add("ignored: send");
        }
    }

    public void acknowledge() {
        switch (state) {
            case ESTABLISHED -> log.add("ACK");
            case CLOSED, LISTEN -> log.add("ignored: acknowledge");
        }
    }

    public void close() {
        switch (state) {
            case ESTABLISHED -> { log.add("send FIN"); state = State.LISTEN; }
            case CLOSED, LISTEN -> log.add("ignored: close");
        }
    }

    public String state() {
        return state.name();
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
