package dev.kaldiroglu.dp.behavioral.state.person;

/**
 * The person forwards each greeting to the current emotional state. The caller changes the
 * state with {@link #setEmotionalState}; the states never do.
 */
public class Person {

	private EmotionalState emotionalState;

	public Person(EmotionalState emotionalState) {
		this.emotionalState = emotionalState;
	}

	public void setEmotionalState(EmotionalState emotionalState) {
		this.emotionalState = emotionalState;
	}

	public String sayGoodbye() {
		return emotionalState.sayGoodbye();
	}

	public String sayHello() {
		return emotionalState.sayHello();
	}

}