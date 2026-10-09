package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public class Test {

	public static void main(String[] args) {
		Handler handler3 = new ConcreteHandler3(null);
		Handler handler2 = new ConcreteHandler2(handler3);
		Handler handler1 = new ConcreteHandler1(handler2);

		Help help = handler1.handleRequest(Context.MORE_SPECIFIC);
		help.show();

		System.out.println();
		
		help = handler1.handleRequest(Context.SPECIFIC);
		help.show();
		
		System.out.println();

		help = handler1.handleRequest(Context.GENERIC);
		help.show();
	}
}
