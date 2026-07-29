package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Six roll-ups, asserted as numbers rather than described: a sum, a count, a maximum, a
 * reduction to an element, a search, and a rendering. Each is asked of a root and answered
 * for the whole subtree, and each is asked again of a single leaf to show the client cannot
 * tell the difference.
 * <p>
 * The last group covers GoF implementation issue 8 — caching the total in the composite, and
 * invalidating upward when anything changes.
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
        assertEquals(5, visited.size(), "the iterator yields the descendants");
        assertEquals(6, home.count(), "count() includes the element itself, so a leaf is one");
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


    // ------------------------------------------------------------------ the roll-ups

    @Test
    @DisplayName("count crosses every level, and a leaf is one")
    void countIsARollUp() {
        tree();

        assertEquals(6, home.count(), "akin, Dev, Readme, Report, Reports, Important");
        assertEquals(2, reports.count(), "Reports and the file in it");
        assertEquals(1, report.count(), "a leaf is one element, and answers for itself");
    }

    @Test
    @DisplayName("lastModified is a maximum, not a sum")
    void lastModifiedIsAMaximum() {
        tree();
        Instant older = Instant.parse("2026-01-04T09:00:00Z");
        Instant newest = Instant.parse("2026-07-21T17:45:00Z");
        report.touch(older);
        ((StorageElement) reports.elements().get(0)).touch(newest);

        assertEquals(newest, home.lastModified(), "the newest anywhere beneath the root");
        assertEquals(newest, reports.lastModified());
        assertEquals(older, report.lastModified(), "and a leaf answers for itself");
    }

    @Test
    @DisplayName("largest reduces the subtree to an element, not a number")
    void largestReturnsAnElement() {
        tree();

        Storage biggest = home.largest().orElseThrow();

        assertEquals("Important.docx", biggest.getName());
        assertEquals(120_000, biggest.size());
        assertSame(report, report.largest().orElseThrow(), "a leaf is its own largest");
        assertTrue(new Directory("empty").largest().isEmpty(), "and an empty directory has none");
    }

    @Test
    @DisplayName("find reaches any depth, and the caller writes no recursion")
    void findCrossesLevels() {
        tree();

        assertEquals("Important.docx",
                home.find("Important.docx").map(Storage::getName).orElseThrow(),
                "three levels down from the root");
        assertEquals("Reports", home.find("Reports").map(Storage::getName).orElseThrow(),
                "and a directory is findable too");
        assertTrue(home.find("nothing.txt").isEmpty());
    }

    @Test
    @DisplayName("findAll returns composites and leaves alike, because the test never asks")
    void findAllDoesNotDistinguish() {
        tree();

        List<String> big = home.findAll(element -> element.size() > 40_000)
                .stream().map(Storage::getName).toList();

        assertEquals(List.of("akin", "Dev", "Report.docx", "Reports", "Important.docx"), big,
                "a directory over 40 KB is over 40 KB — the predicate did not ask what it was");
    }

    // ------------------------------------------------------------------ the client

    @Test
    @DisplayName("the client names one type, and works on a leaf as well as a tree")
    void theClientCannotTellTheDifference() {
        tree();

        DiskReport ofTree = new DiskReport(home);
        assertEquals(home.size(), ofTree.totalBytes());
        assertEquals(6, ofTree.elements());
        assertEquals("Important.docx", ofTree.biggest());

        DiskReport ofLeaf = new DiskReport(report);      // one file, same five questions
        assertEquals(45_000, ofLeaf.totalBytes());
        assertEquals(1, ofLeaf.elements());
        assertEquals("Report.docx", ofLeaf.biggest());
    }

    @Test
    @DisplayName("and it names no concrete element type anywhere")
    void theClientNamesOnlyTheComponent() {
        List<Class<?>> concrete = List.of(Directory.class, File.class, Link.class,
                Alias.class, ShortCut.class, StorageElement.class);

        for (Method m : DiskReport.class.getDeclaredMethods()) {
            assertFalse(concrete.contains(m.getReturnType()), m.getName() + " returns a leaf type");
            assertTrue(Arrays.stream(m.getParameterTypes()).noneMatch(concrete::contains),
                    m.getName() + " takes a concrete type");
        }
        assertTrue(Arrays.stream(DiskReport.class.getDeclaredFields())
                        .allMatch(f -> f.getType() == Storage.class),
                "the only type it holds is the Component");
    }

    // ------------------------------------------------- caching, GoF implementation issue 8

    @Test
    @DisplayName("a cached total is not recomputed while nothing changes")
    void theTotalIsCached() {
        tree();
        home.size();                       // warm every level
        Directory.resetRecomputations();

        home.size();
        home.size();
        home.size();

        assertEquals(0, Directory.recomputations(), "three calls, and the tree is not walked");
    }

    @Test
    @DisplayName("a change invalidates exactly its ancestors, and nothing else")
    void invalidationRunsUpward() {
        tree();
        home.size();
        Directory.resetRecomputations();

        new File("Notes.md", reports, 900);   // three levels down
        long after = home.size();

        assertEquals(3, Directory.recomputations(), "Reports, Dev and akin — no more");
        assertEquals(168_716, after, "and the answer is right");
    }

    @Test
    @DisplayName("removing invalidates too, and the total goes back down")
    void removingInvalidates() {
        tree();
        long before = home.size();

        report.delete();

        assertEquals(before - 45_000, home.size(), "the tree noticed");
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
