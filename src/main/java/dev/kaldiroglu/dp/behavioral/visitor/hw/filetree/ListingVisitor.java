package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

import java.util.ArrayList;
import java.util.List;

/** Lists the tree with indentation. It uses enter and leave to know the depth. */
public final class ListingVisitor implements EntryVisitor {

    private final List<String> lines = new ArrayList<>();
    private int depth;

    @Override
    public void visitFile(FileEntry file) {
        lines.add("  ".repeat(depth) + file.name() + " (" + file.size() + ")");
    }

    @Override
    public void enterFolder(Folder folder) {
        lines.add("  ".repeat(depth) + folder.name() + "/");
        depth++;
    }

    @Override
    public void leaveFolder(Folder folder) {
        depth--;
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }
}
