package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

/** Adds up the size of every file. It knows nothing about walking the tree. */
public final class SizeVisitor implements EntryVisitor {

    private long total;

    @Override
    public void visitFile(FileEntry file) {
        total += file.size();
    }

    @Override
    public void enterFolder(Folder folder) {
    }

    @Override
    public void leaveFolder(Folder folder) {
    }

    public long total() {
        return total;
    }
}
