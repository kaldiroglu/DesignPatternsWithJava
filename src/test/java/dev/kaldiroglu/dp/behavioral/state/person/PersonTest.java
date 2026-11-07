package dev.kaldiroglu.dp.behavioral.state.person;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The person: the caller changes the emotional state, and the person forwards greetings. */
class PersonTest {

    @Test
    @DisplayName("Main prints both greetings in each state")
    void mainPrintsBothStates() {
        assertEquals(List.of(
                "Hello in happy state: Hello, friend!",
                "Goodbye in happy state: Bye, friend!",
                "",
                "Hello in sad state: Hello. Sniff, sniff.",
                "Goodbye in sad state: Bye. Sniff, sniff."), Printed.by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("the person is the context, not a state")
    void thePersonIsNotAState() {
        assertFalse(EmotionalState.class.isAssignableFrom(Person.class));
    }

    @Test
    @DisplayName("a greeting does not change the state")
    void aGreetingDoesNotChangeTheState() {
        Person person = new Person(new SadState());
        person.sayHello();
        assertEquals("Bye. Sniff, sniff.", person.sayGoodbye());
    }
}
