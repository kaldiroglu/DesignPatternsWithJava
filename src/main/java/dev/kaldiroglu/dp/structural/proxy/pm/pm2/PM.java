package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

/**
 * Stage 2 — the Prime Minister, relieved of the screening.
 * <p>
 * This class is now doing one job, which is an improvement on {@code pm1}. Everything that
 * reaches {@code listen} is something he should be hearing.
 */
public class PM {

    private int problemsHeard;

    public void listen(String problem) {
        problemsHeard++;
        System.out.println("PM: listening to you.");
        resolve(problem);
    }

    public void findJob(String name) {
        System.out.println("PM: do not ask me to find a job for you, " + name + ".");
    }

    private void resolve(String problem) {
        System.out.println("PM: I will have this resolved — " + problem);
    }

    public int problemsHeard() {
        return problemsHeard;
    }
}
