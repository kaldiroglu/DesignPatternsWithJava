package dev.kaldiroglu.dp.structural.proxy.pm.pm1;

/** A citizen who holds the Prime Minister's own telephone number. */
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
