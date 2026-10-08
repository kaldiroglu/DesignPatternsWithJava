package dev.kaldiroglu.dp.behavioral.state.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Context</b>: GoF's {@code TCPConnection}.
 * <p>
 * It holds one {@link TCPState} and forwards every request to it, passing itself so that
 * the state can change it. {@link #changeState} is package-private: only the states may
 * call it. In C++ GoF make {@code TCPState} a friend of the connection for the same reason.
 */
public final class TCPConnection {

    private TCPState state = TCPClosed.INSTANCE;
    private final List<String> log = new ArrayList<>();

    public void activeOpen() {
        state.activeOpen(this);
    }

    public void passiveOpen() {
        state.passiveOpen(this);
    }

    public void send(String data) {
        state.send(this, data);
    }

    public void acknowledge() {
        state.acknowledge(this);
    }

    public void close() {
        state.close(this);
    }

    public String state() {
        return state.name();
    }

    public List<String> log() {
        return List.copyOf(log);
    }

    void changeState(TCPState next) {
        state = next;
    }

    void record(String line) {
        log.add(line);
    }
}
