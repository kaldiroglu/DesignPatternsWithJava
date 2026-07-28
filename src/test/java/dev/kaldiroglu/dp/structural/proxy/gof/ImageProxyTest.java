package dev.kaldiroglu.dp.structural.proxy.gof;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * GoF's own example is a <em>virtual</em> proxy: the saving is that the expensive object is
 * never built unless it is actually needed. That is measurable, so it is measured.
 */
class ImageProxyTest {

    private static final Extent A4 = new Extent(210, 297);

    @Test
    @DisplayName("laying out the page does not load the image")
    void extentIsAnsweredWithoutLoading() {
        ImageProxy proxy = new ImageProxy("map.png", A4);

        assertEquals(A4, proxy.getExtent());
        assertFalse(proxy.isImageLoaded(), "the whole point: nothing was loaded");
        assertEquals(0, proxy.loadCount());
    }

    @Test
    @DisplayName("drawing loads it, once")
    void drawingForcesTheLoad() {
        ImageProxy proxy = new ImageProxy("map.png", A4);

        proxy.draw(new Point(0, 0));
        assertTrue(proxy.isImageLoaded());
        assertEquals(1, proxy.loadCount());

        proxy.draw(new Point(10, 10));
        assertEquals(1, proxy.loadCount(), "and only once");
    }

    @Test
    @DisplayName("the proxy is substitutable for the real image")
    void substitutable() {
        assertTrue(Graphic.class.isAssignableFrom(ImageProxy.class));
        assertTrue(Graphic.class.isAssignableFrom(Image.class));
    }

    @Test
    @DisplayName("a document of unopened images costs nothing to lay out")
    void aDocumentOfProxies() {
        TextDocument document = new TextDocument();
        ImageProxy[] images = new ImageProxy[20];
        for (int i = 0; i < images.length; i++) {
            images[i] = new ImageProxy("figure-" + i + ".png", A4);
            document.insert(images[i]);
        }

        int totalHeight = 0;
        for (ImageProxy image : images) {
            totalHeight += image.getExtent().height();
        }

        assertEquals(20 * 297, totalHeight);
        for (ImageProxy image : images) {
            assertFalse(image.isImageLoaded(), "twenty images, none of them read from disk");
        }
    }
}
