package dev.kaldiroglu.dp.structural.bridge.retrofit;

import java.util.List;

/**
 * The one bought next year, which is why this is a Bridge and not an Adapter.
 * <p>
 * A different vendor with different habits: it names sessions differently and returns rows
 * in its own shape. No reporting class knows it exists.
 */
public class PurchasedEngine implements VendorClient {

    @Override
    public String name() {
        return "purchased";
    }

    @Override
    public String open(String database) {
        return "conn[" + database + "]";
    }

    @Override
    public List<String> pull(String handle, String statement) {
        return List.of(handle + " >> " + statement.toLowerCase());
    }

    @Override
    public void release(String handle) {
        // this vendor pools sessions; releasing is a no-op
    }
}
