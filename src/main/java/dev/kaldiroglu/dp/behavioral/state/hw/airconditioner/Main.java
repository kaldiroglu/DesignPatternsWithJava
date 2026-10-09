package dev.kaldiroglu.dp.behavioral.state.hw.airconditioner;

/**
 * Turns an air conditioner on in a warm room, lets the room cool down, sets a higher
 * target and turns it off. The states choose the next state from the room and the target.
 */
public final class Main {

    public static void main(String[] args) {
        AirConditioner ac = new AirConditioner(26);
        ac.powerOn();
        System.out.println("On in a 26 degree room, target 22: " + ac.state());
        ac.roomIs(22);
        System.out.println("The room is now 22:                " + ac.state());
        ac.setTarget(24);
        System.out.println("The target is now 24:              " + ac.state());
        ac.powerOff();
        ac.setTarget(20);
        System.out.println("Off, then the target is set to 20: " + ac.state());
        System.out.println("Log: " + ac.log());
    }
}
