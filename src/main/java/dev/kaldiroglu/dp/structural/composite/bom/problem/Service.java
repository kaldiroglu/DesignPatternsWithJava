package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * A third kind of item, added to the naive design after the fact: a
 * subcontracted operation such as powder coating.
 *
 * <p>It costs money, but it adds no mass and it is not a part anybody can put on
 * a shelf. It is a perfectly reasonable thing for a bill of materials to
 * contain, and it is exactly the kind of requirement that arrives six months
 * after a design is settled.</p>
 *
 * <p>Watch what it costs the naive design:</p>
 *
 * <ol>
 *   <li>{@link Assembly} cannot hold one. Its two collections are typed
 *       {@code List<Part>} and {@code List<Assembly>}, so a third collection has
 *       to be added — and then <em>every</em> traversal has to visit three lists
 *       instead of two.</li>
 *   <li>{@link NaiveCosting} and {@link NaiveShipping} do not recognize it, so
 *       each of their {@code instanceof} chains needs a new branch. Until they
 *       get one, they throw — see
 *       {@code NaiveBomTest.aNewKindOfItemBreaksEveryExistingClient}.</li>
 *   <li>Any client written by a third party, which the authors of this code
 *       cannot edit, is simply broken.</li>
 * </ol>
 *
 * <p>Contrast {@code solution.Service}: the same concept, fifteen lines, extends
 * {@code BomComponent}, and every existing client handles it correctly on the
 * day it is written. That is GoF's third consequence (p. 166) — "makes it easier
 * to add new kinds of components" — as a difference you can run.</p>
 */
public class Service {

    private final String partNumber;
    private final String name;
    private final Money fee;

    public Service(String partNumber, String name, Money fee) {
        this.partNumber = partNumber;
        this.name = name;
        this.fee = fee;
    }

    /** Creates a service from the shared reference data. */
    public Service(Catalog.ServiceSpec spec) {
        this(spec.partNumber(), spec.name(), spec.fee());
    }

    public String partNumber() {
        return partNumber;
    }

    public String name() {
        return name;
    }

    public Money fee() {
        return fee;
    }
}
