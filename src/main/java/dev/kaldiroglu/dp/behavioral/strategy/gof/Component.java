package dev.kaldiroglu.dp.behavioral.strategy.gof;

/**
 * One thing on a line of text — a word, an image, a rule.
 * <p>
 * GoF's Strategy chapter (p. 315) is set in a document editor called Lexi, and the thing
 * being laid out is a stream of components. All a line-breaking algorithm needs to know
 * about one is how wide it is and whether the line may be broken after it.
 *
 * @param text     what it says, for reading the result back
 * @param width    how much room it takes
 * @param breakable whether a line may end here
 */
public record Component(String text, int width, boolean breakable) {

    /** A word, which a line may be broken after. */
    public static Component word(String text) {
        return new Component(text, text.length(), true);
    }

    /** Something that must not be split from what follows — a figure with its caption. */
    public static Component glued(String text) {
        return new Component(text, text.length(), false);
    }
}
