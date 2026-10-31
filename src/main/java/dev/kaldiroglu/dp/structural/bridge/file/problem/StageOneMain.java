package dev.kaldiroglu.dp.structural.bridge.file.problem;

import static dev.kaldiroglu.dp.structural.bridge.file.problem.DraftReport.PATH;

/** Stage one: eight drafts of one document through the switch. FileNet keeps all eight. */
public class StageOneMain {

    public static void main(String[] args) {
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
        DraftReport.print(stores, "Evernote", "finance/" + PATH, Department.FINANCE);
        DraftReport.print(stores, "Evernote", "insurance/" + PATH, Department.INSURANCE);
        DraftReport.print(stores, "FileNet", "insurance!" + PATH, Department.INSURANCE);
        System.out.println("""
                  The last line is the whole problem. Insurance may keep two versions and
                  is holding eight, because the FileNet branch never applied the rule.
                  Nothing threw. The vendor is happy. Only an auditor would find it.
                """);
    }
}
