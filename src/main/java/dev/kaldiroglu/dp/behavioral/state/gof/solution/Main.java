package dev.kaldiroglu.dp.behavioral.state.gof.solution;

/**
 * Sends the same requests as the switch version. Each request goes to the current state
 * object, and the state object chooses the next state.
 */
public final class Main {

    public static void main(String[] args) {
        TCPConnection connection = new TCPConnection();
        System.out.println("State at the start: " + connection.state());
        connection.acknowledge();
        connection.activeOpen();
        System.out.println("State after activeOpen: " + connection.state());
        connection.send("hello");
        connection.acknowledge();
        connection.close();
        System.out.println("Log:   " + connection.log());
        System.out.println("State: " + connection.state());
    }
}
