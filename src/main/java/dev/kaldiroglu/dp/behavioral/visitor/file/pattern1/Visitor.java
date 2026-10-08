package dev.kaldiroglu.dp.behavioral.visitor.file.pattern1;

public interface Visitor {
	
	public boolean visit(TextFile file);
	
	public boolean visit(XMLFile file);

}
