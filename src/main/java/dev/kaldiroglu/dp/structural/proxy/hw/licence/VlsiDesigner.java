package dev.kaldiroglu.dp.structural.proxy.hw.licence;

/**
 * <b>RealSubject</b> — the licensed application itself.
 *
 * <p>Knows nothing about licences, seats or queues, exactly as the Prime Minister knows
 * nothing about the deputy screening her calls. That is what lets the university change
 * how many seats it buys without recompiling this.</p>
 *
 * <p>It counts its own launches so a test can prove the expensive object was never built
 * for a student who did not get a seat.</p>
 */
public class VlsiDesigner implements Application {

    private static int launches;

    private final String user;
    private boolean running;

    VlsiDesigner(String user) {
        this.user = user;
        launches++;                 // in the real thing: three seconds of start-up
    }

    @Override
    public void launch() {
        running = true;
        System.out.println("VlsiDesigner: started for " + user);
    }

    @Override
    public String open(String document) {
        if (!running) {
            throw new IllegalStateException("not started");
        }
        return "VlsiDesigner[" + user + "] editing " + document;
    }

    @Override
    public void close() {
        running = false;
        System.out.println("VlsiDesigner: closed for " + user);
    }

    /** How many real applications have been created since the last reset. */
    public static int launchCount() {
        return launches;
    }

    public static void resetLaunchCount() {
        launches = 0;
    }
}
