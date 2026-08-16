package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.List;

/**
 * Naive design 2: one class per (department, store) pair.
 * <p>
 * Three of the six are written out here. The name has to state both axes, because the class
 * is both — and that is the smell: a class name carrying two ideas is hiding a field.
 * <p>
 * Compare with {@link InsuranceEvernoteManager}: the Evernote calls are identical and the
 * retention number differs. Compare with {@link FinanceSharepointManager}: the retention
 * number is identical and the vendor calls differ. Neither axis can be edited alone.
 */
public final class FinanceEvernoteManager {

    private static final int KEEP = 5;
    private static final String NOTEBOOK = "finance";

    private final VendorStores stores;

    public FinanceEvernoteManager(VendorStores stores) {
        this.stores = stores;
    }

    public String save(String path, String content) {
        String guid = stores.evernoteCreateNote(NOTEBOOK, path, content);
        List<String> kept = stores.evernoteNoteVersions(NOTEBOOK, path);
        for (int i = 0; i < kept.size() - KEEP; i++) {
            stores.evernoteExpunge(NOTEBOOK, path, kept.get(i));
        }
        return guid;
    }
}
