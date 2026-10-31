package dev.kaldiroglu.dp.structural.bridge.file.problem;

/** What the three stages share: the document they save, and one line about what was kept. */
final class DraftReport {

    static final String PATH = "claims/2026/case-4021";

    private DraftReport() {
    }

    static void print(VendorStores stores, String vendor, String address, Department department) {
        System.out.printf("    %-10s %-34s kept %d, allowed %d%s%n",
                vendor, address,
                stores.versionsHeld(vendor, address), department.retainedVersions(),
                stores.versionsHeld(vendor, address) > department.retainedVersions()
                        ? "   <-- over the limit" : "");
    }
}
