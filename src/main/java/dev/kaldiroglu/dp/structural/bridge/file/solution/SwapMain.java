package dev.kaldiroglu.dp.structural.bridge.file.solution;

/** The same manager object moves to another store while the program runs. */
public class SwapMain {

    public static void main(String[] args) {
        // The move that makes it a Bridge rather than a choice made once.
        FileManager finance = new FinanceFileManager(new EvernoteProvider());
        finance.save("memo", "written on Evernote");
        finance.setProvider(new FileNetProvider());
        finance.save("memo", "written on FileNet");
        System.out.println("The same manager object, moved between stores at run time:");
        System.out.println("  " + finance.read("memo"));
    }
}
