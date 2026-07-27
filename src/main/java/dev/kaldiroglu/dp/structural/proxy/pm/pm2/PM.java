package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

public class PM {

    public void listen(String problem) {
        System.out.println("PM: Listening to you.");
        resolve(problem);
    }

    public void findJob(String name) {
        System.out.println("PM: Don't ask me to find a job for you!");
    }

    private void resolve(String problem) {
        System.out.println("PM: Please resolve this: " + problem);
    }
}
