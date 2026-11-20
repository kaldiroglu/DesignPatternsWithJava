package dev.kaldiroglu.dp.behavioral.visitor.hw.filetree;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 2: the folder tree walks itself, so the visitors have no loop. */
class FileTreeTest {

    private static Folder project() {
        return new Folder("project",
                new FileEntry("README.md", 2),
                new Folder("src",
                        new FileEntry("Main.java", 4),
                        new FileEntry("Order.java", 6)),
                new FileEntry("build.xml", 3));
    }

    @Test
    @DisplayName("the listing visitor indents by depth, using enter and leave")
    void theListing() {
        ListingVisitor listing = new ListingVisitor();
        project().accept(listing);
        assertEquals(List.of("project/", "  README.md (2)", "  src/", "    Main.java (4)",
                "    Order.java (6)", "  build.xml (3)"), listing.lines());
    }

    @Test
    @DisplayName("the size visitor adds up 2 + 4 + 6 + 3 = 15")
    void theTotalSize() {
        SizeVisitor size = new SizeVisitor();
        project().accept(size);
        assertEquals(15, size.total());
    }

    @Test
    @DisplayName("Folder.accept does the walking: no visitor has a loop over the children")
    void theFolderWalks() {
        String folder = Printed.codeOf("src/main/java/dev/kaldiroglu/dp/behavioral/visitor/hw/filetree/Folder.java");
        assertTrue(folder.contains("for (Entry child : children)"));
        for (String visitor : List.of("ListingVisitor", "SizeVisitor")) {
            String code = Printed.codeOf("src/main/java/dev/kaldiroglu/dp/behavioral/visitor/hw/filetree/" + visitor + ".java");
            assertFalse(code.contains("for ("), visitor);
            assertFalse(code.contains("children"), visitor);
        }
    }

    @Test
    @DisplayName("Main prints the listing, then the total size")
    void mainOutput() {
        assertEquals(List.of("project/", "  README.md (2)", "  src/", "    Main.java (4)",
                "    Order.java (6)", "  build.xml (3)", "Total size: 15"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
