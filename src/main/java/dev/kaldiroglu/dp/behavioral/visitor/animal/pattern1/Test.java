package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern1;

public class Test {

	public static void main(String[] args) {
		Feeder feeder = new Feeder();
		
		Animal a = new Dog();
		a.accept(feeder);

		a = new Cat();
		a.accept(feeder);
	}
}
