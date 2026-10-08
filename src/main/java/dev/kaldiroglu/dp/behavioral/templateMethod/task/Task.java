package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public abstract class Task {
	protected String name;
	protected int interval;
	protected int repetition;
	
	public Task(String name, int interval, int repetition) {
		this.name = name;
		this.interval = interval;
		this.repetition = repetition;
	}

	public void prepare() {
		System.out.println("*** in prepare() ***");
	}
	
	public void clean() {
		System.out.println("*** in clean() ***");
	}
	
	public void before() {
		System.out.println("\n- in before() -");
	}

	public void after() {
		System.out.println("- in after() -\n");
	}

	public abstract void doTask();

	public final void run() {
		prepare();
		int repetitionCount = 0;
		while (repetitionCount < repetition) {
			before();
			doTask();
			after();
			repetitionCount++;
			try {
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		 clean() ;
	}
}
