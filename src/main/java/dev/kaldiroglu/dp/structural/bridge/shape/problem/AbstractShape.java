package dev.kaldiroglu.dp.structural.bridge.shape.problem;

public abstract class AbstractShape implements Shape {
	protected String name;
	
	public AbstractShape(String name){
		this.name = name;
	}
}
