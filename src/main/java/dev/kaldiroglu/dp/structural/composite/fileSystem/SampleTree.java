package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.time.Instant;

/** The small tree every demo in this package runs on. */
final class SampleTree {

    final Directory home = new Directory("akin");
    final Directory dev = new Directory("Dev", home);
    final File readme = new File("Readme.txt", dev, 2_048);
    final File report = new File("Report.docx", dev, 45_000);
    final File java = new File("Selam.java", dev, 3_100);
    final Directory reports = new Directory("Reports", dev);
    final File important = new File("ImportantReport.docx", reports, 120_000);

    SampleTree() {
        new Alias("Latest report", reports, report);

        readme.touch(Instant.parse("2026-01-04T09:00:00Z"));
        report.touch(Instant.parse("2026-03-19T14:30:00Z"));
        java.touch(Instant.parse("2026-02-11T08:15:00Z"));
        important.touch(Instant.parse("2026-07-21T17:45:00Z"));
    }

    /** Adds Notes.md three levels down, the change {@link CacheMain} measures. */
    void addNotes() {
        new File("Notes.md", reports, 900);
    }

    /** The tree as it stands after the file is added and Report.docx has moved. */
    static SampleTree afterChanges() {
        SampleTree tree = new SampleTree();
        tree.addNotes();
        tree.report.move(tree.reports);
        return tree;
    }
}
