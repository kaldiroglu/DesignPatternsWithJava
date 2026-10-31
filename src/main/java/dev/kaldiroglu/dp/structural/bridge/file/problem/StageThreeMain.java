package dev.kaldiroglu.dp.structural.bridge.file.problem;

import static dev.kaldiroglu.dp.structural.bridge.file.problem.DraftReport.PATH;

/** Stage three: the store becomes the superclass. */
public class StageThreeMain {

    public static void main(String[] args) {
        System.out.println("Design 3 — the store becomes the superclass");
        VendorStores stores = new VendorStores();
        EvernoteBoundFinanceManager manager = new EvernoteBoundFinanceManager(stores);

        for (int draft = 1; draft <= 8; draft++) {
            manager.save(PATH, "draft " + draft);
        }
        DraftReport.print(stores, "Evernote", "finance/" + PATH, Department.FINANCE);

        System.out.println("""
                  The retention rule is written once now, which is a real improvement.
                  But the store is in the superclass, so it was chosen when this code was
                  compiled. There is no line you can write here that moves this manager to
                  SharePoint — see bridge.file, where the store is a field and
                  setProvider does exactly that.
                """);
    }
}
