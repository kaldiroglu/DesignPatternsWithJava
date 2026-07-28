package dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter2;

import dev.kaldiroglu.dp.structural.adapter.electricity.domain.tr.TurkishPowerSource;
import dev.kaldiroglu.dp.structural.adapter.electricity.domain.us.USPowerSource;

/**
 * <b>Object adapter</b>, second version — the same translation, plus work of its own.
 * <p>
 * GoF note that an adapter may do more than rename methods, and this is what that looks like:
 * a safety check and voltage regulation, performed once when the adapter is built rather than
 * on every call.
 * <p>
 * Like {@code powerAdapter1} it <strong>tracks whether the power is on</strong>, and it has to.
 * The Turkish interface offers two distinct operations, {@code turnOn} and {@code turnOff},
 * while the American source offers one {@code pushSwitch} that <em>toggles</em>. Mapping two
 * onto one is only correct if somebody remembers the current state — without it, two
 * consecutive {@code turnOn} calls would leave the appliance off.
 * <p>
 * That is the lesson of this package: adapting an interface is not only renaming methods, it
 * is preserving the <em>meaning</em> across a different operation model.
 */
public class USTurkishPowerAdapter implements TurkishPowerSource {

    private final USPowerSource usPowerSource;
    private boolean on;

    public USTurkishPowerAdapter(USPowerSource usPowerSource) {
        System.out.println("\nUSTurkishPowerAdapter: converting USPowerSource to TurkishPowerSource");
        this.usPowerSource = usPowerSource;
        convert110To220();
    }

    @Override
    public void providePowerAt220V() {
        usPowerSource.providePowerAt110V();
        convert110To220();
    }

    /** Idempotent, as the Turkish interface implies: "on" twice still means on. */
    @Override
    public void turnOn() {
        if (!on) {
            usPowerSource.pushSwitch();
            on = true;
        }
    }

    @Override
    public void turnOff() {
        if (on) {
            usPowerSource.pushSwitch();
            on = false;
        }
    }

    /** The extra work this adapter does beyond translating. */
    private void convert110To220() {
        check();
        regulateVoltage();
        System.out.println("USTurkishPowerAdapter: converting from 110V to 220V");
    }

    private void check() {
        System.out.println("USTurkishPowerAdapter: making safety checks.");
    }

    private void regulateVoltage() {
        System.out.println("USTurkishPowerAdapter: regulating voltage.");
    }
}
