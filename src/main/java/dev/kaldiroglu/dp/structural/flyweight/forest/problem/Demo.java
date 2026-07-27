package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

public class Demo {
    public static void main(String[] args) {
        Forest forest = new Forest();

        byte[] oakTexture = new byte[1_000_000]; // pretend: 1MB
        byte[] pineTexture = new byte[1_000_000];

        // 10,000 oaks + 10,000 pines = 20,000 separate texture references stored.
        for (int i = 0; i < 10_000; i++) {
            forest.plant(i, i, "Oak", "Green", oakTexture);
            forest.plant(i, -i, "Pine", "DarkGreen", pineTexture);
        }

        System.out.println("Trees planted: " + forest.size());
        // forest.render(); // skipped to keep output short
    }
}
