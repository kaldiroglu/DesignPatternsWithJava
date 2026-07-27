
package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

public class Citizen {
    private String name;
	private PM pm;

    public Citizen(String name, PMSecretary secretary){
        this.name = name;
        this.pm = secretary.getMePM();
    }

	public void tellProblem() {
        pm.listen("The problem is ...");
	}

	public void askForJob() {
        pm.findJob(name);
	}
}
