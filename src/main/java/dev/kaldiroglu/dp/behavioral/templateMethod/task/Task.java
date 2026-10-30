package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public abstract class Task {
	protected int interval;
	protected int repetition;
	
	public Task(int interval, int repetition) {
		this.interval = interval;
		this.repetition = repetition;
	}

	protected void prepare() {
		System.out.println("*** in prepare() ***");
	}
	
	protected void clean() {
		System.out.println("*** in clean() ***");
	}
	
	protected void before() {
		System.out.println("\n- in before() -");
	}

	protected void after() {
		System.out.println("- in after() -\n");
	}

	protected abstract void doTask();

	public final void run() {
		prepare();
		int repetitionCount = 0;
		while (repetitionCount < repetition) {
			before();
			doTask();
			after();
			repetitionCount++;
			// Wait only between repetitions, not after the last one.
			if (repetitionCount < repetition) {
				try {
					Thread.sleep(interval * 1000L);
				} catch (InterruptedException e) {
					// Someone asked this thread to stop: keep the interrupt flag and stop.
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
		clean();
	}
}
