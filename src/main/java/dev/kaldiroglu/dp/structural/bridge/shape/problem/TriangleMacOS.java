package dev.kaldiroglu.dp.structural.bridge.shape.problem;

/**
 * One cell of the grid: a triangle, on MacOS.
 * <p>
 * Adding this shape kind cost <strong>three</strong> classes — the abstract Triangle and one leaf
 * per device — and the next device will cost one more leaf for every shape already on the
 * menu. That is m x n, growing.
 */
public class TriangleMacOS extends Triangle {

    public TriangleMacOS(String name) {
        super(name);
    }

    @Override
    public void draw() {
        System.out.println("  MacOS: drawing a triangle.");
    }

    @Override
    public void erase() {
        System.out.println("  MacOS: erasing a triangle.");
    }
}
