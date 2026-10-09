package dev.kaldiroglu.dp.behavioral.iterator.fileSystem;

import dev.kaldiroglu.dp.behavioral.command.lender.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The file system example. The output the Part 3 notes quote from fileSystem.Test is asserted here. */
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
    @DisplayName("fileSystem.Test prints the Dev directory's four elements, then the two files in Reports")
    void theClientPrintsWhatTheNotesQuote() {
        List<String> lines = Printed.by(
                () -> dev.kaldiroglu.dp.behavioral.iterator.fileSystem.Test.main(new String[0]));

        List<String> nonBlank = lines.stream().filter(line -> !line.isBlank()).toList();
        assertEquals(List.of(
                "List of the directory: /Users/akin",
                "Iterating",
                "Readme.txt", "Report.docs", "Selam.java", "Reports",
                "Iterating",
                "ImportantReport.docs", "SelamTest.java"), nonBlank);
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
