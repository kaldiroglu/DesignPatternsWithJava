package dev.kaldiroglu.dp.structural.proxy.hw.licence;

import java.util.List;

/**
 * Three seats, five students.
 *
 * <p>Prints what each student got, what it cost, and — the number worth reading — how many
 * copies of the expensive application were actually created.</p>
 */
public class Main {

    public static void main(String[] args) {
        VlsiDesigner.resetLaunchCount();
        LicenceServer server = new LicenceServer("VlsiDesigner", 3);

        List<String> students = List.of("Ayse", "Bora", "Cem", "Deniz", "Ece");
        LicenceProxy[] sessions = new LicenceProxy[students.size()];

        for (int i = 0; i < students.size(); i++) {
            String student = students.get(i);
            sessions[i] = new LicenceProxy(student, server);
            try {
                sessions[i].launch();
                System.out.println("  " + student + ": " + sessions[i].open("adder.vhd"));
            } catch (NoLicenceAvailableException e) {
                System.out.println("  " + student + ": refused — " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("Seats                    : " + server.seats());
        System.out.println("In use                   : " + server.inUse());
        System.out.println("Waiting                  : " + server.queue());
        System.out.println("Applications created     : " + VlsiDesigner.launchCount());
        System.out.println();

        System.out.println("Ayse closes her session:");
        sessions[0].close();
        System.out.println("  waiting now             : " + server.queue());
        System.out.println("  Deniz holds a seat?     : " + sessions[3].hasLicence());

        System.out.println();
        sessions[3].launch();
        System.out.println("  " + sessions[3].open("multiplier.vhd"));
        System.out.println();
        System.out.println("Applications created     : " + VlsiDesigner.launchCount());
        System.out.println("Two students were refused, and neither cost a single object.");
    }
}
