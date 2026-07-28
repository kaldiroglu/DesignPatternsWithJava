package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * The RealSubject: the Prime Minister, doing one job.
 * <p>
 * No screening, no gatekeeping, no awareness that anybody stands in front of him. Compare
 * {@code pm1.PM}, which had to do all three.
 */
public class RealPM implements PM {

    private int problemsHeard;

    @Override
    public void listen(String problem) {
        problemsHeard++;
        System.out.println("RealPM: listening to you.");
        resolve(problem);
    }

    @Override
    public void findJob(String name) {
        System.out.println("RealPM: do not ask me to find a job for you, " + name + ".");
    }

    private void resolve(String problem) {
        System.out.println("RealPM: I will have this resolved — " + problem);
    }

    public int problemsHeard() {
        return problemsHeard;
    }
}
