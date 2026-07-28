package dev.kaldiroglu.dp.structural.flyweight.book.solution;

import java.util.ArrayList;
import java.util.List;

/** A named book of pages. Unshared, like its pages and lines. */
public class Book {

    private static final String PAGE_BREAK = "--- page break ---";

    private final String name;
    private final int capacity;
    private final List<Page> pages;

    Book(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.pages = new ArrayList<>(capacity);
    }

    public boolean add(Page page) {
        if (pages.size() >= capacity) {
            return false;
        }
        pages.add(page);
        return true;
    }

    public String name() {
        return name;
    }

    public List<Page> getPages() {
        return List.copyOf(pages);
    }

    /** Total character occurrences in the book — not the number of objects holding them. */
    public int characterCount() {
        int total = 0;
        for (Page page : pages) {
            for (Line line : page.getLines()) {
                total += line.length();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Page page : pages) {
            sb.append(page);
            sb.append(PAGE_BREAK).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
