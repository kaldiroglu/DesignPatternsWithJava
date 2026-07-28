
package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

/**
 * A small tree, and the four things the first version could not do.
 */
public class Main {

    public static void main(String[] args) {
        Directory home = new Directory("akin");
        Directory dev = new Directory("Dev", home);
        new File("Readme.txt", dev, 2_048);
        File report = new File("Report.docx", dev, 45_000);
        new File("Selam.java", dev, 3_100);

        Directory reports = new Directory("Reports", dev);
        new File("ImportantReport.docx", reports, 120_000);
        new Alias("Latest report", reports, report);

        home.list();

        System.out.println();
        System.out.println("size of the whole tree : " + home.size() + " bytes");
        System.out.println("size of Reports only   : " + reports.size() + " bytes");
        System.out.println("elements below home    : " + home.count());
        System.out.println("  One call, any depth, and no loop at the call site.");

        System.out.println("\n-- move Report.docx into Reports --");
        report.move(reports);
        home.list();
        System.out.println("Report.docx now lives at: " + report.path());
        System.out.println("  It left Dev and arrived in Reports. The first version did one");
        System.out.println("  or the other, never both.");

        System.out.println("\n-- copy the Reports directory --");
        Storage duplicate = (Storage) reports.copy();
        duplicate.rename("Reports (copy)");
        System.out.println("copy is a real object : " + (duplicate != null));
        System.out.println("copy size             : " + duplicate.size() + " bytes");
        System.out.println("  copy() used to call clone() on a class that was not Cloneable,");
        System.out.println("  catch the exception, print a line and return null. Every time.");

        System.out.println("\n-- walk the tree depth-first --");
        StorageIterator walker = home.iterator();
        while (walker.hasNext()) {
            System.out.println("  " + walker.next().getName());
        }
    }
}
