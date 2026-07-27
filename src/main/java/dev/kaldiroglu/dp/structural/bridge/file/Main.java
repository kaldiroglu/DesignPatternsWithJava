package dev.kaldiroglu.dp.structural.bridge.file;

import java.util.List;

/**
 * Two departments, three stores, one retention rule each.
 * <p>
 * Six combinations from five classes. The rules never learn which store they are writing to,
 * and the stores have never heard of retention.
 */
public class Main {

    public static void main(String[] args) {
        List<FileProvider> stores = List.of(
                new EvernoteProvider(), new SharepointProvider(), new FileNetProvider());

        for (FileProvider store : stores) {
            FileManager finance = new FinanceFileManager(store);
            FileManager insurance = new InsuranceFileManager(store);

            for (int i = 1; i <= 8; i++) {
                finance.save("q3-report", "finance draft " + i);
                insurance.save("policy-4417", "insurance draft " + i);
            }

            System.out.printf("%-12s finance keeps %d, live versions %s%n",
                    store.name(), finance.retainedVersions(),
                    live(finance.versions("q3-report"), finance.retainedVersions()));
            System.out.printf("%-12s insurance keeps %d, live versions %s%n%n",
                    store.name(), insurance.retainedVersions(),
                    live(insurance.versions("policy-4417"), insurance.retainedVersions()));
        }

        // The move that makes it a Bridge rather than a choice made once.
        FileManager finance = new FinanceFileManager(new EvernoteProvider());
        finance.save("memo", "written on Evernote");
        finance.setProvider(new FileNetProvider());
        finance.save("memo", "written on FileNet");
        System.out.println("The same manager object, moved between stores at run time:");
        System.out.println("  " + finance.read("memo"));

        System.out.println();
        System.out.println("2 departments + 3 stores = 5 classes, 6 combinations.");
        System.out.println("A fourth store is one class, and no retention rule is touched.");
    }

    private static String live(List<Integer> versions, int keep) {
        return versions.subList(Math.max(0, versions.size() - keep), versions.size()).toString();
    }
}
