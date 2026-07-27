package dev.kaldiroglu.dp.structural.proxy.gof;

/**
 * Runs the GoF page-207 scenario end to end so the laziness of the virtual
 * proxy is visible on the console.
 */
public final class Demo {


    public static void main(String[] args) {
        System.out.println("== Building the document (insert proxies — no image is loaded) ==");
        TextDocument doc = new TextDocument();
        doc.insert(new ImageProxy("diagram.png", new Extent(800, 600)));
        doc.insert(new ImageProxy("photo.png", new Extent(1024, 768)));

        System.out.println("\n== Laying out the page (uses getExtent() — still no image loaded) ==");
        System.out.println("Total page extent: " + doc.layoutExtent());

        System.out.println("\n== Drawing the page (first draw forces the real images to load) ==");
        doc.draw(new Point(0, 0));

        System.out.println("\n== Drawing again (images already loaded — no reload) ==");
        doc.draw(new Point(0, 0));
    }
}
