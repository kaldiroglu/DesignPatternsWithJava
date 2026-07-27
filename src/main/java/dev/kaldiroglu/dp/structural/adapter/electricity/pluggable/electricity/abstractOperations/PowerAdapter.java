package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.abstractOperations;

abstract class PowerAdapter implements TurkishPowerSource {
    @Override
    public final void turnOn() {
        deliver();
    }

    @Override
    public final void turnOff() {
        cut();
    }

    protected abstract void deliver();

    protected abstract void cut();
}
