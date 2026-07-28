package dev.kaldiroglu.dp.structural.composite.gof.graphics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the graphics example of GoF p. 163.
 *
 * <p>They pin down the three properties that make this Composite and not just a
 * list: leaves and composites share one type, a composite may contain other
 * composites to any depth, and the transparent interface makes a leaf reject
 * child operations at run time.</p>
 */
class GraphicsCompositeTest {

    @Test
    @DisplayName("A leaf and a composite are both usable as a Graphic")
    void leafAndCompositeShareTheComponentType() {
        List<Graphic> mixed = List.of(new Line(10), new Picture("empty"), new Text("hi"));

        // The client declares only Graphic; there is no type test anywhere.
        for (Graphic graphic : mixed) {
            graphic.draw(new Point(0, 0));
        }

        assertEquals(3, mixed.size());
    }

    @Test
    @DisplayName("A Picture can nest other Pictures to arbitrary depth")
    void compositesNest() {
        Line innermost = new Line(5);

        Picture level3 = new Picture("level3");
        level3.add(innermost);
        Picture level2 = new Picture("level2");
        level2.add(level3);
        Picture level1 = new Picture("level1");
        level1.add(level2);

        assertSame(innermost, level1.getChild(0).getChild(0).getChild(0));
    }

    @Test
    @DisplayName("A composite reports its children; a leaf reports none")
    void childrenAreVisibleThroughTheComponentInterface() {
        Picture picture = new Picture("drawing");
        Rectangle rectangle = new Rectangle(3, 4);
        picture.add(rectangle);

        assertEquals(List.of(rectangle), picture.children());
        assertTrue(picture.isComposite());

        Graphic leaf = new Text("leaf");
        assertEquals(List.of(), leaf.children());
        assertFalse(leaf.isComposite());
    }

    @Test
    @DisplayName("remove() detaches a child from its composite")
    void removeDetachesAChild() {
        Picture picture = new Picture("drawing");
        Line line = new Line(1);
        picture.add(line);
        picture.remove(line);

        assertEquals(List.of(), picture.children());
    }

    @Test
    @DisplayName("The price of transparency: a leaf rejects child operations")
    void leavesRejectChildOperations() {
        Graphic leaf = new Line(1);

        assertThrows(UnsupportedOperationException.class, () -> leaf.add(new Line(2)));
        assertThrows(UnsupportedOperationException.class, () -> leaf.remove(new Line(2)));
        assertThrows(UnsupportedOperationException.class, () -> leaf.getChild(0));
    }

    @Test
    @DisplayName("A recursive walk over the tree needs no knowledge of concrete types")
    void aUniformWalkCountsEveryNode() {
        Picture drawing = new Picture("drawing");
        drawing.add(new Line(100));
        drawing.add(new Text("Composite"));
        Picture logo = new Picture("logo");
        logo.add(new Rectangle(40, 20));
        logo.add(new Line(40));
        drawing.add(logo);

        assertEquals(6, countNodes(drawing)); // 2 pictures + 4 primitives
    }

    private static int countNodes(Graphic graphic) {
        int count = 1;
        for (Graphic child : graphic.children()) {
            count += countNodes(child);
        }
        return count;
    }
}
