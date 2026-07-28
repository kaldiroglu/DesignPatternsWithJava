/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Records what the proxy was asked to do.
 * <p>
 * The original printed {@code new Date()}, so no two runs agreed and nothing could be
 * asserted. Entries are now kept in a list as well as printed, which is what lets a test
 * show that a refused request <em>was still logged</em>.
 */
public final class Logger {

    private static final List<String> ENTRIES = new ArrayList<>();

    private Logger() {
    }

    public static void log(String message) {
        ENTRIES.add(message);
        System.out.println("[log] " + message);
    }

    public static List<String> entries() {
        return List.copyOf(ENTRIES);
    }

    public static void clear() {
        ENTRIES.clear();
    }
}
