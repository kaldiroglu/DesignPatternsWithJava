package dev.kaldiroglu.dp.behavioral.state.pattern;

public interface TCPConnection {
	
	void open();
	
	void close();
	
	void acknowledge();

}
