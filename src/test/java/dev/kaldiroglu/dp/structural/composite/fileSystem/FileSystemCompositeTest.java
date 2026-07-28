package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Four of these are regression tests with a history: copy() always returned null, move() did
 * half its job and did a different half depending on the receiver's type, delete() threw on a
 * root, and rendering asked what kind of element it was holding.
 */
class FileSystemCompositeTest {

    private Directory home;
    private Directory dev;
    private Directory reports;
    private File report;

    private void tree() {
        home = new Directory("akin");
        dev = new Directory("Dev", home);
        new File("Readme.txt", dev, 2_048);
        report = new File("Report.docx", dev, 45_000);
        reports = new Directory("Reports", dev);
        new File("Important.docx", reports, 120_000);
    }

    @Test
    @DisplayName("size answers for the whole subtree, at any depth")
    void sizeRecurses() {
        tree();
        assertEquals(45_000, report.size());
        assertEquals(120_256, reports.size());                  // 120000 + the directory
        assertEquals(2_048 + 45_000 + 120_256 + 256, dev.size());
        assertEquals(dev.size() + 256, home.size());
    }

    @Test
    @DisplayName("copy returns a real, deep, detached copy")
    void copyIsDeepAndNotNull() {
        // It used to call clone() on a class that never implemented Cloneable, catch the
        // exception, print a line and return null — on every element, every time.
        tree();
        Storage duplicate = reports.copy();

        assertNotNull(duplicate);
        assertNotSame(reports, duplicate);
        assertEquals(reports.size(), duplicate.size());
        assertNull(((StorageElement) duplicate).getParent(), "a copy starts detached");

        // Deep: editing the copy must not touch the original.
        ((Directory) duplicate).add(new File("extra.txt", null, 10));
        assertNotEquals(reports.size(), duplicate.size());
    }

    private static void assertNotEquals(long a, long b) {
        org.junit.jupiter.api.Assertions.assertNotEquals(a, b);
    }

    @Test
    @DisplayName("move leaves the old parent and joins the new one")
    void moveDoesBothHalves() {
        tree();
        assertTrue(dev.elements().contains(report));

        report.move(reports);

        assertTrue(reports.elements().contains(report), "arrived");
        assertTrue(!dev.elements().contains(report), "and left");
        assertEquals(reports, report.getParent(), "and knows where it lives");
        assertEquals("akin/Dev/Reports/Report.docx", report.path());
    }

    @Test
    @DisplayName("moving a directory moves the subtree with it, and only once")
    void movingADirectory() {
        tree();
        long reportsSize = reports.size();
        reports.move(home);

        assertTrue(home.elements().contains(reports));
        assertTrue(!dev.elements().contains(reports));
        assertEquals(reportsSize, reports.size());          // its contents came along
        assertEquals(1, home.elements().stream().filter(e -> e == reports).count());
    }

    @Test
    @DisplayName("deleting a root is harmless — it used to throw")
    void deleteIsNullSafe() {
        tree();
        home.delete();                       // no parent; must not explode
        assertNull(home.getParent());

        report.delete();
        assertTrue(!dev.elements().contains(report));
    }

    @Test
    @DisplayName("rendering asks nothing about types")
    void renderingIsPolymorphic() {
        tree();
        String rendered = home.render("");

        assertTrue(rendered.contains("akin/"));
        assertTrue(rendered.contains("Important.docx"), "three levels down");
        // The old version branched on isDirectory(); the flag and the method are both gone.
        assertTrue(java.util.Arrays.stream(StorageElement.class.getMethods())
                .noneMatch(m -> m.getName().equals("isDirectory")));
    }

    @Test
    @DisplayName("the iterator walks the whole tree, not just the first level")
    void iteratorRecurses() {
        tree();
        List<String> visited = new ArrayList<>();
        StorageIterator walker = home.iterator();
        while (walker.hasNext()) {
            visited.add(walker.next().getName());
        }

        assertEquals(List.of("Dev", "Readme.txt", "Report.docx", "Reports", "Important.docx"),
                visited);
        assertEquals(5, home.count());
    }

    @Test
    @DisplayName("a link is its own small size, so nothing is counted twice")
    void linksDoNotDoubleCount() {
        tree();
        long before = home.size();
        new Alias("latest", reports, report);

        assertEquals(before + 64, home.size());
        assertTrue(home.size() < before + report.size());
    }

    @Test
    @DisplayName("a directory cannot contain itself")
    void noCycles() {
        tree();
        assertThrows(IllegalArgumentException.class, () -> dev.add(dev));
        assertThrows(IllegalArgumentException.class, () -> dev.move(dev));
    }

    @Test
    @DisplayName("child management is off the Component — the safe variant, as in graphic")
    void theSafeVariant() {
        assertTrue(java.util.Arrays.stream(Storage.class.getDeclaredMethods())
                .noneMatch(m -> m.getName().equals("add")));
        assertTrue(StorageContainer.class.isAssignableFrom(Directory.class));
        assertTrue(!StorageContainer.class.isAssignableFrom(File.class));
    }
}
