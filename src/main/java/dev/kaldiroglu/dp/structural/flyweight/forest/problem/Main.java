package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

/**
 * Plants a forest the obvious way and reports what it cost.
 *
 * <p>The numbers are deliberately smaller than a real game's. Ten thousand trees each holding
 * a megabyte would be twenty gigabytes, which does not fit in a demo — and an example that
 * cannot be run is an example nobody checks.</p>
 */
public class Main {

    static final int TREES_PER_KIND = 5_000;
    static final int TEXTURE_BYTES = 4_096;

    public static void main(String[] args) {
        TextureLoader.reset();
        Forest forest = new Forest();

        byte[] oakTexture = new byte[TEXTURE_BYTES];
        byte[] pineTexture = new byte[TEXTURE_BYTES];

        for (int i = 0; i < TREES_PER_KIND; i++) {
            forest.plant(i, i, "Oak", "Green", oakTexture);
            forest.plant(i, -i, "Pine", "DarkGreen", pineTexture);
        }

        System.out.println("Trees planted        : " + forest.size());
        System.out.println("Distinct kinds       : 2");
        System.out.println("Textures loaded      : " + TextureLoader.loadCount());
        System.out.printf ("Texture bytes held   : %,d%n", forest.textureBytes());
        System.out.println();
        System.out.println("Two kinds of tree, and every one of them paid for its own texture.");
    }
}
