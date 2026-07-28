package dev.kaldiroglu.dp.structural.flyweight.hw.particles;

/** A million particles, four kinds. */
public class Main {

    private static final int PARTICLES = 1_000_000;
    private static final int SPRITE_BYTES = 16 * 1024;

    public static void main(String[] args) {
        ParticleFactory factory = new ParticleFactory(SPRITE_BYTES);
        ParticleSystem system = new ParticleSystem(PARTICLES);

        for (int i = 0; i < PARTICLES; i++) {
            ParticleType type = switch (i % 4) {
                case 0 -> factory.get("Spark", "Orange", 0.1, 30);
                case 1 -> factory.get("Smoke", "Gray", 0.02, 120);
                case 2 -> factory.get("Ember", "Red", 0.15, 60);
                default -> factory.get("Ash", "DarkGray", 0.05, 200);
            };
            system.spawn(type, i % 800, i % 600, (i % 7) - 3, (i % 5) - 2);
        }

        for (int frame = 0; frame < 60; frame++) {
            system.tick();
        }

        System.out.printf("Particles simulated      : %,d%n", system.count());
        System.out.println("Kinds of particle        : 4");
        System.out.println("Requests to the factory  : " + factory.requestCount());
        System.out.println("ParticleType objects     : " + factory.distinctTypes());
        System.out.printf("Sprite bytes held        : %,d%n",
                (long) factory.distinctTypes() * SPRITE_BYTES);
        System.out.printf("Bytes if each particle owned one: %,d%n",
                (long) PARTICLES * SPRITE_BYTES);
        System.out.println();
        System.out.println("After 60 frames, particle 0 : " + system.render(0));
        System.out.printf("Expired particles        : %,d%n", system.expiredCount());
    }
}
