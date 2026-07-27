package dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter;

/**
 * <b>Adaptee</b> participant: an existing American source whose interface is incompatible with
 * {@link TurkishPowerSource}. A single {@code pushSwitch} toggles power on and off (US style), and
 * {@code isLive} reports the current state. We reuse this class without modifying it.
 */
public class USPowerSource {

    private boolean live;

    /** US style: one switch toggles power on/off. */
    public void pushSwitch() {
        live = !live;
        System.out.println("    US source: switch pushed -> " + (live ? "LIVE (110V/60Hz)" : "OFF"));
    }

    public boolean isLive() {
        return live;
    }
}
