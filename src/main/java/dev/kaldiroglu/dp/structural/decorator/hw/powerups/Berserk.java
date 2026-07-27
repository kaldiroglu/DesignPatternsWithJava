package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

/**
 * Ignores what is below it entirely and returns a fixed number.
 * <p>
 * This is a decorator that forwards <em>zero</em> times, which GoF's own description allows.
 * It is worth arguing about: it satisfies the structure completely, and it silently discards
 * every effect underneath it, so a player who drinks a potion and then goes berserk has
 * wasted the potion. The pattern constrains the shape of the code, not the honesty of it.
 */
public final class Berserk extends PowerUp {

    private final int fixedDamage;

    public Berserk(Combatant component, int fixedDamage) {
        super(component);
        this.fixedDamage = fixedDamage;
    }

    @Override
    public int damage() {
        return fixedDamage;
    }
}
