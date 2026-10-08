package dev.kaldiroglu.dp.behavioral.state.gof.solution;

/** A <b>ConcreteState</b>: waiting for the other side. Sending opens the connection. */
public final class TCPListen extends TCPState {

    public static final TCPListen INSTANCE = new TCPListen();

    private TCPListen() {
    }

    @Override
    public String name() {
        return "LISTEN";
    }

    @Override
    public void send(TCPConnection connection, String data) {
        connection.record("send SYN, SYN-ACK");
        changeState(connection, TCPEstablished.INSTANCE);
    }
}
