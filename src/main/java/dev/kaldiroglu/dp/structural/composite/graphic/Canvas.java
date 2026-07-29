package dev.kaldiroglu.dp.structural.composite.graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Composite: a canvas is drawable, and it holds drawables.
 * <p>
 * Being <em>both</em> is what makes the tree work — a canvas can be added to another canvas,
 * because the thing being added only has to be a {@link Graphic}. Every operation here does
 * its own bit and then forwards to the children, which is the pattern in four methods.
 */
public class Canvas extends GraphicObject implements CompositeGraphic {

    private final List<Graphic> elements = new ArrayList<>();

    public Canvas(String name, String color) {
        super(name, color);
    }

    @Override
    public void draw() {
        System.out.println("Drawing canvas: " + name);
        elements.forEach(Graphic::draw);
    }

    @Override
    public void erase() {
        System.out.println("Erasing canvas: " + name);
        elements.forEach(Graphic::erase);
    }

    @Override
    public void paint() {
        System.out.println("Painting canvas: " + name + ", color " + color);
        elements.forEach(Graphic::paint);
    }

    /**
     * The payoff: one number for a whole tree, and no caller writes a loop.
     * <p>
     * A leaf answers 1, a canvas adds up its children, and neither the client nor this class
     * has to know how deep the tree goes.
     */
    @Override
    public int shapeCount() {
        return elements.stream().mapToInt(Graphic::shapeCount).sum();
    }

    @Override
    public void addGraphic(Graphic graphic) {
        elements.add(graphic);
    }

    @Override
    public void removeGraphic(Graphic graphic) {
        elements.remove(graphic);
    }

    @Override
    public Collection<Graphic> getGraphics() {
        return List.copyOf(elements);
    }

    @Override
    public void listGraphic() {
        listGraphic("");
    }

    /**
     * Recurses, with indentation: a nested canvas prints its own contents rather than one
     * line of its own. Listing a tree is the same shape as measuring one — do this node's
     * part, then ask the children to do theirs.
*/
    private void listGraphic(String indent) {
        System.out.println(indent + this);
        for (Graphic element : elements) {
            if (element instanceof Canvas nested) {
                nested.listGraphic(indent + "    ");
            } else {
                System.out.println(indent + "    " + element);
            }
        }
    }
}
