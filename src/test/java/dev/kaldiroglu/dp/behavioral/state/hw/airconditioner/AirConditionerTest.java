package dev.kaldiroglu.dp.behavioral.state.hw.airconditioner;

import dev.kaldiroglu.dp.behavioral.state.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 1: the air conditioner. The "which state now?" rule is written once. */
class AirConditionerTest {

    @Test
    @DisplayName("on in a warm room it cools; at the target it is idle; below the target it heats")
    void theRoomAndTheTargetChooseTheState() {
        AirConditioner ac = new AirConditioner(26);
        ac.powerOn();
        assertEquals("cooling", ac.state());
        ac.roomIs(22);
        assertEquals("idle", ac.state());
        ac.setTarget(24);
        assertEquals("heating", ac.state());
    }

    @Test
    @DisplayName("when it is off, a new target or room temperature does not start it")
    void offStaysOff() {
        AirConditioner ac = new AirConditioner(26);
        ac.setTarget(20);
        ac.roomIs(30);
        assertEquals("off", ac.state());
        assertEquals(List.of(), ac.log());
    }

    @Test
    @DisplayName("the rule is written once: only the base class AcState declares running and changed")
    void theRuleIsWrittenOnce() {
        for (Class<?> state : AcState.class.getDeclaredClasses()) {
            List<String> methods = Arrays.stream(state.getDeclaredMethods()).map(Method::getName).toList();
            assertFalse(methods.contains("running"), state.getSimpleName());
        }
        List<String> base = Arrays.stream(AcState.class.getDeclaredMethods()).map(Method::getName).toList();
        assertTrue(base.contains("running"));
        assertTrue(base.contains("changed"));
    }

    @Test
    @DisplayName("Main prints cooling, idle, heating, off, and the log")
    void mainOutput() {
        assertEquals(List.of(
                "On in a 26 degree room, target 22: cooling",
                "The room is now 22:                idle",
                "The target is now 24:              heating",
                "Off, then the target is set to 20: off",
                "Log: [cooling (room 26, target 22), idle (room 22, target 22), heating (room 22, target 24), off]"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
