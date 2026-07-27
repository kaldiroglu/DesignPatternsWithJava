package dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter;

/**
 * <b>Class adapter</b> (GoF p. 142): presents a {@link USPowerSource} as a
 * {@link TurkishPowerSource}.
 *
 * <p>It <b>extends the adaptee</b> and <b>implements the target</b> &ndash; the single-inheritance
 * idiom. Note there is <b>no adaptee field and no delegation</b>: {@code turnOn}/{@code turnOff}
 * map the two-operation Turkish interface onto the adaptee's single toggle by calling the
 * <i>inherited</i> {@link USPowerSource#pushSwitch()} and {@link USPowerSource#isLive()}.</p>
 *
 * <p>Because the adapter <i>is-a</i> {@code USPowerSource}, it may also be used anywhere a
 * {@code USPowerSource} is expected, and it could override the adaptee's behaviour &ndash; two
 * abilities the object adapter does not have for free.</p>
 */
public class USTurkishPowerAdapter extends USPowerSource implements TurkishPowerSource {

    @Override
    public void turnOn() {
        if (!isLive()) {   // inherited from USPowerSource
            pushSwitch();  // inherited from USPowerSource
        }
    }

    @Override
    public void turnOff() {
        if (isLive()) {
            pushSwitch();
        }
    }
}
