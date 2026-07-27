package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

public class ProxyPM implements PM{
    private PM pm;

    public ProxyPM(PM pm){
        this.pm = pm;
    }

    public void listen(String problem) {
        System.out.println("Proxy: Listening to you.");
        if(sortOut(problem))
            delegate(problem);
    }

    public void findJob(String name) {
        System.out.println("Proxy: 'I'll find out what I can do for you!'");
    }

    private void delegate(String problem) {
        pm.listen(problem);
    }

    private boolean sortOut(String problem){
        boolean b = true;
        //...
        return b;
    }
}
