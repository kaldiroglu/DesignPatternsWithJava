package dev.kaldiroglu.dp.behavioral.state.gof.solution;

/** A <b>ConcreteState</b>: the connection is open. It sends data, acknowledges, and closes. */
public final class TCPEstablished extends TCPState {

    public static final TCPEstablished INSTANCE = new TCPEstablished();

    private TCPEstablished() {
    }

    @Override
    public String name() {
        return "ESTABLISHED";
    }

    @Override
    public void send(TCPConnection connection, String data) {
        connection.record("sent: " + data);
    }

    @Override
    public void acknowledge(TCPConnection connection) {
        connection.record("ACK");
    }

    @Override
    public void close(TCPConnection connection) {
        connection.record("send FIN");
        changeState(connection, TCPListen.INSTANCE);
    }
}
