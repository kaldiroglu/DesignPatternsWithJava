package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

public class Main {
    public static void main(String[] args) {
        PM pm = new PM();
        Proxy proxy = new Proxy(pm);
        Citizen citizen = new Citizen("John", proxy);
        citizen.tellProblem();
        citizen.askForJob();
    }
}
