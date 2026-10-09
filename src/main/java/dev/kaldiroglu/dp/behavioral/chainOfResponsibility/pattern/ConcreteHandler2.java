package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public class ConcreteHandler2 extends AbstractHandler {

	public ConcreteHandler2(Handler successor) {
		super(successor);
	}

	@Override
	protected Help newHelp() {
		return new Help2();
	}

	@Override
	public Help handleRequest(Context context) {
		if(context == Context.SPECIFIC)
			return newHelp();
		else {
			Help successorHelp = successor.handleRequest(context);
			successorHelp.addHelp(newHelp());
			return successorHelp;
		}
	}
}
