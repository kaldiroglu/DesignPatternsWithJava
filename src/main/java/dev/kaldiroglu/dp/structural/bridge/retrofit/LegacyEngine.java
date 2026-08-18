package dev.kaldiroglu.dp.structural.bridge.retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * The system from last decade, and the reason none of this can be rewritten.
 * <p>
 * It works, it is fast, and it has callers all over the company that use it directly and are
 * not going to stop. {@link #reportDirectly(String)} is one of them: a caller that predates
 * the standard entirely and must keep working untouched.
 */
public class LegacyEngine implements VendorClient {

    private final List<String> calls = new ArrayList<>();
    private int sessions;

    @Override
    public String name() {
        return "legacy";
    }

    @Override
    public String open(String database) {
        sessions++;
        return "legacy-session-" + sessions + ":" + database;
    }

    @Override
    public List<String> pull(String handle, String statement) {
        calls.add(statement);
        return List.of(handle + " | " + statement.toUpperCase());
    }

    @Override
    public void release(String handle) {
        sessions--;
    }

    /** A caller that has existed for ten years and knows nothing about any standard. */
    public String reportDirectly(String statement) {
        String handle = open("payroll");
        String row = pull(handle, statement).getFirst();
        release(handle);
        return row;
    }

    /** So a test can show which statements actually reached the engine. */
    public List<String> statementsSeen() {
        return List.copyOf(calls);
    }

    public int openSessions() {
        return sessions;
    }
}
