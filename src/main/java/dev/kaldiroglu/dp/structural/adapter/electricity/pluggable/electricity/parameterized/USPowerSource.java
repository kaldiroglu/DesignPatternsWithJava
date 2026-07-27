package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized;

/**
 * <b>Adaptee</b> #1: an American source with its own, incompatible interface ({@code pushSwitch}).
 */
public final class USPowerSource {

    public void pushSwitch() {
        System.out.println("US source: switch pushed (110V / 60Hz)");
    }
}
