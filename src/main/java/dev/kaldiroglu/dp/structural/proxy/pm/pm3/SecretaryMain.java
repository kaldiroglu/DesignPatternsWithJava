package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * Stage 3 — the secretary creates the Prime Minister when the first citizen is constructed,
 * before any call.
 */
public class SecretaryMain {

    public static void main(String[] args) {
        PMSecretary secretary = new PMSecretary();
        System.out.println("Prime Minister created yet? " + secretary.hasBeenAskedForThePM());

        new Citizen("Ayse", secretary);
        System.out.println("Prime Minister created yet? " + secretary.hasBeenAskedForThePM());
    }
}
