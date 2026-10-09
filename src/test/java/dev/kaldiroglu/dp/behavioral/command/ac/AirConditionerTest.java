package dev.kaldiroglu.dp.behavioral.command.ac;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The air conditioner and its wall switch. The room starts at 22 degrees. The classes print
 * to standard output and have no getters, so the tests check what they print.
 */
class AirConditionerTest {

    /** Runs the action and returns what it printed, one line per element. */
    private static List<String> printed(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString().lines().toList();
    }

    @Test
    @DisplayName("turning on below the room temperature starts the cooler")
    void turnOnCools() {
        ACSwitch acSwitch = new ACSwitch();
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 20",
                "Cooler is turned on. Target temperature is: 20"),
                printed(() -> acSwitch.turnOn(20)));
    }

    @Test
    @DisplayName("turning on above the room temperature starts the heater")
    void turnOnHeats() {
        ACSwitch acSwitch = new ACSwitch();
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 25",
                "Heater is turned on. Target temperature is: 25"),
                printed(() -> acSwitch.turnOn(25)));
    }

    @Test
    @DisplayName("turning on at the room temperature starts only the fan")
    void turnOnAtRoomTemperature() {
        ACSwitch acSwitch = new ACSwitch();
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 22"),
                printed(() -> acSwitch.turnOn(22)));
    }

    @Test
    @DisplayName("turning on twice, or off twice, says so")
    void onTwiceOffTwice() {
        ACSwitch acSwitch = new ACSwitch();
        acSwitch.turnOn(22);
        assertEquals(List.of("", "AirConditioner is already on!"),
                printed(() -> acSwitch.turnOn(22)));
        printed(acSwitch::turnOff);
        assertEquals(List.of("AirConditioner is already off!", ""),
                printed(acSwitch::turnOff));
    }

    @Test
    @DisplayName("turning off prints one line and an empty one")
    void turnOff() {
        ACSwitch acSwitch = new ACSwitch();
        acSwitch.turnOn(22);
        assertEquals(List.of("AirConditioner is turned off.", ""), printed(acSwitch::turnOff));
    }

    @Test
    @DisplayName("turning off keeps the room temperature: back on at 20, only the fan starts")
    void turnOffKeepsTheRoomTemperature() {
        ACSwitch acSwitch = new ACSwitch();
        acSwitch.turnOn(20);                  // cools the room from 22 to 20
        printed(acSwitch::turnOff);
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 20"),
                printed(() -> acSwitch.turnOn(20)));
    }

    @Test
    @DisplayName("the heater and the cooler need the air conditioner to be on")
    void heaterAndCoolerNeedPower() {
        ACSwitch acSwitch = new ACSwitch();
        List<String> lines = printed(() -> {
            acSwitch.turnOnCooler(18);
            acSwitch.turnOnHeater(25);
        });
        assertEquals(List.of("AirConditioner is off, please first turn it on!",
                "AirConditioner is off, please first turn it on!"), lines);
    }

    @Test
    @DisplayName("the cooler only cools, and the heater only heats")
    void eachOnlyGoesOneWay() {
        ACSwitch acSwitch = new ACSwitch();
        acSwitch.turnOn(22);
        List<String> lines = printed(() -> {
            acSwitch.turnOnCooler(25);   // warmer than the room: ignored
            acSwitch.turnOnCooler(18);
            acSwitch.turnOnHeater(15);   // colder than the room: ignored
            acSwitch.turnOnHeater(23);
        });
        assertEquals(List.of("Cooler is turned on. Target temperature is: 18",
                "Heater is turned on. Target temperature is: 23"), lines);
    }

    @Test
    @DisplayName("each command passes its request to the air conditioner")
    void commandsForward() {
        AirConditioner ac = new AirConditioner(new Temperature(22));
        List<String> lines = printed(() -> {
            new TurnOnCommand(ac).execute(new Temperature(22));
            new HeatCommand(ac).execute(new Temperature(24));
            new CoolCommand(ac).execute(new Temperature(19));
            new TurnOffCommand(ac).execute(null);
        });
        assertEquals(List.of("", "Fan is turned on. Target temperature is: 22",
                "Heater is turned on. Target temperature is: 24",
                "Cooler is turned on. Target temperature is: 19",
                "AirConditioner is turned off.", ""), lines);
    }

    @Test
    @DisplayName("the switch holds its four requests as Command objects")
    void theSwitchHoldsCommands() {
        long commands = Arrays.stream(ACSwitch.class.getDeclaredFields())
                .map(Field::getType)
                .filter(Command.class::equals)
                .count();
        assertEquals(4, commands);
        assertTrue(printed(ACSwitch::new).isEmpty(), "creating the switch prints nothing");
    }

    @Test
    @DisplayName("undo and redo are declared, not yet written: they do nothing")
    void undoAndRedoDoNothingYet() {
        AirConditioner ac = new AirConditioner(new Temperature(22));
        List<Command> all = List.of(new TurnOnCommand(ac), new TurnOffCommand(ac),
                new HeatCommand(ac), new CoolCommand(ac));
        List<String> lines = printed(() -> all.forEach(c -> {
            c.undo();
            c.redo();
        }));
        assertTrue(lines.isEmpty());
    }
}
