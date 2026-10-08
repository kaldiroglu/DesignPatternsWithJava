package dev.kaldiroglu.dp.behavioral.state.gof.solution;

/**
 * A <b>ConcreteState</b>. It has no fields, so one object serves every connection: GoF make
 * each state a Singleton. That is GoF implementation issue 2 (creating and destroying State
 * objects): create a state once and share it, when it holds nothing of its own.
 */
public final class TCPClosed extends TCPState {

    public static final TCPClosed INSTANCE = new TCPClosed();

    private TCPClosed() {
    }

    @Override
    public String name() {
        return "CLOSED";
    }

    @Override
    public void activeOpen(TCPConnection connection) {
        connection.record("send SYN");
        changeState(connection, TCPEstablished.INSTANCE);
    }

    @Override
    public void passiveOpen(TCPConnection connection) {
        changeState(connection, TCPListen.INSTANCE);
    }
}
