package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** The client that asks a tree five questions, asking them of a single file. */
public class DiskReportMain {

    public static void main(String[] args) {
        SampleTree tree = new SampleTree();

        System.out.println("-- the same questions asked of a single file --");
        DiskReport leafReport = new DiskReport(tree.readme);
        System.out.print(leafReport.summary());
        System.out.println("  A leaf answers all five. The client cannot tell the difference.");
    }
}
