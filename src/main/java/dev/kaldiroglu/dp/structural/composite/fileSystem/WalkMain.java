package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

/** Composite and Iterator used together: a depth-first walk of the whole tree. */
public class WalkMain {

    public static void main(String[] args) {
        SampleTree tree = SampleTree.afterChanges();

        System.out.println("-- walk the tree depth-first --");
        StorageIterator walker = tree.home.iterator();
        while (walker.hasNext()) {
            System.out.println("  " + walker.next().getName());
        }
    }
}
