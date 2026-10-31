package dev.kaldiroglu.dp.structural.composite.fileSystem;

/** Moving a file: it leaves one directory and arrives in the other in one call. */
public class MoveMain {

    public static void main(String[] args) {
        SampleTree tree = new SampleTree();
        tree.addNotes();

        System.out.println("-- move Report.docx into Reports --");
        tree.report.move(tree.reports);
        tree.home.list();
        System.out.println("Report.docx now lives at: " + tree.report.path());
        System.out.println("  It left Dev and arrived in Reports — both halves, in one call.");
    }
}
