package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** The cached totals, and the upward walk that clears them. */
public class CacheMain {

    public static void main(String[] args) {
        SampleTree tree = new SampleTree();
        Directory home = tree.home;
        home.size();   // fills the caches, so the three calls below find them full

        System.out.println("-- the cache, and why the parent reference exists --");
        Directory.resetRecomputations();
        home.size();
        home.size();
        home.size();
        System.out.println("three calls, nothing changed : " + Directory.recomputations()
                + " totals computed — the tree is not walked at all");

        tree.addNotes();                         // three levels down
        home.size();
        System.out.println("after one file is added      : " + Directory.recomputations()
                + " — exactly Reports, Dev and akin");
        System.out.println("  Invalidation runs upward, which is why an element keeps a");
        System.out.println("  reference to its parent. Nothing below the change is touched.");
    }
}
