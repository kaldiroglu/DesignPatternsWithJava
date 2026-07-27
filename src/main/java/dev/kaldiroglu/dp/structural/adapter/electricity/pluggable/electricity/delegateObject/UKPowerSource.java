package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

/**
 * <b>Adaptee</b> #2: a British source with yet another interface ({@code flipToggle}). Having two
 * differently-shaped adaptees is what makes the pluggable adapter worth it: one adapter class,
 * many sources.
 */
public final class UKPowerSource {

    public void flipToggle() {
        System.out.println("UK source: toggle flipped (230V / 50Hz)");
    }
}
