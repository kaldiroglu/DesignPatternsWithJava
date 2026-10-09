package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public abstract class AbstractCallTaker implements CallTaker{
	protected CallTaker next;
	
	public AbstractCallTaker(CallTaker next) {
		this.next = next;
	}

	public CallTaker getNext() {
		return next;
	}

	public void setNext(CallTaker next) {
		this.next = next;
	}
}
