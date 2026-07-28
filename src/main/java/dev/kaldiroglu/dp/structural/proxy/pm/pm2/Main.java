package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

/**
 * Stage 2 — the screening is in the right place, and the citizen still had to be rewritten.
 */
public class Main {

    public static void main(String[] args) {
        PM pm = new PM();
        Proxy proxy = new Proxy(pm);
        Citizen ayse = new Citizen("Ayse", proxy);
        Citizen bora = new Citizen("Bora", proxy);

        ayse.tellProblem("the bridge on the coast road is closed");
        System.out.println();
        bora.tellProblem("my cousin needs a job at the ministry");

        System.out.println();
        System.out.printf("calls screened by the proxy : %d%n", proxy.callsScreened());
        System.out.printf("calls passed to the PM      : %d%n", proxy.callsPassedOn());
        System.out.printf("problems the PM heard       : %d%n", pm.problemsHeard());
        System.out.println("""

                Better: the Prime Minister's class no longer screens anything,
                and one of the two calls never reached him.

                Still wrong: Citizen holds a Proxy, not a PM. The two classes
                share no type, so the stand-in is visible to every client — and
                every client had to be edited to accept it.

                A stand-in the client can see is not a proxy.""");
    }
}
