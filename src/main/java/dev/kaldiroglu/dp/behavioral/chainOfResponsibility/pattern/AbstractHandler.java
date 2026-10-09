package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public abstract class AbstractHandler implements Handler {
	protected Handler successor;

	public AbstractHandler(Handler successor) {
		this.successor = successor;
	}

	/** Each request gets new help objects, so one request cannot change the help of another. */
	protected abstract Help newHelp();
}
