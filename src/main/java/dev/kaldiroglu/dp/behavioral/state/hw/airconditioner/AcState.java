package dev.kaldiroglu.dp.behavioral.state.hw.airconditioner;

/**
 * The <b>State</b>. The homework question was where the "which state now?" rule lives. It
 * lives in {@link #running}: every running state uses the same rule, so it is written once.
 */
abstract class AcState {

    abstract String name();

    AcState powerOn(AirConditioner ac) {
        return this;
    }

    AcState powerOff(AirConditioner ac) {
        ac.log("off");
        return off();
    }

    /** The target or the room temperature changed. */
    AcState changed(AirConditioner ac) {
        return running(ac);
    }

    static AcState off() {
        return Off.INSTANCE;
    }

    /** The running state that fits the room and the target. */
    static AcState running(AirConditioner ac) {
        AcState next = ac.room() > ac.target() ? Cooling.INSTANCE
                : ac.room() < ac.target() ? Heating.INSTANCE
                : Idle.INSTANCE;
        ac.log(next.name() + " (room " + ac.room() + ", target " + ac.target() + ")");
        return next;
    }

    static final class Off extends AcState {
        static final Off INSTANCE = new Off();

        String name() { return "off"; }

        @Override
        AcState powerOn(AirConditioner ac) {
            return running(ac);
        }

        @Override
        AcState powerOff(AirConditioner ac) {
            return this;
        }

        @Override
        AcState changed(AirConditioner ac) {
            return this;                    // off: a new target is kept, nothing runs
        }
    }

    static final class Idle extends AcState {
        static final Idle INSTANCE = new Idle();

        String name() { return "idle"; }
    }

    static final class Cooling extends AcState {
        static final Cooling INSTANCE = new Cooling();

        String name() { return "cooling"; }
    }

    static final class Heating extends AcState {
        static final Heating INSTANCE = new Heating();

        String name() { return "heating"; }
    }
}
