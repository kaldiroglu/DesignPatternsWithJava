package dev.kaldiroglu.dp.introduction.proxy.pm.pm3;

/**
 * A citizen who holds a {@link PM}.
 * <p>
 * Compare the field with {@code pm1.Citizen} and {@code pm2.Citizen}. In {@code pm1} it was
 * {@code PM}, a class; in {@code pm2} it was {@code Proxy}. Here it is {@link PM}, an
 * interface. The screening now happens, and this class does not know it does.
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
