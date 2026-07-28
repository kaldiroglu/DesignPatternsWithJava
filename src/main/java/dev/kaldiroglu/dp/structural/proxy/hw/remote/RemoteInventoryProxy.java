package dev.kaldiroglu.dp.structural.proxy.hw.remote;

import java.util.Objects;

/**
 * A <em>remote proxy</em> — GoF's first named kind (p. 208): "a local representative for an
 * object in a different address space".
 * <p>
 * It implements {@link InventoryService}, so a caller cannot tell that the warehouse is on
 * another machine. That is the benefit, and it is also the trap this exercise is about.
 * <p>
 * <strong>The call is not the same call.</strong> It takes a hundred times longer, it can
 * fail for reasons that have nothing to do with inventory, and a loop that was harmless when
 * the object was local becomes a hundred round trips when it is not. The interface says none
 * of this. Making a remote object look local is exactly what the pattern is for, and exactly
 * what makes it easy to misuse.
 * <p>
 * The retry here is a decision, not a given: it hides brief outages and doubles the delay of
 * a real one.
 */
public class RemoteInventoryProxy implements InventoryService {

    private final InventoryService warehouse;
    private final Link link;
    private final int attempts;

    public RemoteInventoryProxy(InventoryService warehouse, Link link, int attempts) {
        this.warehouse = Objects.requireNonNull(warehouse);
        this.link = Objects.requireNonNull(link);
        this.attempts = attempts;
    }

    @Override
    public int stockOf(String sku) {
        RemoteCallFailedException last = null;
        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                link.cross();                    // the part the interface does not mention
                return warehouse.stockOf(sku);
            } catch (RemoteCallFailedException e) {
                last = e;
            }
        }
        throw new RemoteCallFailedException(
                "gave up after " + attempts + " attempts: " + last.getMessage());
    }
}
