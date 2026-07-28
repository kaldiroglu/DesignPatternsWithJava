package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

/**
 * Stage 2 — a stand-in that screens, and the reason this stage is <strong>not yet</strong>
 * the pattern.
 * <p>
 * The screening has moved out of {@link PM}, which was the point, and this class does it
 * well. But look at what it <em>is</em>: a class of its own, related to {@code PM} by nothing
 * at all. They share no supertype, so nothing in the type system says one can stand in for
 * the other.
 * <p>
 * The consequence is on the very next line of {@link Citizen}: its field had to change from
 * {@code PM} to {@code Proxy}. <strong>Every client had to be edited to accommodate the
 * stand-in</strong> — which is precisely what a proxy is supposed to make unnecessary.
 * <p>
 * A stand-in the client can see is not a proxy. It is just a different object. {@code pm3}
 * fixes this with one interface.
 */
public class Proxy {

    private final PM pm;
    private int callsScreened;
    private int callsPassedOn;

    public Proxy(PM pm) {
        this.pm = pm;
    }

    public void listen(String problem) {
        callsScreened++;
        System.out.println("Proxy: let me hear it first.");
        if (worthHisTime(problem)) {
            callsPassedOn++;
            pm.listen(problem);
        } else {
            System.out.println("Proxy: I am afraid that is not something he deals with.");
        }
    }

    public void findJob(String name) {
        System.out.println("Proxy: I will see what can be done, " + name + ".");
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
}
