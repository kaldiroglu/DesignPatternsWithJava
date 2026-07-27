package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

/**
 * Homework 3 — the power-up.
 * <p>
 * The stacking half is ordinary Decorator. The removal half is the one with no clean answer,
 * and finding that out is the point of the exercise.
 */
public class Main {

    public static void main(String[] args) {
        Combatant fighter = new Fighter(10);

        // Built by hand, the way every decorator example is built.
        Combatant buffed = new DoubleDamage(new Poison(new DoubleDamage(fighter), 3));
        System.out.println("fighter                                    " + fighter.damage());
        System.out.println("double, poisoned, doubled again            " + buffed.damage());
        System.out.println("  ... and there is no way to cure the poison in that chain.");
        System.out.println();

        // Built from a list, which can be changed.
        EffectStack stack = new EffectStack(fighter)
                .grant(Effect.doubleDamage("battle cry", 5))
                .grant(Effect.poison("venom", 3, 3))
                .grant(Effect.doubleDamage("rage", 8));

        System.out.println("same three effects, via EffectStack         " + stack.damage());
        System.out.println("active                                     " + stack.activeEffects());

        stack.revoke("venom");
        System.out.println("after an antidote                          " + stack.damage());
        System.out.println("active                                     " + stack.activeEffects());

        stack.advance(6);
        System.out.println("six ticks later, battle cry has lapsed     " + stack.damage());
        System.out.println("active                                     " + stack.activeEffects());

        System.out.println();
        System.out.println("Every call to chain() builds new objects: " + (stack.chain() != stack.chain()));
        System.out.println("Attaching is simple. Detaching costs you EffectStack.");
    }
}
