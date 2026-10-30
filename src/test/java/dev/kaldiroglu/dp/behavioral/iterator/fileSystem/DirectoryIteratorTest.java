package dev.kaldiroglu.dp.behavioral.iterator.fileSystem;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The file system example. The output the Part 3 notes quote from fileSystem.Main is asserted here. */
class DirectoryIteratorTest {

    private static List<String> namesIn(Directory directory) {
        List<String> names = new ArrayList<>();
        DirectoryIterator<Storage> iterator = directory.iterator();
        while (iterator.hasNext()) {
            names.add(iterator.next().toString());
        }
        return names;
    }

    @Test
    @DisplayName("fileSystem.Main prints the Dev directory's four elements, then the two files in Reports")
    void theClientPrintsWhatTheNotesQuote() {
        List<String> lines = Printed.by(
                () -> Main.main(new String[0]));

        List<String> nonBlank = lines.stream().filter(line -> !line.isBlank()).toList();
        assertEquals(List.of(
                "List of the directory: /Users/akin",
                "Iterating",
                "Readme.txt", "Report.docs", "Selam.java", "Reports",
                "Iterating",
                "ImportantReport.docs", "SelamTest.java"), nonBlank);
    }

    @Test
    @DisplayName("move takes the element out of its directory, sets the parent and adds it to the target")
    void moveChangesTheParent() {
        Directory dev = new Directory("Dev");
        Directory reports = new Directory("Reports");
        File file = new File("Readme.txt", dev);
        Directory archive = new Directory("Archive", dev);

        file.move(reports);
        archive.move(reports);

        assertEquals(List.of(), namesIn(dev));
        assertEquals(List.of("Readme.txt", "Archive"), namesIn(reports));
        assertSame(reports, file.getParent());
        assertSame(reports, archive.getParent());
    }

    @Test
    @DisplayName("copying a file gives a new file in no directory")
    void copyAFile() {
        Directory dev = new Directory("Dev");
        File file = new File("Readme.txt", dev);

        File copy = (File) file.copy();

        assertNotSame(file, copy);
        assertEquals("Readme.txt", copy.getName());
        assertNull(copy.getParent());
        assertEquals(List.of("Readme.txt"), namesIn(dev));
    }

    @Test
    @DisplayName("copying a directory copies its elements, so a change to the copy does not reach the original")
    void copyADirectory() {
        Directory dev = new Directory("Dev");
        new File("Readme.txt", dev);
        Directory reports = new Directory("Reports", dev);
        new File("ImportantReport.docs", reports);

        Directory copy = (Directory) dev.copy();
        new File("Extra.txt", copy);

        assertEquals(List.of("Readme.txt", "Reports", "Extra.txt"), namesIn(copy));
        assertEquals(List.of("Readme.txt", "Reports"), namesIn(dev));
        assertSame(copy, ((File) copy.elements().getFirst()).getParent());
    }

    @Test
    @DisplayName("deleting a directory with no parent changes nothing and does not fail")
    void deleteARoot() {
        new Directory("Root").delete();
    }

    @Test
    @DisplayName("the iterator lists only the directory's own elements: a folder inside it is one element")
    void aFolderIsOneElement() {
        Directory dev = new Directory("Dev");
        new File("Readme.txt", dev);
        Directory reports = new Directory("Reports", dev);
        new File("ImportantReport.docs", reports);
        new File("SelamTest.java", reports);

        assertEquals(List.of("Readme.txt", "Reports"), namesIn(dev));
        assertEquals(List.of("ImportantReport.docs", "SelamTest.java"), namesIn(reports));
    }

    @Test
    @DisplayName("only the iterator can reach the list: elements() is package-private and cannot be changed")
    void theListIsHiddenAndUnchangeable() throws Exception {
        Method elements = Directory.class.getDeclaredMethod("elements");
        int modifiers = elements.getModifiers();
        assertFalse(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isProtected(modifiers));
        assertFalse(Modifier.isPrivate(modifiers));

        Directory dev = new Directory("Dev");
        new File("Readme.txt", dev);
        assertThrows(UnsupportedOperationException.class,
                () -> dev.elements().add(new File("Extra.txt", null)));
    }

    @Test
    @DisplayName("DirectoryIterator is in the same package as Directory")
    void theIteratorSitsBesideTheDirectory() {
        assertEquals(Directory.class.getPackage(), DirectoryIterator.class.getPackage());
    }
}
