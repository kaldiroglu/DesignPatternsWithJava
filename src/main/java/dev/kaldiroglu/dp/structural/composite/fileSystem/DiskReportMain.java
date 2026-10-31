package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** One client, one question: the total bytes of a whole tree, and of a single file. */
public class DiskReportMain {

    public static void main(String[] args) {
        Directory home = new Directory("akin");
        Directory dev = new Directory("Dev", home);
        new File("Readme.txt", dev, 2_048);
        File report = new File("Report.docx", dev, 45_000);
        Directory reports = new Directory("Reports", dev);
        new File("Important.docx", reports, 120_000);

        System.out.printf("whole tree : %,d bytes%n", new DiskReport(home).totalBytes());
        System.out.printf("one file   : %,d bytes%n", new DiskReport(report).totalBytes());
        System.out.println("  The same client, and the same five questions. It cannot tell the");
        System.out.println("  difference, because it holds a Storage.");
    }
}
