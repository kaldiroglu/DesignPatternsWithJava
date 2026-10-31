package dev.kaldiroglu.dp.structural.bridge.file.problem;

import static dev.kaldiroglu.dp.structural.bridge.file.problem.DraftReport.PATH;

/** Stage two: one class per (department, store) pair. */
public class StageTwoMain {

    public static void main(String[] args) {
        System.out.println("Design 2 — one class per (department, store) pair");
        VendorStores stores = new VendorStores();
        FinanceEvernoteManager finance = new FinanceEvernoteManager(stores);
        InsuranceEvernoteManager insurance = new InsuranceEvernoteManager(stores);

        for (int draft = 1; draft <= 8; draft++) {
            finance.save(PATH, "draft " + draft);
            insurance.save(PATH, "draft " + draft);
        }

        DraftReport.print(stores, "Evernote", "finance/" + PATH, Department.FINANCE);
        DraftReport.print(stores, "Evernote", "insurance/" + PATH, Department.INSURANCE);
        System.out.println("""
                  Both rules are right this time, and each was written by hand. The two
                  classes differ by one number; the Evernote calls in them are identical.
                  Two departments and three stores is six such classes, and a fourth store
                  is three more.
                """);
    }
}
