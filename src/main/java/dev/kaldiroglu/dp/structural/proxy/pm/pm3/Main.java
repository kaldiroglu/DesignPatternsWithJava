package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * Stage 3 — the pattern. The citizen's code is the same shape it was in stage 1, and every
 * call is screened.
 */
public class Main {

    public static void main(String[] args) {
        PMSecretary secretary = new PMSecretary();
        System.out.println("Prime Minister created yet? " + secretary.hasBeenAskedForThePM());

        Citizen ayse = new Citizen("Ayse", secretary);
        System.out.println("Prime Minister created yet? " + secretary.hasBeenAskedForThePM());
        System.out.println();

        Citizen bora = new Citizen("Bora", secretary);

        ayse.tellProblem("the bridge on the coast road is closed");
        System.out.println();
        bora.tellProblem("my cousin needs a job at the ministry");
        System.out.println();
        bora.askForJob();

        ProxyPM proxy = (ProxyPM) secretary.getPM();
        System.out.println();
        System.out.printf("calls screened : %d%n", proxy.callsScreened());
        System.out.printf("passed on      : %d%n", proxy.callsPassedOn());
        System.out.printf("refused        : %d%n", proxy.callsRefused());
        System.out.println("""

                Citizen holds a PM, exactly as it did in stage 1 — the field type
                went back. Nothing in it changed to accommodate the screening, and
                nothing in RealPM changed either.

                Two proxy kinds are at work in this one object:
                  protection — one call was refused, and findJob never reached him
                  virtual    — the Prime Minister did not exist until asked for""");
    }
}
