package dev.kaldiroglu.dp.structural.bridge.file.problem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The three document stores, as their SDKs were handed to us.
 * <p>
 * This is the part nobody gets to redesign, and it is the fixed point every design in this
 * package ends at — the same role {@code notifications.domain.Transports} plays for the
 * notification example.
 * <p>
 * Read the three groups of methods together. No two vendors agree on anything:
 * <ul>
 *   <li><b>What an address is.</b> Evernote wants a notebook and a title, SharePoint a
 *       site-relative URL, FileNet an object store and a document id.</li>
 *   <li><b>What a version is.</b> SharePoint numbers them from 1. Evernote and FileNet
 *       issue opaque identifiers, and the two are not the same shape as each other.</li>
 *   <li><b>What "store this" is called.</b> Create a note, upload a file, check a document
 *       in.</li>
 * </ul>
 * The stores are simulated in memory so the example runs anywhere and so a test can assert
 * what was actually kept.
 */
public final class VendorStores {

    /** version identifiers per "vendor|address", oldest first. */
    private final Map<String, List<String>> versions = new LinkedHashMap<>();

    /** content per "vendor|address|version". */
    private final Map<String, String> contents = new LinkedHashMap<>();

    // ------------------------------------------------------------ Evernote

    /** Evernote's API: a note in a notebook, addressed by title. Returns a note GUID. */
    public String evernoteCreateNote(String notebook, String title, String body) {
        String address = notebook + "/" + title;
        String guid = "note-" + (count("Evernote", address) + 1);
        store("Evernote", address, guid, body);
        return guid;
    }

    public List<String> evernoteNoteVersions(String notebook, String title) {
        return List.copyOf(versionsOf("Evernote", notebook + "/" + title));
    }

    public void evernoteExpunge(String notebook, String title, String guid) {
        remove("Evernote", notebook + "/" + title, guid);
    }

    // ---------------------------------------------------------- SharePoint

    /** SharePoint's API: bytes to a site-relative URL. Versions are numbered from 1. */
    public int sharePointUpload(String siteRelativeUrl, byte[] content) {
        int version = count("SharePoint", siteRelativeUrl) + 1;
        store("SharePoint", siteRelativeUrl, String.valueOf(version), new String(content));
        return version;
    }

    public List<Integer> sharePointVersionHistory(String siteRelativeUrl) {
        return versionsOf("SharePoint", siteRelativeUrl).stream().map(Integer::parseInt).toList();
    }

    public void sharePointDeleteVersion(String siteRelativeUrl, int version) {
        remove("SharePoint", siteRelativeUrl, String.valueOf(version));
    }

    // ------------------------------------------------------------- FileNet

    /** FileNet's API: check a document in to an object store. Returns a version series id. */
    public String fileNetCheckin(String objectStore, String documentId, byte[] content) {
        String address = objectStore + "!" + documentId;
        String seriesId = "vs-" + (count("FileNet", address) + 1);
        store("FileNet", address, seriesId, new String(content));
        return seriesId;
    }

    public List<String> fileNetVersionSeries(String objectStore, String documentId) {
        return List.copyOf(versionsOf("FileNet", objectStore + "!" + documentId));
    }

    public void fileNetDelete(String objectStore, String documentId, String seriesId) {
        remove("FileNet", objectStore + "!" + documentId, seriesId);
    }

    // -------------------------------------------------- what a test can see

    /** How many versions this vendor is currently holding at this address. */
    public int versionsHeld(String vendor, String address) {
        return count(vendor, address);
    }

    /** The most recently stored content at this address, whichever vendor holds it. */
    public String latestContent(String vendor, String address) {
        List<String> held = versionsOf(vendor, address);
        if (held.isEmpty()) {
            throw new IllegalStateException("nothing stored at " + vendor + "|" + address);
        }
        return contents.get(key(vendor, address, held.get(held.size() - 1)));
    }

    // ------------------------------------------------------------- private

    private void store(String vendor, String address, String version, String content) {
        versionsOf(vendor, address).add(version);
        contents.put(key(vendor, address, version), content);
    }

    private void remove(String vendor, String address, String version) {
        versionsOf(vendor, address).remove(version);
        contents.remove(key(vendor, address, version));
    }

    private List<String> versionsOf(String vendor, String address) {
        return versions.computeIfAbsent(vendor + "|" + address, k -> new ArrayList<>());
    }

    private int count(String vendor, String address) {
        return versionsOf(vendor, address).size();
    }

    private static String key(String vendor, String address, String version) {
        return vendor + "|" + address + "|" + version;
    }
}
