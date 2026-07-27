package dev.kaldiroglu.dp.structural.proxy.pm.pm1;

public class PM {
	
	public void listen(String problem) {
		System.out.println("PM: Listening to you.");
		if(sortOut(problem))
			resolve(problem);
	}

	public void findJob(String name) {
		System.out.println("PM: Don't ask me to find a job for you!");
	}
	
	private boolean sortOut(String problem){
		boolean b = true;
		//...
		return b;
	}
	
	private void resolve(String problem) {
		System.out.println("PM: Please resolve this: " + problem);
	}
}
