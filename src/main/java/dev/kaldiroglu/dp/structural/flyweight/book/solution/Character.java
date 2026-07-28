package dev.kaldiroglu.dp.structural.flyweight.book.solution;

/**
 * <b>ConcreteFlyweight</b> (GoF, p. 198).
 *
 * <p>Holds the intrinsic state of a character and nothing else: the letter itself, and
 * whether it is capitalized. Both are properties of the character <em>wherever it appears</em>,
 * which is exactly the test for intrinsic state — and it is why one instance can stand for
 * every occurrence of that letter in the whole book.</p>
 *
 * <p>Two things make sharing safe here, and they have to be read together:</p>
 * <ul>
 *   <li>The class is <b>immutable</b>. There is no setter, and both fields are final.</li>
 *   <li>The constructor is <b>package-private</b>, so only {@link CharacterFactory} can mint
 *       instances. Nothing outside this package can create a second copy of 'e' and defeat
 *       the pooling.</li>
 * </ul>
 *
 * <p>Compare with {@code book.problem.Character}, which stores {@code line} and
 * {@code position}. Those describe where an occurrence <em>sits</em>, not what it
 * <em>is</em> — extrinsic state, and storing it is what made that version unshareable.</p>
 */
public final class Character {

    private final char value;
    private final boolean upperCase;

    /** Package-private: instances come from {@link CharacterFactory}, never from a client. */
    Character(char value, boolean upperCase) {
        this.value = value;
        this.upperCase = upperCase;
    }

    /**
     * The character as it should be rendered. The problem version stored {@code upperCase}
     * and then ignored it here, so a capital letter came out lower case.
     */
    public char getValue() {
        return upperCase ? java.lang.Character.toUpperCase(value) : value;
    }

    /** The letter as it was requested, before capitalization is applied. */
    public char rawValue() {
        return value;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
