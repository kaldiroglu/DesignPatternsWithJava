package dev.kaldiroglu.dp.structural.composite.gof.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite role of the Composite solution (GoF, p. 164).
 *
 * <p>A {@code Picture} is a {@link Graphic} that is made of other
 * {@code Graphic}s. Its children may be primitives or other pictures, to any
 * depth — that recursion is the whole point of the solution.</p>
 *
 * <p>Notice how small {@link #draw(Point)} is: a composite implements the
 * Component operations by <em>forwarding them to its children</em>. There is no
 * conditional logic anywhere asking "is this a line or a picture?"; the type
 * system and the recursion do that work.</p>
 */
public class Picture extends Graphic {

    private final String name;
    private final List<Graphic> children = new ArrayList<>();

    public Picture(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public void draw(Point at) {
        System.out.println("Picture \"" + name + "\" drawing " + children.size()
                + " child graphic(s) at " + at + ":");
        for (Graphic child : children) {
            child.draw(at); // uniform: the child may be a leaf or another Picture
        }
    }

    @Override
    public void add(Graphic child) {
        children.add(child);
    }

    @Override
    public void remove(Graphic child) {
        children.remove(child);
    }

    @Override
    public Graphic getChild(int index) {
        return children.get(index);
    }

    @Override
    public List<Graphic> children() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public boolean isComposite() {
        return true;
    }
}
