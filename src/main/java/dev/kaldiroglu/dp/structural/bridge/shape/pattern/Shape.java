package dev.kaldiroglu.dp.structural.bridge.shape.pattern;

public interface Shape{
	
	public void draw();
	
	public void erase();
	
	public void setDrawer(ShapeDrawer drawer);

}
