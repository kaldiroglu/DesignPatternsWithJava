
package dev.kaldiroglu.dp.structural.proxy.pm.pm2;

public class Citizen {
    private String name;
	private Proxy proxy;

    public Citizen(String name, Proxy proxy){
        this.name = name;
        this.proxy = proxy;
    }

	public void tellProblem() {
        proxy.listen("The problem is ...");
	}

	public void askForJob() {
        proxy.findJob(name);
	}
}
