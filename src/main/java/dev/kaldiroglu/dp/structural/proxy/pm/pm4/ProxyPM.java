package dev.kaldiroglu.dp.structural.proxy.pm.pm4;

/**
 * The Proxy, of two kinds in one class.
 * <p>
 * A <em>protection proxy</em>: it decides whether a call reaches the real subject.
 * <p>
 * A <em>virtual proxy</em>: it starts with no {@link RealPM} and creates one the first time a
 * call has to be forwarded. A refused call and {@link #findJob} never create one.
 */
public class ProxyPM implements PM {

    private RealPM realPM;
    private int callsScreened;
    private int callsPassedOn;
    private int callsRefused;

    @Override
    public void listen(String problem) {
        callsScreened++;
        System.out.println("ProxyPM: let me hear it first.");
        if (worthHerTime(problem)) {
            callsPassedOn++;
            realPM().listen(problem);
        } else {
            callsRefused++;
            System.out.println("ProxyPM: I am afraid that is not something she deals with.");
        }
    }

    /** Answered here and never forwarded, so it never creates the real subject. */
    @Override
    public void findJob(String name) {
        System.out.println("ProxyPM: I will see what can be done, " + name + ".");
    }

    private RealPM realPM() {
        if (realPM == null) {
            realPM = new RealPM();
        }
        return realPM;
    }

    private boolean worthHerTime(String problem) {
        return !problem.toLowerCase().contains("job")
                && !problem.toLowerCase().contains("cousin");
    }

    public boolean realPMCreated() {
        return realPM != null;
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
