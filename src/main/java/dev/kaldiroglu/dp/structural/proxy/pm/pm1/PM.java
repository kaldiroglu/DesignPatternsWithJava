package dev.kaldiroglu.dp.structural.proxy.pm.pm1;

/**
 * Stage 1 — no proxy at all. The Prime Minister takes the calls herself.
 * <p>
 * Notice what this class is doing. Two jobs, not one: it <em>resolves</em> problems, which is
 * the Prime Minister's actual work, and it <em>decides which problems are worth her time</em>,
 * which is not. The screening logic sits inside the very class it exists to protect.
 * <p>
 * That is the design this package exists to criticize. Compare {@code pm2} and {@code pm3}.
 */
public class PM {

    private int problemsHeard;
    private int problemsResolved;

    public void listen(String problem) {
        problemsHeard++;
        System.out.println("PM: listening to you.");
        if (worthMyTime(problem)) {
            resolve(problem);
        } else {
            System.out.println("PM: that is not something I deal with.");
        }
    }

    public void findJob(String name) {
        System.out.println("PM: do not ask me to find a job for you, " + name + ".");
    }

    /**
     * The screening rule — and the problem with this design.
     * <p>
     * It is a perfectly sensible rule. It is simply in the wrong class: the Prime Minister
     * has to run it herself, on every call, before she can do any of her own work.
     */
    private boolean worthMyTime(String problem) {
        return !problem.toLowerCase().contains("job")
                && !problem.toLowerCase().contains("cousin");
    }

    private void resolve(String problem) {
        problemsResolved++;
        System.out.println("PM: I will have this resolved — " + problem);
    }

    public int problemsHeard() {
        return problemsHeard;
    }

    public int problemsResolved() {
        return problemsResolved;
    }
}
