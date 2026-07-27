package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import java.util.Arrays;

/**
 * A fixed grid of characters that visual components draw themselves onto.
 * <p>
 * The GoF example talks about drawing on a screen. A character grid keeps the example
 * honest — a border really is drawn, and you can see where it lands — while staying
 * small enough to assert on in a unit test.
 * <p>
 * This class is deliberately shared by the {@code problem} and {@code solution} packages
 * so that the two designs are compared on identical output.
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

    /** Writes one character, ignoring anything that falls outside the grid. */
    public void put(int x, int y, char c) {
        if (y >= 0 && y < height() && x >= 0 && x < width()) {
            cells[y][x] = c;
        }
    }

    /** Writes a string left to right starting at (x, y). */
    public void text(int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            put(x + i, y, s.charAt(i));
        }
    }

    /** Draws the outline of a rectangle whose top-left corner is (x, y). */
    public void rectangle(int x, int y, int width, int height) {
        for (int i = 1; i < width - 1; i++) {
            put(x + i, y, '-');
            put(x + i, y + height - 1, '-');
        }
        for (int i = 1; i < height - 1; i++) {
            put(x, y + i, '|');
            put(x + width - 1, y + i, '|');
        }
        put(x, y, '+');
        put(x + width - 1, y, '+');
        put(x, y + height - 1, '+');
        put(x + width - 1, y + height - 1, '+');
    }

    /** The grid as text, one line per row, without a trailing newline. */
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
