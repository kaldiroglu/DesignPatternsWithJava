package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public class ConcreteHandler3 extends AbstractHandler {

	public ConcreteHandler3(Handler successor) {
		super(successor);
	}

	@Override
	protected Help newHelp() {
		return new Help3();
	}

	@Override
	public Help handleRequest(Context context) {
		// The last handler: it answers every context that reaches it, Context.GENERIC included.
		return newHelp();
	}
}
