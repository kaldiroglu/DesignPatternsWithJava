package dev.kaldiroglu.dp.behavioral.state.gof;

/** Runs the same requests through both versions and prints both logs. */
public final class Main {

    public static void main(String[] args) {
        var before = new dev.kaldiroglu.dp.behavioral.state.gof.problem.TCPConnection();
        before.acknowledge();
        before.activeOpen();
        before.send("hello");
        before.acknowledge();
        before.close();
        System.out.println("Switch:       " + before.log() + " -> " + before.state());

        var after = new dev.kaldiroglu.dp.behavioral.state.gof.solution.TCPConnection();
        after.acknowledge();
        after.activeOpen();
        after.send("hello");
        after.acknowledge();
        after.close();
        System.out.println("State objects: " + after.log() + " -> " + after.state());
    }
}
