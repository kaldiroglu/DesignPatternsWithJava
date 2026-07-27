package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

import java.util.function.UnaryOperator;

/**
 * One granted effect: what it is called, when it lapses, and how to wrap a combatant in it.
 * <p>
 * Note what is stored. Not the decorator <em>object</em>, but the <em>recipe</em> for making
 * one. That is the whole trick behind {@link EffectStack}: you cannot pull a link out of a
 * chain that has already been nested, so you keep the ingredients and cook again.
 *
 * @param name         how the player refers to it
 * @param expiresAtTick the tick at which it lapses
 * @param apply        wraps a combatant in this effect
 */
public record Effect(String name, int expiresAtTick, UnaryOperator<Combatant> apply) {

    public static Effect doubleDamage(String name, int expiresAtTick) {
        return new Effect(name, expiresAtTick, DoubleDamage::new);
    }

    public static Effect poison(String name, int expiresAtTick, int severity) {
        return new Effect(name, expiresAtTick, c -> new Poison(c, severity));
    }

    public static Effect berserk(String name, int expiresAtTick, int fixedDamage) {
        return new Effect(name, expiresAtTick, c -> new Berserk(c, fixedDamage));
    }
}
