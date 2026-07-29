package dev.kaldiroglu.dp.structural.flyweight.book.correct;

/**
 * Client demo.
 *
 * <p>Types a real paragraph rather than nine hand-written letters, because the pattern only
 * becomes visible at volume: the saving is the gap between how many characters the text has
 * and how many distinct ones it uses, and that gap cannot be seen in a nine-letter sample.</p>
 */
public class Main {

    private static final String[] TEXT = {
            "Flyweight uses sharing to support large numbers of",
            "fine-grained objects efficiently. A flyweight is a",
            "shared object that can be used in multiple contexts",
            "simultaneously, and it cannot make assumptions about",
            "the context in which it operates.",
    };

    public static void main(String[] args) {
        CharacterFactory factory = new CharacterFactory();

        Book book = factory.createBook("Design Patterns", 1);
        Page page = factory.createPage(1, TEXT.length);

        for (String text : TEXT) {
            Line line = factory.createLine(text.length());
            for (char c : text.toCharArray()) {
                // The case of the letter is intrinsic, so it is part of what gets shared.
                boolean upperCase = java.lang.Character.isUpperCase(c);
                line.add(factory.createCharacter(java.lang.Character.toLowerCase(c), upperCase));
            }
            page.add(line);
        }
        book.add(page);

        System.out.println(book);

        System.out.println("Character occurrences in the book : " + book.characterCount());
        System.out.println("Requests made to the factory      : " + factory.requestCount());
        System.out.println("Distinct flyweights allocated     : " + factory.createdCount());
        System.out.println("Occurrences that cost no object   : " + factory.savedCount());

        // The claim the pattern actually makes: one object, in many places at once.
        Line first = page.getLines().get(0);
        Line second = page.getLines().get(1);
        Character eInLineOne = first.characterAt(indexOf(first, 'e'));
        Character eInLineTwo = second.characterAt(indexOf(second, 'e'));

        System.out.println();
        System.out.println("The 'e' on line 1 and the 'e' on line 2 are the same object: "
                + (eInLineOne == eInLineTwo));
    }

    /** Position of the first occurrence of a letter on a line — extrinsic state, read live. */
    private static int indexOf(Line line, char letter) {
        for (int i = 0; i < line.length(); i++) {
            if (line.characterAt(i).getValue() == letter) {
                return i;
            }
        }
        throw new IllegalArgumentException("no '" + letter + "' on this line");
    }
}
