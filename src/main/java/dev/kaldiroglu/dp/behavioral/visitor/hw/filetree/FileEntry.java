package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

public record FileEntry(String name, long size) implements Entry {

    @Override
    public void accept(EntryVisitor visitor) {
        visitor.visitFile(this);
    }
}
