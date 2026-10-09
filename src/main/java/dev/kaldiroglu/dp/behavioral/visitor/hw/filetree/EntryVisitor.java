package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

/**
 * The <b>Visitor</b> for a folder tree. A folder is visited twice — when the walk enters it
 * and when it leaves — so a visitor can keep track of the depth. {@code java.nio.file.FileVisitor}
 * has the same shape: {@code preVisitDirectory} and {@code postVisitDirectory}.
 */
public interface EntryVisitor {

    void visitFile(FileEntry file);

    void enterFolder(Folder folder);

    void leaveFolder(Folder folder);
}
