package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public interface Handler {
	
	public Help handleRequest(Context context);
}
