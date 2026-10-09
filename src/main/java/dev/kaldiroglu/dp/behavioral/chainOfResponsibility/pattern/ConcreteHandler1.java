package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public class ConcreteHandler1 extends AbstractHandler {

	public ConcreteHandler1(Handler successor) {
		super(successor);
	}

	@Override
	protected Help newHelp() {
		return new Help1();
	}

	@Override
	public Help handleRequest(Context context) {
		if(context == Context.MORE_SPECIFIC)
			return newHelp();
		else {
			Help successorHelp = successor.handleRequest(context);
			successorHelp.addHelp(newHelp());
			return successorHelp;
		}
	}
}
