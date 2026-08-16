package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.List;

/**
 * The same department as {@link FinanceEvernoteManager}, the other store.
 * <p>
 * The retention rule below is the same rule, written a second time against a different
 * vendor's API. When finance moves from five years to ten, this class and
 * {@code FinanceFileNetManager} have to be found and changed too — and the day one of them
 * is missed, nothing fails.
 */
public final class FinanceSharepointManager {

    private static final int KEEP = 5;
    private static final String SITE = "/sites/finance/";

    private final VendorStores stores;

    public FinanceSharepointManager(VendorStores stores) {
        this.stores = stores;
    }

    public String save(String path, String content) {
        String url = SITE + path;
        int version = stores.sharePointUpload(url, content.getBytes());
        List<Integer> kept = stores.sharePointVersionHistory(url);
        for (int i = 0; i < kept.size() - KEEP; i++) {
            stores.sharePointDeleteVersion(url, kept.get(i));
        }
        return String.valueOf(version);
    }
}
