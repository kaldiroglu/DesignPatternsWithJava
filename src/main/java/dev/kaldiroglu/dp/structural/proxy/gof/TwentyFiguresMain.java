package dev.kaldiroglu.dp.structural.proxy.gof;

/** A document of twenty figures is laid out, and not one image is read from disk. */
public final class TwentyFiguresMain {

    private static final Extent A4 = new Extent(210, 297);

    private TwentyFiguresMain() {
    }

    public static void main(String[] args) {
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

        int loaded = 0;
        for (ImageProxy image : images) {
            if (image.isImageLoaded()) {
                loaded++;
            }
        }

        System.out.println("figures in the document : " + images.length);
        System.out.println("total page height       : " + totalHeight);
        System.out.println("images read from disk   : " + loaded);
    }
}
