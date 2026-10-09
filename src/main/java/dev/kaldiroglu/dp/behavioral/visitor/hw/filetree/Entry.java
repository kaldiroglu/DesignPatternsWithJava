package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

/**
 * Homework 2: who walks the tree?
 * <p>
 * Here the structure walks itself: {@link Folder#accept} visits the folder and then passes
 * the visitor to each child. Every visitor gets the walk for free and cannot get it wrong.
 * The cost is that every visitor gets the same walk. GoF implementation issue 2 (who is
 * responsible for traversing the object structure?) lists this as the first answer.
 */
public interface Entry {

    String name();

    void accept(EntryVisitor visitor);
}
