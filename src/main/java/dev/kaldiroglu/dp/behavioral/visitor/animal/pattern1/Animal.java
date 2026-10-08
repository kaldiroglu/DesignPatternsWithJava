package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern1;

public interface Animal {
	void eat();
	
	void accept(Feeder feeder);
}
