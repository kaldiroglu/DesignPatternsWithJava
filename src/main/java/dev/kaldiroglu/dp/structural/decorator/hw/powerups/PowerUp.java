package dev.kaldiroglu.dp.structural.decorator.hw.powerups;

import java.util.Objects;

/**
 * The Decorator: one effect, wrapped around whatever it was granted to.
 * <p>
 * Effects are granted in the middle of a fight and they expire, which is the half of this
 * problem the solution does not answer. See {@link EffectStack}.
 */
public abstract class PowerUp implements Combatant {

    protected final Combatant component;

    protected PowerUp(Combatant component) {
        this.component = Objects.requireNonNull(component);
    }
}
