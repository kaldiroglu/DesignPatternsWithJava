package dev.kaldiroglu.dp.introduction.proxy.pm.pm1;

public class Main {
    public static void main(String[] args) {
        PM pm = new PM();
        Citizen citizen = new Citizen("John", pm);
        citizen.tellProblem();
        citizen.askForJob();
    }
}
