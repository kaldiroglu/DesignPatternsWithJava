package dev.kaldiroglu.dp.structural.proxy.hw.licence;

/**
 * <b>Proxy</b> — and, unusually, three of GoF's kinds at once. Worth naming all three,
 * because each one is doing a different job here.
 *
 * <ul>
 *   <li><b>Protection.</b> {@link #launch()} refuses when every seat is taken. GoF, p. 208:
 *       "controls access to the original object... useful when objects should have
 *       different access rights."</li>
 *   <li><b>Virtual.</b> The real application is not built until a seat is granted, so a
 *       refused student costs nothing at all. Asserted: the launch counter does not move.</li>
 *   <li><b>Smart reference.</b> {@link #close()} does not close the application so much as
 *       give the seat back — and promote whoever was waiting for it.</li>
 * </ul>
 *
 * <p>What makes this a proxy rather than a wrapper is the interface: a student holds an
 * {@link Application}, and the same code runs whether the university bought three seats or
 * three hundred.</p>
 */
public class LicenceProxy implements Application {

    private final String user;
    private final LicenceServer server;

    private Application real;     // created only once a seat has been granted

    public LicenceProxy(String user, LicenceServer server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public void launch() {
        if (!server.acquire(user)) {
            int position = server.queue().indexOf(user) + 1;
            throw new NoLicenceAvailableException(
                    "all " + server.seats() + " seats for " + server.product()
                            + " are in use; " + user + " is number " + position
                            + " in the queue", position);
        }
        if (real == null) {
            real = new VlsiDesigner(user);      // the expensive part, and only now
        }
        real.launch();
    }

    @Override
    public String open(String document) {
        if (real == null) {
            throw new IllegalStateException(user + " has no licence; launch() first");
        }
        return real.open(document);
    }

    /**
     * Gives the seat back. The real application is closed but kept, so the same student
     * relaunching later does not pay for start-up twice.
     */
    @Override
    public void close() {
        if (real == null) {
            return;
        }
        real.close();
        String promoted = server.release(user);
        if (promoted != null) {
            System.out.println("LicenceProxy: seat released by " + user
                    + " — " + promoted + " may now launch");
        }
    }

    /** Whether this student currently holds one of the university's seats. */
    public boolean hasLicence() {
        return server.isHolding(user);
    }
}
