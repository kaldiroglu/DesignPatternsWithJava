package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern2;

public abstract class AbstractAnimal implements Animal{
	private String name;

	public AbstractAnimal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
