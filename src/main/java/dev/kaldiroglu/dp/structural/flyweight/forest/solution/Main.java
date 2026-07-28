package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

/** The same forest, planted through a factory. Same trees on screen, two textures in memory. */
public class Main {

    static final int TREES_PER_KIND = 5_000;
    static final int TEXTURE_BYTES = 4_096;

    public static void main(String[] args) {
        TextureLoader loader = new TextureLoader(TEXTURE_BYTES);
        Forest forest = new Forest(new TreeFactory(loader));

        for (int i = 0; i < TREES_PER_KIND; i++) {
            forest.plant(i, i, "Oak", "Green");
            forest.plant(i, -i, "Pine", "DarkGreen");
        }

        System.out.println("Trees planted        : " + forest.size());
        System.out.println("Distinct TreeTypes   : " + forest.distinctTypes());
        System.out.println("Textures loaded      : " + loader.loadCount());
        System.out.printf ("Texture bytes held   : %,d%n", forest.textureBytes());
        System.out.println();
        System.out.println("The same " + forest.size() + " trees are on screen. Two textures are in memory.");
    }
}
