package dev.kaldiroglu.dp.behavioral.state.pattern;

public interface TCPState {
	
	void open();

	void close();

	void acknowledge();

}
