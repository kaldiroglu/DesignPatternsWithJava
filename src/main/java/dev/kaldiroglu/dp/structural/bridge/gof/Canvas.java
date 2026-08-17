package dev.kaldiroglu.dp.structural.bridge.gof;

import java.util.Arrays;

/**
 * A fixed grid of characters that windows are drawn onto.
 * <p>
 * GoF's example is about a windowing system that must run on more than one platform. A
 * character grid is small enough to assert on in a unit test, and it lets each platform
 * draw with visibly different characters — so "the same window, drawn by a different
 * implementation" is something students can see rather than take on trust.
 * <p>
 * Shared by the {@code problem} and {@code solution} packages, so the two designs are
 * compared on identical output.
 */
public final class Canvas {

    private final char[][] cells;

    public Canvas(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("canvas must have a positive size");
        }
        this.cells = new char[height][width];
        for (char[] row : cells) {
            Arrays.fill(row, ' ');
        }
    }

    public int width() {
        return cells[0].length;
    }

    public int height() {
        return cells.length;
    }

    public void put(int x, int y, char c) {
        if (y >= 0 && y < height() && x >= 0 && x < width()) {
            cells[y][x] = c;
        }
    }

    public void text(int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            put(x + i, y, s.charAt(i));
        }
    }

    /** Draws a rectangle outline using the characters the caller's platform prefers. */
    public void rectangle(int x, int y, int width, int height,
                          char corner, char horizontal, char vertical) {
        for (int i = 1; i < width - 1; i++) {
            put(x + i, y, horizontal);
            put(x + i, y + height - 1, horizontal);
        }
        for (int i = 1; i < height - 1; i++) {
            put(x, y + i, vertical);
            put(x + width - 1, y + i, vertical);
        }
        put(x, y, corner);
        put(x + width - 1, y, corner);
        put(x, y + height - 1, corner);
        put(x + width - 1, y + height - 1, corner);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int y = 0; y < height(); y++) {
            if (y > 0) {
                out.append('\n');
            }
            out.append(cells[y]);
        }
        return out.toString();
    }
}
