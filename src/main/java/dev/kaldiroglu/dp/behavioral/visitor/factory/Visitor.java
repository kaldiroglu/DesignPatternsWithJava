package dev.kaldiroglu.dp.behavioral.visitor.factory;

public interface Visitor {
	
	public void visit(Employee employee);

	public void visit(Boss boss);

}
