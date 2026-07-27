package dev.kaldiroglu.dp.structural.proxy.gof;

/**
 * Proxy role of the Proxy pattern (GoF, p. 207) — a <em>virtual proxy</em>.
 *
 * <p>A lightweight stand-in for an {@link Image}. It keeps the file name and a
 * cached {@link Extent} so the document editor can lay out the page without
 * paying the cost of loading the real image. The real {@link Image} is created
 * only the first time the proxy is asked to {@link #draw(Point)}; afterwards
 * every call is forwarded to it.</p>
 */
public class ImageProxy implements Graphic {

    private final String fileName;
    private Extent extent;   // cached, so getExtent() needs no real Image
    private Image image;     // the RealSubject, created lazily
    private int loadCount;   // how many times the real Image has been created

    public ImageProxy(String fileName, Extent extent) {
        this.fileName = fileName;
        this.extent = extent;
    }

    /** Lazily creates and returns the real image, loading it at most once. */
    private Image image() {
        if (image == null) {
            image = new Image(fileName);
            extent = image.getExtent(); // keep the cached extent in sync with reality
            loadCount++;
        }
        return image;
    }

    @Override
    public void draw(Point at) {
        image().draw(at); // forces the real image to load, then delegates
    }

    @Override
    public Extent getExtent() {
        // Answered from the cached extent — the real image is NOT loaded.
        return extent;
    }

    @Override
    public void store() {
        // Only the file name and extent need to be persisted for the proxy.
        System.out.println("[ImageProxy] Storing reference to '" + fileName + "' " + extent);
    }

    @Override
    public void load() {
        System.out.println("[ImageProxy] Restoring reference to '" + fileName + "' (image not loaded yet)");
    }

    // --- package-private hooks so tests can observe the laziness ---

    boolean isImageLoaded() {
        return image != null;
    }

    int loadCount() {
        return loadCount;
    }
}
