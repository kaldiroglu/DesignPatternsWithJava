package dev.kaldiroglu.dp.structural.flyweight.hw.particles;

import java.util.HashMap;
import java.util.Map;

/** <b>FlyweightFactory</b>. Four kinds of particle for a million particles on screen. */
public class ParticleFactory {

    private final Map<String, ParticleType> types = new HashMap<>();
    private final int spriteBytes;
    private int requests;

    public ParticleFactory(int spriteBytes) {
        this.spriteBytes = spriteBytes;
    }

    public ParticleType get(String name, String color, double mass, int lifetimeTicks) {
        requests++;
        String key = name + "|" + color + "|" + mass + "|" + lifetimeTicks;
        return types.computeIfAbsent(key, k ->
                new ParticleType(name, color, mass, lifetimeTicks, new byte[spriteBytes]));
    }

    public int distinctTypes() {
        return types.size();
    }

    public int requestCount() {
        return requests;
    }
}
