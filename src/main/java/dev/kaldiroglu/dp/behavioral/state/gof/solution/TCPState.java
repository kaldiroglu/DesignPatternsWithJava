package dev.kaldiroglu.dp.behavioral.state.gof.solution;

/**
 * The <b>State</b>: GoF's {@code TCPState}.
 * <p>
 * It is an abstract class, not an interface, because it gives every operation a default:
 * ignore the request. Each concrete state overrides only the requests it answers. That is
 * how GoF write it.
 */
public abstract class TCPState {

    public abstract String name();

    public void activeOpen(TCPConnection connection) {
        connection.record("ignored: activeOpen");
    }

    public void passiveOpen(TCPConnection connection) {
        connection.record("ignored: passiveOpen");
    }

    public void send(TCPConnection connection, String data) {
        connection.record("ignored: send");
    }

    public void acknowledge(TCPConnection connection) {
        connection.record("ignored: acknowledge");
    }

    public void close(TCPConnection connection) {
        connection.record("ignored: close");
    }

    /** GoF's protected {@code ChangeState}: a state tells the connection which state comes next. */
    protected void changeState(TCPConnection connection, TCPState next) {
        connection.changeState(next);
    }
}
