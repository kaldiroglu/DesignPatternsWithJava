package dev.kaldiroglu.dp.structural.flyweight.book.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Client</b>-side container, and the place the extrinsic state actually lives.
 *
 * <p>This is the part worth reading twice. A character's position is no longer stored
 * anywhere: it <em>is</em> the index in this list. The problem version wrote a
 * {@code position} field onto the character itself, which is what made the character
 * unshareable — two occurrences of the same letter would have fought over one field.</p>
 *
 * <p>So the extrinsic state did not move to a new home. It turned out never to have needed
 * one, which is the cheapest form this pattern takes.</p>
 */
public class Line {

    private final List<Character> chars;
    private final int capacity;

    Line(int capacity) {
        this.capacity = capacity;
        this.chars = new ArrayList<>(capacity);
    }

    /**
     * Appends a character if there is room.
     *
     * <p>The problem version declared itself full at {@code capacity + 1}, so a line built
     * for ten characters accepted eleven.</p>
     *
     * @return true if the character was added, false if the line is full
     */
    public boolean add(Character character) {
        if (isFull()) {
            return false;
        }
        chars.add(character);
        return true;
    }

    public boolean isFull() {
        return chars.size() >= capacity;
    }

    public int capacity() {
        return capacity;
    }

    public int length() {
        return chars.size();
    }

    /** The character at a position — the extrinsic state, read rather than stored. */
    public Character characterAt(int position) {
        return chars.get(position);
    }

    public List<Character> getChars() {
        return List.copyOf(chars);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Character character : chars) {
            sb.append(character.getValue());
        }
        return sb.toString();
    }
}
