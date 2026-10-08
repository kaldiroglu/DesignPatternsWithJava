package dev.kaldiroglu.dp.behavioral.state.hw.airconditioner;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 1: an air conditioner's states.
 * <p>
 * Off, Idle, Cooling and Heating. The user turns it on and off and sets a target; a sensor
 * reports the room temperature. Which state comes next depends on the room and the target,
 * so the states decide it. The context only forwards and keeps the temperatures.
 */
public final class AirConditioner {

    private AcState state = AcState.off();
    private int target = 22;
    private int room;
    private final List<String> log = new ArrayList<>();

    public AirConditioner(int room) {
        this.room = room;
    }

    public void powerOn() {
        state = state.powerOn(this);
    }

    public void powerOff() {
        state = state.powerOff(this);
    }

    public void setTarget(int target) {
        this.target = target;
        state = state.changed(this);
    }

    /** The sensor reports a new room temperature. */
    public void roomIs(int room) {
        this.room = room;
        state = state.changed(this);
    }

    public String state() {
        return state.name();
    }

    int target() {
        return target;
    }

    int room() {
        return room;
    }

    void log(String line) {
        log.add(line);
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
