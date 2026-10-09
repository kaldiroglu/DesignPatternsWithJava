package dev.kaldiroglu.dp.behavioral.state.hw.document;

/**
 * Takes a document through review with a central transition table: an action the table
 * does not allow is refused, then the document is rejected once, approved and archived.
 */
public final class Main {

    public static void main(String[] args) {
        Document document = new Document(new Workflow());
        try {
            document.apply(Action.APPROVE);
        } catch (IllegalStateException refused) {
            System.out.println("APPROVE -> refused: " + refused.getMessage());
        }
        for (Action action : new Action[] {
                Action.SUBMIT, Action.REJECT, Action.SUBMIT, Action.APPROVE, Action.ARCHIVE}) {
            document.apply(action);
            System.out.println(action + " -> " + document.status());
        }
    }
}
