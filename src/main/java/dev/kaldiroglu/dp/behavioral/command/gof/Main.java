package dev.kaldiroglu.dp.behavioral.command.gof;

/**
 * Shows the receivers on their own: an application, a document and a clipboard, with no menu
 * at all.
 */
public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        Document letter = new Document("letter", application.clipboard());
        application.add(letter);
        letter.open();
        System.out.println("Opened '" + letter.name() + "': " + letter.isOpen());

        letter.type("Dear Deniz");
        letter.copy();
        System.out.println("Typed and copied. Clipboard holds: " + application.clipboard().contents());

        letter.paste();
        System.out.println("Pasted. The letter reads: " + letter.text());
        System.out.println("The document knows how to copy and paste, and nothing about menus.");
    }
}
