package dev.kaldiroglu.dp.structural.proxy.hw.licence;

/**
 * <b>Subject</b> (GoF, p. 209). What a student wants to use — and the only type the
 * student's code ever names.
 *
 * <p>The licence check is not on this interface, which is the whole point. A student writes
 * {@code app.launch()} and either it starts or it says why not; nothing in the type tells
 * her a seat had to be found first.</p>
 */
public interface Application {

    /** Starts the application. Expensive: it is the thing being rationed. */
    void launch();

    String open(String document);

    /** Finished with it. For the proxy, this is also when the seat goes back. */
    void close();
}
