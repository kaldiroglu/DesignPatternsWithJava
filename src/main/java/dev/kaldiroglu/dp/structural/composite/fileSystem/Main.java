package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

import java.time.Instant;

/**
 * A small tree, and the six roll-ups that make it a Composite: a sum, a count, a maximum,
 * a reduction to an element, a search, and a rendering — every one of them one call at any
 * depth, with no loop at the call site.
 */
public class Main {

    public static void main(String[] args) {
        Directory home = new Directory("akin");
        Directory dev = new Directory("Dev", home);
        File readme = new File("Readme.txt", dev, 2_048);
        File report = new File("Report.docx", dev, 45_000);
        File java = new File("Selam.java", dev, 3_100);

        Directory reports = new Directory("Reports", dev);
        File important = new File("ImportantReport.docx", reports, 120_000);
        new Alias("Latest report", reports, report);

        readme.touch(Instant.parse("2026-01-04T09:00:00Z"));
        report.touch(Instant.parse("2026-03-19T14:30:00Z"));
        java.touch(Instant.parse("2026-02-11T08:15:00Z"));
        important.touch(Instant.parse("2026-07-21T17:45:00Z"));

        home.list();

        System.out.println();
        System.out.println("-- six questions, one call each --");
        System.out.println("size of the whole tree : " + home.size() + " bytes");
        System.out.println("size of Reports only   : " + reports.size() + " bytes");
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

        System.out.println("\n-- the same questions asked of a single file --");
        DiskReport leafReport = new DiskReport(readme);
        System.out.print(leafReport.summary());
        System.out.println("  A leaf answers all five. The client cannot tell the difference.");

        System.out.println("\n-- the cache, and why the parent reference exists --");
        Directory.resetRecomputations();
        home.size();
        home.size();
        home.size();
        System.out.println("three calls, nothing changed : " + Directory.recomputations()
                + " totals computed — the tree is not walked at all");

        new File("Notes.md", reports, 900);      // three levels down
        home.size();
        System.out.println("after one file is added      : " + Directory.recomputations()
                + " — exactly Reports, Dev and akin");
        System.out.println("  Invalidation runs upward, which is why an element keeps a");
        System.out.println("  reference to its parent. Nothing below the change is touched.");

        System.out.println("\n-- move Report.docx into Reports --");
        report.move(reports);
        home.list();
        System.out.println("Report.docx now lives at: " + report.path());
        System.out.println("  It left Dev and arrived in Reports — both halves, in one call.");

        System.out.println("\n-- copy the Reports directory --");
        Storage duplicate = reports.copy();
        duplicate.rename("Reports (copy)");
        System.out.println("copy size             : " + duplicate.size() + " bytes");
        System.out.println("  A deep copy: the directory and everything under it, detached");
        System.out.println("  from any parent, with the same total.");

        System.out.println("\n-- walk the tree depth-first --");
        StorageIterator walker = home.iterator();
        while (walker.hasNext()) {
            System.out.println("  " + walker.next().getName());
        }
    }
}
