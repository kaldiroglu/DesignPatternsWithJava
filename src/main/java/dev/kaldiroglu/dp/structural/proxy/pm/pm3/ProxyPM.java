package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

import java.util.Objects;

/**
 * The Proxy — a <em>protection proxy</em> in GoF's terms (p. 208): it controls access to the
 * real subject.
 * <p>
 * It implements {@link PM}, which is what makes it substitutable, and holds a {@link PM},
 * which is what lets it forward. Being both is the solution, and it is the same shape as
 * Decorator — the difference is the intent. A decorator <em>adds</em> to what the subject
 * does; this one <em>decides whether the subject is called at all</em>.
 * <p>
 * Note also that it answers {@link #findJob} entirely by itself. A proxy is allowed to
 * satisfy a request without ever touching the real subject, which no decorator would do.
 */
public class ProxyPM implements PM {

    private final PM pm;
    private int callsScreened;
    private int callsPassedOn;
    private int callsRefused;

    public ProxyPM(PM pm) {
        this.pm = Objects.requireNonNull(pm, "a proxy must stand in front of something");
    }

    @Override
    public void listen(String problem) {
        callsScreened++;
        System.out.println("ProxyPM: let me hear it first.");
        if (worthHisTime(problem)) {
            callsPassedOn++;
            pm.listen(problem);
        } else {
            callsRefused++;
            System.out.println("ProxyPM: I am afraid that is not something she deals with.");
        }
    }

    /** Answered here and never forwarded — the request does not reach the real subject. */
    @Override
    public void findJob(String name) {
        System.out.println("ProxyPM: I will see what can be done, " + name + ".");
    }

    private boolean worthHisTime(String problem) {
        return !problem.toLowerCase().contains("job")
                && !problem.toLowerCase().contains("cousin");
    }

    public int callsScreened() {
        return callsScreened;
    }

    public int callsPassedOn() {
        return callsPassedOn;
    }

    public int callsRefused() {
        return callsRefused;
    }
}
