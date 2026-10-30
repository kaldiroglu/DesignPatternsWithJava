package dev.kaldiroglu.dp.structural.proxy.pm.pm4;

/**
 * The RealSubject: the Prime Minister, doing one job.
 * <p>
 * The class is package-private, so no code outside this package can create one or call it.
 * Only {@link ProxyPM} creates it.
 */
class RealPM implements PM {

    RealPM() {
        System.out.println("RealPM: the Prime Minister takes office.");
    }

    @Override
    public void listen(String problem) {
        System.out.println("RealPM: I will have this resolved — " + problem);
    }

    @Override
    public void findJob(String name) {
        System.out.println("RealPM: do not ask me to find a job for you, " + name + ".");
    }
}
