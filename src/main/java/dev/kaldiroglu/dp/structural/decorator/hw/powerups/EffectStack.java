package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

import java.util.ArrayList;
import java.util.List;

/**
 * The honest answer to "how do you take one back off a chain?".
 * <p>
 * <strong>You do not.</strong> A decorator chain is built by nesting, so every link holds the
 * one beneath it and nothing holds the link above. Removing the middle of
 * {@code Poison(DoubleDamage(fighter))} would mean reaching into an object and replacing what
 * it wraps, which no decorator exposes and none should — a chain whose links can be swapped
 * underneath their holders is not transparent any more.
 * <p>
 * So this class keeps the <em>list of active effects</em> and rebuilds the chain whenever it
 * changes. Granting, revoking and expiry are list operations; the chain is derived, cheap, and
 * thrown away. It is also, deliberately, a {@link Combatant} itself, so callers cannot tell.
 * <p>
 * This is GoF's Consequence 1 read carefully. The book says responsibilities can be "added and
 * removed at run-time simply by attaching and detaching them" — and attaching really is
 * simple. Detaching costs you this class.
 */
public final class EffectStack implements Combatant {

    private final Combatant base;
    private final List<Effect> active = new ArrayList<>();
    private int tick;

    public EffectStack(Combatant base) {
        this.base = base;
    }

    public EffectStack grant(Effect effect) {
        active.add(effect);
        return this;
    }

    /** @return true if something was actually removed. */
    public boolean revoke(String name) {
        return active.removeIf(effect -> effect.name().equals(name));
    }

    /** Moves the fight forward, dropping whatever has lapsed. */
    public EffectStack advance(int ticks) {
        tick += ticks;
        active.removeIf(effect -> effect.expiresAtTick() <= tick);
        return this;
    }

    public List<String> activeEffects() {
        return active.stream().map(Effect::name).toList();
    }

    /**
     * Builds the chain from the parts, outermost last.
     * <p>
     * A fresh set of objects every call. That is the price of being able to revoke, and it is
     * only affordable because decorators are small and stateless — which is exactly the shape
     * GoF's implementation notes recommend keeping them.
     */
    public Combatant chain() {
        Combatant combatant = base;
        for (Effect effect : active) {
            combatant = effect.apply().apply(combatant);
        }
        return combatant;
    }

    @Override
    public int damage() {
        return chain().damage();
    }
}
