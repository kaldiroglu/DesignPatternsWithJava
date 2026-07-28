package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * A citizen who holds a {@link PM}.
 * <p>
 * Compare the field with {@code pm2.Citizen}: it is back to being a {@code PM}, exactly as it
 * was in {@code pm1}. The screening now happens, and this class does not know it does.
 */
public class Citizen {

    private final String name;
    private final PM pm;

    public Citizen(String name, PMSecretary secretary) {
        this.name = name;
        this.pm = secretary.getPM();
    }

    public void tellProblem(String problem) {
        pm.listen(problem);
    }

    public void askForJob() {
        pm.findJob(name);
    }
}
