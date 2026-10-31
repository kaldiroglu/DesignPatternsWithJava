package dev.kaldiroglu.dp.structural.composite.fileSystem;

/**
 * The six roll-ups that make this tree a Composite: a sum, a count, a maximum, a reduction
 * to an element, a search and a rendering. Each is one call at any depth, with no loop at
 * the call site.
 */
public class SixQuestionsMain {

    public static void main(String[] args) {
        SampleTree tree = new SampleTree();
        Directory home = tree.home;

        home.list();

        System.out.println();
        System.out.println("-- six questions, one call each --");
        System.out.println("size of the whole tree : " + home.size() + " bytes");
        System.out.println("size of Reports only   : " + tree.reports.size() + " bytes");
        System.out.println("elements in the tree   : " + home.count());
        System.out.println("newest anywhere        : " + home.lastModified());
        System.out.println("biggest leaf           : "
                + home.largest().map(Storage::getName).orElse("none"));
        System.out.println("find Selam.java        : "
                + home.find("Selam.java").map(Storage::getName).orElse("not found"));
        System.out.println("over 40 KB, any depth  : "
                + home.findAll(s -> s.size() > 40_000).stream().map(Storage::getName).toList());
        System.out.println("  Directories are in that list because a directory over 40 KB");
        System.out.println("  is over 40 KB. The predicate never asked what kind it was.");
        System.out.println("  Any depth, and not one loop at the call site.");
    }
}
