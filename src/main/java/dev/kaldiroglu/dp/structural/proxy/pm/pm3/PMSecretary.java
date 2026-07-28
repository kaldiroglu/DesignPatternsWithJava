package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * How a citizen obtains a {@link PM} — and the reason he cannot obtain the real one.
 * <p>
 * The secretary hands out a {@link ProxyPM}. Nothing in the returned type says so, and no
 * caller can reach past it to the {@link RealPM}, because no caller is ever given a reference
 * to it.
 * <p>
 * The Prime Minister is also created <strong>lazily</strong>: he is not brought into
 * existence until somebody actually needs him. That is a second kind of proxy — GoF's
 * <em>virtual</em> proxy — quietly present in the same object.
 */
public class PMSecretary {

    private PM pm;

    public PM getPM() {
        if (pm == null) {
            pm = new ProxyPM(new RealPM());
        }
        return pm;
    }

    /** For the demo and the tests: has the office been set up yet? */
    public boolean hasBeenAskedForThePM() {
        return pm != null;
    }
}
