package dev.kaldiroglu.dp.structural.bridge.retrofit;

import java.util.List;

/**
 * The Implementor: a vendor's own client library, in the vendor's own vocabulary.
 * <p>
 * Nothing here is designed for us. These are the calls the engine already had when the
 * standard arrived — open a session, pull rows, hand the session back — and they are what a
 * type 2 JDBC driver finds underneath it: a native client library that predates the standard
 * and will outlive this year's version of it.
 * <p>
 * Note what is <em>not</em> here: no {@code query}, no {@code report}, nothing shaped like
 * the interface we are required to expose. That is the point. If this interface mirrored the
 * required one there would be no bridge, only two names for the same thing.
 */
public interface VendorClient {

    /** What this engine calls itself in a log line. */
    String name();

    /** Open a session and return whatever handle this vendor uses for one. */
    String open(String database);

    /** Pull rows for a statement written in this vendor's own dialect. */
    List<String> pull(String handle, String statement);

    /** Give the session back. */
    void release(String handle);
}
