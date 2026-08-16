package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.List;

/**
 * The same store as {@link FinanceEvernoteManager}, the other department.
 * <p>
 * Every Evernote call below is character-for-character what the finance class does. Only
 * {@code KEEP} differs. Change how this vendor is addressed and both classes have to be
 * edited; there are four more like them.
 */
public final class InsuranceEvernoteManager {

    private static final int KEEP = 2;
    private static final String NOTEBOOK = "insurance";

    private final VendorStores stores;

    public InsuranceEvernoteManager(VendorStores stores) {
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
