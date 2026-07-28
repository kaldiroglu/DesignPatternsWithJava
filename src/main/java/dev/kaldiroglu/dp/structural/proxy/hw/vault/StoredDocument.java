package dev.kaldiroglu.dp.structural.proxy.hw.vault;

/**
 * The RealSubject: the document itself, expensive to open and unaware of who is holding it.
 */
public class StoredDocument implements Document {

    private final String name;
    private String text;
    private boolean open = true;
    private static int opensPerformed;

    public StoredDocument(String name, String text) {
        this.name = name;
        this.text = text;
        opensPerformed++;
        System.out.println("  [disk] opened " + name);
    }

    @Override
    public String read() {
        requireOpen();
        return text;
    }

    @Override
    public void write(String text) {
        requireOpen();
        this.text = text;
    }

    @Override
    public void close() {
        open = false;
        System.out.println("  [disk] closed " + name);
    }

    public boolean isOpen() {
        return open;
    }

    private void requireOpen() {
        if (!open) {
            throw new IllegalStateException(name + " is closed");
        }
    }

    public static int opensPerformed() {
        return opensPerformed;
    }

    public static void resetOpens() {
        opensPerformed = 0;
    }
}
