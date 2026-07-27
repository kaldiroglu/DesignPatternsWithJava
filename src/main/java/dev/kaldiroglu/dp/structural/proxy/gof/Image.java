package dev.kaldiroglu.dp.structural.proxy.gof;

/**
 * RealSubject role of the Proxy pattern (GoF, p. 207).
 *
 * <p>The actual raster image. Constructing it is <em>expensive</em> because the
 * whole pixel buffer has to be read from disk, so we want to defer creating it
 * until the image really must be drawn. That deferral is exactly what
 * {@link ImageProxy} provides.</p>
 */
public class Image implements Graphic {

    private final String fileName;
    private Extent extent;

    public Image(String fileName) {
        this.fileName = fileName;
        load(); // expensive: reads the whole raster from disk up front
    }

    @Override
    public void load() {
        System.out.println("[Image] Loading raster data from '" + fileName + "' (expensive)...");
        // A real implementation would parse the image header and pixel data here;
        // the extent is discovered while reading the file.
        this.extent = readExtentFromFile();
    }

    private Extent readExtentFromFile() {
        return new Extent(800, 600);
    }

    @Override
    public void draw(Point at) {
        System.out.println("[Image] Drawing '" + fileName + "' " + extent + " at " + at);
    }

    @Override
    public Extent getExtent() {
        return extent;
    }

    @Override
    public void store() {
        System.out.println("[Image] Storing raster data of '" + fileName + "'");
    }

    public String fileName() {
        return fileName;
    }
}
