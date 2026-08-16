package dev.kaldiroglu.dp.structural.bridge.file.problem;

/**
 * The department, welded to a vendor by {@code extends}.
 * <p>
 * It <b>is an</b> {@link EvernoteStore}. Not "has a store" — is one. That single word decides
 * everything that follows:
 * <ul>
 *   <li>The store is fixed <b>when the class is compiled</b>. When the Evernote contract ends
 *       and the documents move to SharePoint, this object cannot follow them. A new class has
 *       to be written and every caller that named this one has to be found.</li>
 *   <li>The retention rule is <b>trapped inside an Evernote class</b>. Finance on SharePoint
 *       needs the same rule and cannot reach it.</li>
 *   <li>A second store is not one new class. It is one <b>per department</b>.</li>
 * </ul>
 * There is no {@code setStore} here and there cannot be. A superclass is chosen once, by the
 * compiler, and never again — which is the requirement the whole example turns on.
 */
public final class EvernoteBoundFinanceManager extends EvernoteStore {

    private static final int KEEP = 5;

    public EvernoteBoundFinanceManager(VendorStores stores) {
        super(stores, "finance");
    }

    public String save(String path, String content) {
        String guid = put(path, content);
        keepOnly(path, KEEP);
        return guid;
    }
}
