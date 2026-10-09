package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

import java.util.List;

/** A folder: the composite. Its {@code accept} does the walking. */
public record Folder(String name, List<Entry> children) implements Entry {

    public Folder(String name, Entry... children) {
        this(name, List.of(children));
    }

    @Override
    public void accept(EntryVisitor visitor) {
        visitor.enterFolder(this);
        for (Entry child : children) {
            child.accept(visitor);
        }
        visitor.leaveFolder(this);
    }
}
