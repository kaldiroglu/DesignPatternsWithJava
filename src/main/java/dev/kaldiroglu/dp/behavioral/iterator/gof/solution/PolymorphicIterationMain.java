package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * One client, PrintEmployees, walks a list backwards and walks a chain list. It knows only
 * the Iterator interface.
 */
public final class PolymorphicIterationMain {

    public static void main(String[] args) {
        System.out.println("Backward: "
                + PrintEmployees.print(new ReverseListIterator<>(SampleLists.list())));
        System.out.println("Chain list: "
                + PrintEmployees.print(SampleLists.chain().createIterator()));
    }
}
