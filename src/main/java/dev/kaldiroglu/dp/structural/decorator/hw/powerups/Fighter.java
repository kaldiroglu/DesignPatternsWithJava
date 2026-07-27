package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

/** The ConcreteComponent: the character, with no effects on it. */
public final class Fighter implements Combatant {

    private final int baseDamage;

    public Fighter(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    @Override
    public int damage() {
        return baseDamage;
    }
}
