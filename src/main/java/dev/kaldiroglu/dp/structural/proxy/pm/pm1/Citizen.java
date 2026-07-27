
package dev.kaldiroglu.dp.structural.proxy.pm.pm1;

public class Citizen {
    private String name;
	private PM pm;

    public Citizen(String name, PM pm){
        this.name = name;
        this.pm = pm;
    }

	public void tellProblem() {
		pm.listen("The problem is ...");
	}

	public void askForJob() {
		pm.findJob(name);
	}
}
