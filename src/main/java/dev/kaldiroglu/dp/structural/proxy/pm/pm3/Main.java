package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

public class Main {
    public static void main(String[] args) {
        System.out.println("Everything starts wth a citizen coming to PM Secretary and asking for PM");
        PMSecretary secretary = new PMSecretary();
        Citizen citizen = new Citizen("John", secretary);
        citizen.tellProblem();
        citizen.askForJob();
    }
}
