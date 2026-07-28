package dev.kaldiroglu.dp.structural.proxy.pm.pm1;

/**
 * Stage 1 — everyone gets through, and the Prime Minister does his own screening.
 */
public class Main {

    public static void main(String[] args) {
        PM pm = new PM();
        Citizen ayse = new Citizen("Ayse", pm);
        Citizen bora = new Citizen("Bora", pm);

        ayse.tellProblem("the bridge on the coast road is closed");
        System.out.println();
        bora.tellProblem("my cousin needs a job at the ministry");
        System.out.println();
        bora.askForJob();

        System.out.println();
        System.out.printf("problems heard by the PM himself : %d%n", pm.problemsHeard());
        System.out.printf("problems actually resolved       : %d%n", pm.problemsResolved());
        System.out.println("""

                Both calls reached him. He listened to both, and screened both,
                before doing any work of his own.

                Two things are wrong, and neither is the screening rule:
                  1. The Prime Minister's class contains a job that is not his.
                  2. Every citizen holds a direct reference to him. There is no
                     way to change who answers without changing the citizens.""");
    }
}
