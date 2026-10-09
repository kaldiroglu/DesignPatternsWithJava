package dev.kaldiroglu.dp.behavioral.memento.pattern2;

public class Test {

	private static Originator originator;

	public static void main(String[] args) {
		originator = new Originator("state-0");
		
		OriginatorTrigger trigger = new OriginatorTrigger();
		trigger.start();

		Caretaker caretaker = new Caretaker(originator);
		caretaker.start();
	}

	static class OriginatorTrigger extends Thread {

		public void run() {
			for (int i = 1; i < 20; i++) {
				String state = "state-" + i;
//				System.out.println("***: " + state);
				originator.setState(state);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
