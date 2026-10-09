package dev.kaldiroglu.dp.behavioral.state.pattern;

import java.util.List;

/**
 * Shows the shape of the earlier TCP example. Its methods are empty, so running them
 * prints nothing; the full version is in {@code state.gof.solution}.
 */
public final class Main {

    public static void main(String[] args) {
        TCPConnection connection = new ConcreteTCPConnection();
        connection.open();
        connection.acknowledge();
        connection.close();
        List<TCPState> states = List.of(new TCPClosed(), new TCPListen(), new TCPEstablished());
        for (TCPState state : states) {
            System.out.println(state.getClass().getSimpleName() + " implements TCPState");
        }
        System.out.println("The methods of this version are empty: it shows only the classes of the pattern.");
    }
}
