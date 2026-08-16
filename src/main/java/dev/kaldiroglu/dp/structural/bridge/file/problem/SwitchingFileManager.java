package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.List;

/**
 * Naive design 1: one class, one method, and a switch on each axis.
 * <p>
 * This is what the code looks like after the second store arrives and before anybody has
 * time to think. It works, and for one department and one store it is the clearest thing in
 * the file.
 *
 * <h2>What it costs</h2>
 * <ul>
 *   <li><b>Every branch is a pair.</b> Departments x stores, written out by hand. Two and
 *       three is six branches; a third department makes it nine.</li>
 *   <li><b>Both axes are frozen together.</b> Adding a store means editing every
 *       department's branch; adding a department means editing every store's. No edit
 *       touches one axis alone.</li>
 *   <li><b>The rules leak.</b> Count the calls to a retention helper below. The rule is not
 *       owned by anything; it is repeated wherever somebody remembered it — and in
 *       {@code saveForInsurance}, on FileNet, forgotten.</li>
 * </ul>
 *
 * <h2>The bug this class exists to show</h2>
 * Insurance may keep two versions. On FileNet it keeps all of them, for ever. Nothing
 * throws, nothing is logged, and the vendor is perfectly happy — the breach is only visible
 * to an auditor, or to a test that counts what was kept.
 */
public final class SwitchingFileManager {

    private final VendorStores stores;

    public SwitchingFileManager(VendorStores stores) {
        this.stores = stores;
    }

    public String save(Department department, Store store, String path, String content) {
        return switch (department) {
            case FINANCE -> saveForFinance(store, path, content);
            case INSURANCE -> saveForInsurance(store, path, content);
        };
    }

    private String saveForFinance(Store store, String path, String content) {
        String notebook = "finance";
        return switch (store) {
            case EVERNOTE -> {
                String guid = stores.evernoteCreateNote(notebook, path, content);
                List<String> kept = stores.evernoteNoteVersions(notebook, path);
                for (int i = 0; i < kept.size() - 5; i++) {
                    stores.evernoteExpunge(notebook, path, kept.get(i));
                }
                yield guid;
            }
            case SHAREPOINT -> {
                String url = "/sites/" + notebook + "/" + path;
                int version = stores.sharePointUpload(url, content.getBytes());
                List<Integer> kept = stores.sharePointVersionHistory(url);
                for (int i = 0; i < kept.size() - 5; i++) {
                    stores.sharePointDeleteVersion(url, kept.get(i));
                }
                yield String.valueOf(version);
            }
            case FILENET -> {
                String series = stores.fileNetCheckin(notebook, path, content.getBytes());
                List<String> kept = stores.fileNetVersionSeries(notebook, path);
                for (int i = 0; i < kept.size() - 5; i++) {
                    stores.fileNetDelete(notebook, path, kept.get(i));
                }
                yield series;
            }
        };
    }

    private String saveForInsurance(Store store, String path, String content) {
        String notebook = "insurance";
        return switch (store) {
            case EVERNOTE -> {
                String guid = stores.evernoteCreateNote(notebook, path, content);
                List<String> kept = stores.evernoteNoteVersions(notebook, path);
                for (int i = 0; i < kept.size() - 2; i++) {
                    stores.evernoteExpunge(notebook, path, kept.get(i));
                }
                yield guid;
            }
            case SHAREPOINT -> {
                String url = "/sites/" + notebook + "/" + path;
                int version = stores.sharePointUpload(url, content.getBytes());
                List<Integer> kept = stores.sharePointVersionHistory(url);
                for (int i = 0; i < kept.size() - 2; i++) {
                    stores.sharePointDeleteVersion(url, kept.get(i));
                }
                yield String.valueOf(version);
            }
            case FILENET -> {
                // The two-version rule is missing here. Nobody removed it; the person who
                // added FileNet copied the finance branch, which keeps five, and then
                // deleted the loop rather than change the number. Nothing throws.
                yield stores.fileNetCheckin(notebook, path, content.getBytes());
            }
        };
    }
}
