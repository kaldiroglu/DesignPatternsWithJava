package dev.kaldiroglu.dp.structural.proxy.gof;

/**
 * Bounding size (width x height) of a graphic. The document editor uses the
 * extent to lay out the page, which is why the proxy caches it.
 */
public record Extent(int width, int height) {

    public static final Extent ZERO = new Extent(0, 0);
}
