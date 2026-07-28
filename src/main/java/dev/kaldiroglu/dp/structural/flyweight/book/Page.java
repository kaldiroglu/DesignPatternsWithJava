package dev.kaldiroglu.dp.structural.flyweight.book;

import java.util.ArrayList;
import java.util.List;

/** A numbered page of lines. Unshared: every page has its own content. */
public class Page {

    private final int no;
    private final int capacity;
    private final List<Line> lines;

    Page(int no, int capacity) {
        this.no = no;
        this.capacity = capacity;
        this.lines = new ArrayList<>(capacity);
    }

    public boolean add(Line line) {
        if (lines.size() >= capacity) {
            return false;
        }
        lines.add(line);
        return true;
    }

    public int no() {
        return no;
    }

    public List<Line> getLines() {
        return List.copyOf(lines);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Line line : lines) {
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
