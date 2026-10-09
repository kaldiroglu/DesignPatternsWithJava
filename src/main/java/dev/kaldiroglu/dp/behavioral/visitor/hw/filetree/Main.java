package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

/**
 * Two visitors over one folder tree. The folders do the walking, so neither visitor
 * contains a loop over the children.
 */
public final class Main {

    public static void main(String[] args) {
        Folder root = new Folder("project",
                new FileEntry("README.md", 2),
                new Folder("src",
                        new FileEntry("Main.java", 4),
                        new FileEntry("Order.java", 6)),
                new FileEntry("build.xml", 3));

        ListingVisitor listing = new ListingVisitor();
        root.accept(listing);
        listing.lines().forEach(System.out::println);

        SizeVisitor size = new SizeVisitor();
        root.accept(size);
        System.out.println("Total size: " + size.total());
    }
}
