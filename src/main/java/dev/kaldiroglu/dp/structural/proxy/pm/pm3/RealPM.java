package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

public class RealPM implements PM{
    @Override
    public void listen(String problem) {
        System.out.println("RealPM: Listening to you.");
        resolve(problem);
    }

    @Override
    public void findJob(String name) {
        System.out.println("RealPM: Don't ask me to find a job for you!");
    }

    private void resolve(String problem) {
        System.out.println("RealPM: Please resolve this: " + problem);
    }
}
