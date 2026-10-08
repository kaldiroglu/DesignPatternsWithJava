package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern1;

public class Dog implements Animal {
    public void eat() {
        System.out.println("Woof");
    }

	@Override
	public void accept(Feeder feeder) {
		feeder.feed(this);
	}
}