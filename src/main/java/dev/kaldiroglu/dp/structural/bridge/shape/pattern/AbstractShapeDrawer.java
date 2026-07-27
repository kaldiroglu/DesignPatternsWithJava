package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

public abstract class AbstractShapeDrawer implements ShapeDrawer{
	protected String name;
	
	public AbstractShapeDrawer(String name){
		this.name = name;
	}
}
