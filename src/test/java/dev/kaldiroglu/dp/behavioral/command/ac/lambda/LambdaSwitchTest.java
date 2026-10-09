package dev.kaldiroglu.dp.behavioral.command.ac.lambda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The air conditioner's switch with its requests as functions. */
class LambdaSwitchTest {

    @Test
    @DisplayName("the switch holds four functions and no Command")
    void fourFunctions() {
        List<Class<?>> types = Arrays.stream(ACSwitch.class.getDeclaredFields())
                .map(Field::getType).toList();
        assertEquals(3, types.stream().filter(Consumer.class::equals).count());
        assertEquals(1, types.stream().filter(Runnable.class::equals).count());
        assertFalse(types.contains(dev.kaldiroglu.dp.behavioral.command.ac.Command.class));
    }

    @Test
    @DisplayName("the steps of Person print the same lines through either switch")
    void sameOutputAsTheCommandSwitch() {
        assertEquals(by(() -> dev.kaldiroglu.dp.behavioral.command.ac.Person.main(new String[0])),
                by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("turning on below the room temperature starts the cooler")
    void turnOnCools() {
        ACSwitch acSwitch = new ACSwitch();
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 20",
                "Cooler is turned on. Target temperature is: 20"),
                by(() -> acSwitch.turnOn(20)));
    }
}
