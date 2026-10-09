package dev.kaldiroglu.dp.behavioral.visitor.animal.problem;

public class Test {

	public static void main(String[] args) {
		Feeder feeder = new Feeder();
		
		Animal a = new Dog();
		a.eat(); // Prints "Woof"
		
		feeder.feed(a);

		System.out.println();
		
		a = new Cat();
		a.eat(); // Prints "Meeoow"
		
		feeder.feed(a);

		
//		feeder.feed(a); // Can't do this!

//		if (a instanceof Dog) {
//			Dog dog = (Dog) a;
//			feeder.feed(dog);
//		} else if (a instanceof Cat) {
//			Cat cat = (Cat) a;
//			feeder.feed(cat);
//		}
	}
}
