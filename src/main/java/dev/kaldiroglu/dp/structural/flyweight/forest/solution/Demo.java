package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

public class Demo {
    public static void main(String[] args) {
        Forest forest = new Forest();

        byte[] oakTexture = new byte[1_000_000];
        byte[] pineTexture = new byte[1_000_000];

        for (int i = 0; i < 10_000; i++) {
            forest.plant(i, i, "Oak", "Green", oakTexture);
            forest.plant(i, -i, "Pine", "DarkGreen", pineTexture);
        }

        System.out.println("Trees planted: " + forest.size());
        System.out.println("Distinct TreeTypes in memory: " + forest.distinctTypes());
        // forest.render(); // skipped to keep output short
    }
}
