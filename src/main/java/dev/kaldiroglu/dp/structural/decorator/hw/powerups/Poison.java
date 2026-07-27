package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

/** Subtracts a flat amount, never below zero. */
public final class Poison extends PowerUp {

    private final int severity;

    public Poison(Combatant component, int severity) {
        super(component);
        this.severity = severity;
    }

    @Override
    public int damage() {
        return Math.max(0, component.damage() - severity);
    }
}
