package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.List;

/**
 * Naive design 3: put the vendor's details in a base class and inherit them.
 * <p>
 * This is a real improvement on the class-per-pair design, and it should be said out loud:
 * the Evernote calls are now written once instead of once per department, and a department
 * that extends this gets them for free.
 * <p>
 * What it costs is on {@link EvernoteBoundFinanceManager}.
 */
public abstract class EvernoteStore {

    private final VendorStores stores;
    private final String notebook;

    protected EvernoteStore(VendorStores stores, String notebook) {
        this.stores = stores;
        this.notebook = notebook;
    }

    protected String put(String path, String content) {
        return stores.evernoteCreateNote(notebook, path, content);
    }

    protected void keepOnly(String path, int versions) {
        List<String> kept = stores.evernoteNoteVersions(notebook, path);
        for (int i = 0; i < kept.size() - versions; i++) {
            stores.evernoteExpunge(notebook, path, kept.get(i));
        }
    }
}
