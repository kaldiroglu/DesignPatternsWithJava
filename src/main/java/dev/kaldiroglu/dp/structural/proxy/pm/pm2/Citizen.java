package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

/**
 * A citizen who now telephones a {@code Proxy}.
 * <p>
 * Read the field type. In {@code pm1} it was {@code PM}; here it is {@code Proxy}. The
 * citizen was <em>rewritten</em> because the way she is answered changed — and that is the
 * whole defect of this stage. Substituting a stand-in should be invisible to her.
 */
public class Citizen {

    private final String name;
    private final Proxy proxy;      // <-- was PM in pm1. The client had to change.

    public Citizen(String name, Proxy proxy) {
        this.name = name;
        this.proxy = proxy;
    }

    public void tellProblem(String problem) {
        proxy.listen(problem);
    }

    public void askForJob() {
        proxy.findJob(name);
    }
}
