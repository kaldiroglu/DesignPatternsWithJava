package dev.kaldiroglu.dp.structural.proxy.pm.pm4;

/**
 * A citizen who holds a {@link PM}.
 * <p>
 * The field and the constructor parameter are both {@code PM}, an interface. This class names
 * no proxy and no secretary, so it does not change when the object behind the interface does.
 */
public class Citizen {

    private final String name;
    private final PM pm;

    public Citizen(String name, PM pm) {
        this.name = name;
        this.pm = pm;
    }

    public void tellProblem(String problem) {
        pm.listen(problem);
    }

    public void askForJob() {
        pm.findJob(name);
    }
}
