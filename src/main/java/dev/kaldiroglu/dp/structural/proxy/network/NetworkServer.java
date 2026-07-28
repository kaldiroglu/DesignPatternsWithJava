/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * How a client obtains a {@link Network} — and why it can never obtain the real one.
 * <p>
 * The server hands out a {@link ProxyServer}. The returned type is {@code Network}, so no
 * client can tell, and none is ever given a reference to the {@link Gateway}.
 */
public class NetworkServer {

    private static final NetworkServer INSTANCE = new NetworkServer();

    private final Network network;

    private NetworkServer() {
        network = new ProxyServer();
    }

    public static NetworkServer getInstance() {
        return INSTANCE;
    }

    public Network getNetwork() {
        return network;
    }
}
