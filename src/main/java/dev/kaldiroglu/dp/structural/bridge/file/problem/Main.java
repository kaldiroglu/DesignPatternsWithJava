package dev.kaldiroglu.dp.structural.bridge.file.problem;

/**
 * Runs the three naive designs, so the bill can be seen rather than described.
 * <p>
 * Eight drafts of the same document are saved through each design. What survives is the
 * interesting part.
 */
public class Main {

    private static final String PATH = "claims/2026/case-4021";

    public static void main(String[] args) {
        switchOnEachAxis();
        classPerPair();
        inheritTheStore();
    }

    private static void switchOnEachAxis() {
        System.out.println("Design 1 — one class, a switch on each axis");
        VendorStores stores = new VendorStores();
        SwitchingFileManager manager = new SwitchingFileManager(stores);

        for (int draft = 1; draft <= 8; draft++) {
            for (Department department : Department.values()) {
                for (Store store : Store.values()) {
                    manager.save(department, store, PATH, "draft " + draft);
                }
            }
        }

        System.out.println("  after eight drafts of the same document:");
        report(stores, "Evernote", "finance/" + PATH, Department.FINANCE);
        report(stores, "Evernote", "insurance/" + PATH, Department.INSURANCE);
        report(stores, "FileNet", "insurance!" + PATH, Department.INSURANCE);
        System.out.println("""
                  The last line is the whole problem. Insurance may keep two versions and
                  is holding eight, because the FileNet branch never applied the rule.
                  Nothing threw. The vendor is happy. Only an auditor would find it.
                """);
    }

    private static void classPerPair() {
        System.out.println("Design 2 — one class per (department, store) pair");
        VendorStores stores = new VendorStores();
        FinanceEvernoteManager finance = new FinanceEvernoteManager(stores);
        InsuranceEvernoteManager insurance = new InsuranceEvernoteManager(stores);

        for (int draft = 1; draft <= 8; draft++) {
            finance.save(PATH, "draft " + draft);
            insurance.save(PATH, "draft " + draft);
        }

        report(stores, "Evernote", "finance/" + PATH, Department.FINANCE);
        report(stores, "Evernote", "insurance/" + PATH, Department.INSURANCE);
        System.out.println("""
                  Both rules are right this time, and each was written by hand. The two
                  classes differ by one number; the Evernote calls in them are identical.
                  Two departments and three stores is six such classes, and a fourth store
                  is three more.
                """);
    }

    private static void inheritTheStore() {
        System.out.println("Design 3 — the store becomes the superclass");
        VendorStores stores = new VendorStores();
        EvernoteBoundFinanceManager manager = new EvernoteBoundFinanceManager(stores);

        for (int draft = 1; draft <= 8; draft++) {
            manager.save(PATH, "draft " + draft);
        }
        report(stores, "Evernote", "finance/" + PATH, Department.FINANCE);

        System.out.println("""
                  The retention rule is written once now, which is a real improvement.
                  But the store is in the superclass, so it was chosen when this code was
                  compiled. There is no line you can write here that moves this manager to
                  SharePoint — see bridge.file, where the store is a field and
                  setProvider does exactly that.
                """);
    }

    private static void report(VendorStores stores, String vendor, String address,
                               Department department) {
        System.out.printf("    %-10s %-34s kept %d, allowed %d%s%n",
                vendor, address,
                stores.versionsHeld(vendor, address), department.retainedVersions(),
                stores.versionsHeld(vendor, address) > department.retainedVersions()
                        ? "   <-- over the limit" : "");
    }
}
