package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

/** Multiplies. Stacks, so two of these are worth four times the base. */
public final class DoubleDamage extends PowerUp {

    public DoubleDamage(Combatant component) {
        super(component);
    }

    @Override
    public int damage() {
        return component.damage() * 2;
    }
}
