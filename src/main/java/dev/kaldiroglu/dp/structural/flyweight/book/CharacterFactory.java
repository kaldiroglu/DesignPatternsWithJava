package dev.kaldiroglu.dp.structural.flyweight.book;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>FlyweightFactory</b> (GoF, p. 198).
 *
 * <p>"Creates and manages flyweight objects... ensures that flyweights are shared properly.
 * When a client requests a flyweight, the FlyweightFactory object supplies an existing
 * instance or creates one, if none exists."</p>
 *
 * <p>This class used to hold a field for the pool and never read or wrote it, so every
 * request allocated. It now does the thing the participant is named for, and
 * {@link #createdCount()} makes the difference a number rather than a claim.</p>
 *
 * <p>The key must identify the intrinsic state <em>completely</em>. Here that means the
 * letter and its case together: {@code ('t', true)} and {@code ('t', false)} are different
 * flyweights, because they render differently. A key that left out {@code upperCase} would
 * hand back a lower-case 't' to a caller that asked for a capital — the commonest way a
 * flyweight factory goes wrong, and the reason to write the key out rather than reach for
 * the first field to hand.</p>
 */
public class CharacterFactory implements Factory {

    /** One entry per distinct piece of intrinsic state, not per occurrence in the book. */
    private final Map<String, Character> characters = new HashMap<>();

    private int requests;

    @Override
    public Character createCharacter(char value, boolean upperCase) {
        requests++;
        String key = value + (upperCase ? "^" : "");
        return characters.computeIfAbsent(key, k -> new Character(value, upperCase));
    }

    @Override
    public Line createLine(int numberOfCharacters) {
        return new Line(numberOfCharacters);
    }

    @Override
    public Page createPage(int no, int numberOfLines) {
        return new Page(no, numberOfLines);
    }

    @Override
    public Book createBook(String name, int numberOfPages) {
        return new Book(name, numberOfPages);
    }

    /** Distinct flyweights actually allocated — the size of the pool. */
    public int createdCount() {
        return characters.size();
    }

    /** How many times a character was asked for, shared or not. */
    public int requestCount() {
        return requests;
    }

    /** Occurrences that cost no object at all, because the pool already held one. */
    public int savedCount() {
        return requests - characters.size();
    }
}
