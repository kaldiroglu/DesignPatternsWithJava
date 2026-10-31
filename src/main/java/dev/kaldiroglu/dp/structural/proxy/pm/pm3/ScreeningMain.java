package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * Stage 3 — the solution. The citizen holds PM, which is now an interface, and every call is
 * screened.
 */
public class ScreeningMain {

    public static void main(String[] args) {
        PMSecretary secretary = new PMSecretary();
        Citizen ayse = new Citizen("Ayse", secretary);
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

                Citizen holds PM, which is now an interface. In stage 1 PM was a
                class; the name is the same, the type is not. Citizen does not
                know that its calls are screened, and RealPM screens nothing.

                Two proxy kinds are at work in this one object:
                  protection — one call was refused, and findJob never reached her
                  virtual    — the Prime Minister did not exist until asked for""");
    }
}
