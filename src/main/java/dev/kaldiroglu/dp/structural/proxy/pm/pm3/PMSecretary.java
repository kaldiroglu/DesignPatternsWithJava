package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * How a citizen obtains a {@link PM} — and the reason she cannot obtain the real one.
 * <p>
 * The secretary hands out a {@link ProxyPM}. Nothing in the returned type says so, and no
 * caller can reach past it to the {@link RealPM}, because no caller is ever given a reference
 * to it.
 * <p>
 * The Prime Minister is created the first time {@link #getPM()} is called, and
 * {@link Citizen}'s constructor calls it. So she exists from the first citizen, before any
 * call is made. This is not yet GoF's <em>virtual</em> proxy; {@code pm4} creates her on the
 * first forwarded call.
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
