package dev.kaldiroglu.dp.structural.proxy.hw.vault;

/**
 * Homework 3 — the smart reference.
 */
public class Main {

    public static void main(String[] args) {
        StoredDocument.resetOpens();
        DocumentHandle contract = new DocumentHandle("contract.txt", "the original terms");

        System.out.println("handle created. opened on disk yet? " + contract.isOpen());

        DocumentHandle ayse = contract.acquire();
        DocumentHandle bora = contract.acquire();
        System.out.println("two holders. opened on disk yet? " + contract.isOpen());

        System.out.println("Ayse reads: " + ayse.read());
        System.out.println("opened on disk now?  " + contract.isOpen()
                + "  (opens performed: " + StoredDocument.opensPerformed() + ")");

        System.out.println("\n-- locking --");
        contract.lockForWriting("Ayse");
        contract.writeAs("Ayse", "the amended terms");
        try {
            contract.write("Bora's version");
        } catch (IllegalStateException e) {
            System.out.println("  refused: " + e.getMessage());
        }
        contract.unlock("Ayse");
        System.out.println("  now reads: " + bora.read());

        System.out.println("\n-- letting go --");
        ayse.close();
        System.out.println("Ayse finished. still open? " + contract.isOpen()
                + "  (holders: " + contract.holders() + ")");
        bora.close();
        System.out.println("Bora finished. still open? " + contract.isOpen());

        System.out.println("""

                close() on the proxy means "I am finished with it", not "close it".
                Two holders, two closes, one actual close — and the document was
                never opened at all until somebody read from it.""");
    }
}
