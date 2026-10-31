package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** Copying a directory: a deep copy, detached from any parent. */
public class CopyMain {

    public static void main(String[] args) {
        SampleTree tree = SampleTree.afterChanges();

        System.out.println("-- copy the Reports directory --");
        Storage duplicate = tree.reports.copy();
        duplicate.rename("Reports (copy)");
        System.out.println("copy size             : " + duplicate.size() + " bytes");
        System.out.println("  A deep copy: the directory and everything under it, detached");
        System.out.println("  from any parent, with the same total.");
    }
}
