package dev.kaldiroglu.dp.structural.proxy.pm.pm4;

/**
 * Stage 4. The citizen holds a PM and knows nothing else. The proxy creates the real Prime
 * Minister on the first call it has to forward.
 */
public class Main {

    public static void main(String[] args) {
        ProxyPM proxy = new ProxyPM();
        Citizen ayse = new Citizen("Ayse", proxy);
        Citizen bora = new Citizen("Bora", proxy);
        System.out.println("Prime Minister created yet? " + proxy.realPMCreated());
        System.out.println();

        bora.tellProblem("my cousin needs a job at the ministry");
        bora.askForJob();
        System.out.println("Prime Minister created yet? " + proxy.realPMCreated());
        System.out.println();

        ayse.tellProblem("the bridge on the coast road is closed");
        System.out.println("Prime Minister created yet? " + proxy.realPMCreated());
        System.out.println();

        System.out.printf("calls screened : %d%n", proxy.callsScreened());
        System.out.printf("passed on      : %d%n", proxy.callsPassedOn());
        System.out.printf("refused        : %d%n", proxy.callsRefused());
    }
}
