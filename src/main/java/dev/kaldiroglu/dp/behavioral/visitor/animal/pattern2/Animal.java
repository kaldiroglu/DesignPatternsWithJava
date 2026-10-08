package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern2;

public interface Animal {
	void eat();
	
	String getName();
	
	void accept(Feeder feeder);
}
