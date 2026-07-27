package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

public class PMSecretary {
    private PM pm = new ProxyPM(new RealPM());

    PM getMePM(){
        return pm;
    }
}
