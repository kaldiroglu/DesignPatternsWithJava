package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.time.Instant;
import java.util.List;

/**
 * A <b>Client</b>, and the argument for the pattern in one class.
 *
 * <p>It answers five questions about a tree of unknown shape and depth, and it names exactly
 * one type: {@link Storage}. There is no {@code instanceof}, no {@code isDirectory()}, and
 * no loop that walks anything — every method here is one call and one line.</p>
 *
 * <p>Hand it a single file and every answer is still correct. That is the whole claim, and
 * {@code FileSystemCompositeTest} asserts it by reflecting over this class and failing if a
 * concrete element type appears anywhere in its signatures.</p>
 */
public class DiskReport {

    private final Storage root;

    public DiskReport(Storage root) {
        this.root = root;
    }

    public long totalBytes() {
        return root.size();
    }

    public int elements() {
        return root.count();
    }

    public Instant newest() {
        return root.lastModified();
    }

    public String biggest() {
        return root.largest().map(Storage::getName).orElse("nothing");
    }

    /** Everything over a threshold, wherever it lives in the tree. */
    public List<Storage> over(long bytes) {
        return root.findAll(element -> element.size() > bytes);
    }

    public String summary() {
        return """
                %s
                  %,d bytes in %d elements
                  newest    : %s
                  biggest   : %s
                """.formatted(root.getName(), totalBytes(), elements(), newest(), biggest());
    }
}
