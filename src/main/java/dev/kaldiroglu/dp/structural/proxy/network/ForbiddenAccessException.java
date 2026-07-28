/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * Thrown by the proxy when a request is not permitted.
 * <p>
 * Renamed from {@code YasakKardesimException}: the class and its messages were originally in
 * Turkish, and this repository keeps all identifiers and output in English.
 */
public class ForbiddenAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
