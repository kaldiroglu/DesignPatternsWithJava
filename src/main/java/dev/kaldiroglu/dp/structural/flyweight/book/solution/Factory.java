package dev.kaldiroglu.dp.structural.flyweight.book.solution;

/**
 * The factory interface the client programs against.
 *
 * <p>GoF's Client "maintains a reference to flyweights" but obtains them only through the
 * factory. Keeping that behind an interface means a client cannot accidentally reach for a
 * constructor, which is what keeps the sharing guarantee true.</p>
 */
public interface Factory {

    /** Returns the shared flyweight for this letter, creating it only on first request. */
    Character createCharacter(char value, boolean upperCase);

    Line createLine(int numberOfCharacters);

    Page createPage(int no, int numberOfLines);

    Book createBook(String name, int numberOfPages);
}
