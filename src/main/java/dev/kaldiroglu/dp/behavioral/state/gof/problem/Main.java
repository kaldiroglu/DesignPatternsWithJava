package dev.kaldiroglu.dp.behavioral.state.gof.problem;

/**
 * Sends the same requests to a connection that switches on its state in every method:
 * one ignored request, an open, some data, an acknowledgment and a close.
 */
public final class Main {

    public static void main(String[] args) {
        TCPConnection connection = new TCPConnection();
        connection.acknowledge();
        connection.activeOpen();
        connection.send("hello");
        connection.acknowledge();
        connection.close();
        System.out.println("Log:   " + connection.log());
        System.out.println("State: " + connection.state());
    }
}
