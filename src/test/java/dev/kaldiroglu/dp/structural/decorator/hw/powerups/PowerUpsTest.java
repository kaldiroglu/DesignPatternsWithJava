package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Stacking is ordinary Decorator and needs three assertions. Removal is the half the solution
 * does not answer, and it needs the rest of the file.
 */
class PowerUpsTest {

    private static Combatant fighter() {
        return new Fighter(10);
    }

    @Test
    @DisplayName("effects stack, and the same effect can be granted twice")
    void effectsStack() {
        assertEquals(10, fighter().damage());
        assertEquals(20, new DoubleDamage(fighter()).damage());
        assertEquals(40, new DoubleDamage(new DoubleDamage(fighter())).damage());
        assertEquals(34, new DoubleDamage(new Poison(new DoubleDamage(fighter()), 3)).damage());
    }

    @Test
    @DisplayName("poison never takes damage below zero")
    void poisonHasAFloor() {
        assertEquals(0, new Poison(fighter(), 99).damage());
    }

    @Test
    @DisplayName("a decorator may forward zero times, and then everything below it is wasted")
    void berserkIgnoresWhatIsBeneath() {
        Combatant buffed = new DoubleDamage(new DoubleDamage(fighter()));
        assertEquals(40, buffed.damage());

        // Structurally a decorator; behaviorally a lie about the potions underneath it.
        assertEquals(25, new Berserk(buffed, 25).damage());
    }

    @Test
    @DisplayName("EffectStack reproduces a hand-built chain exactly")
    void theStackMatchesTheHandBuiltChain() {
        EffectStack stack = new EffectStack(fighter())
                .grant(Effect.doubleDamage("battle cry", 5))
                .grant(Effect.poison("venom", 3, 3))
                .grant(Effect.doubleDamage("rage", 8));

        assertEquals(new DoubleDamage(new Poison(new DoubleDamage(fighter()), 3)).damage(),
                stack.damage());
        assertEquals(34, stack.damage());
    }

    @Test
    @DisplayName("an effect can be revoked from the middle — which the chain itself cannot do")
    void revokingRebuildsTheChain() {
        EffectStack stack = new EffectStack(fighter())
                .grant(Effect.doubleDamage("battle cry", 5))
                .grant(Effect.poison("venom", 3, 3))
                .grant(Effect.doubleDamage("rage", 8));

        assertTrue(stack.revoke("venom"));
        assertEquals(40, stack.damage());
        assertEquals(List.of("battle cry", "rage"), stack.activeEffects());

        assertFalse(stack.revoke("venom")); // gone, and saying so is not an error
    }

    @Test
    @DisplayName("effects lapse when the fight moves on")
    void effectsExpire() {
        EffectStack stack = new EffectStack(fighter())
                .grant(Effect.doubleDamage("battle cry", 5))
                .grant(Effect.doubleDamage("rage", 8));

        assertEquals(40, stack.damage());
        stack.advance(6);                       // battle cry lapses at tick 5
        assertEquals(List.of("rage"), stack.activeEffects());
        assertEquals(20, stack.damage());
        stack.advance(3);                       // rage lapses at tick 8
        assertEquals(List.of(), stack.activeEffects());
        assertEquals(10, stack.damage());
    }

    @Test
    @DisplayName("the price of being able to revoke is a fresh chain every time")
    void theChainIsRebuiltNotMutated() {
        EffectStack stack = new EffectStack(fighter()).grant(Effect.doubleDamage("rage", 9));

        // Two calls, two different object graphs. Nothing was mutated in place, because no
        // decorator exposes a way to replace what it wraps — and none should.
        assertNotSame(stack.chain(), stack.chain());
        assertEquals(stack.chain().damage(), stack.chain().damage());

        // The proof that removal is not a chain operation: PowerUp holds its component in a
        // final field and offers no setter.
        assertTrue(java.lang.reflect.Modifier.isFinal(
                java.util.Arrays.stream(PowerUp.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("component"))
                        .findFirst().orElseThrow().getModifiers()));
    }
}
