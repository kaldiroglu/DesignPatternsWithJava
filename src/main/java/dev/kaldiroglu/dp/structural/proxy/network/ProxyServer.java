/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * The Proxy — a <em>protection proxy</em> (GoF, p. 208), which is to say a firewall.
 * <p>
 * It implements {@link Network} and holds a {@link Network}, so it is substitutable for the
 * real gateway. Every request is logged, then checked, and only then forwarded. A refused
 * request never reaches the gateway at all.
 * <p>
 * This is the same role {@code proxy.pm.pm3.ProxyPM} plays, in a domain with no people in it
 * — which is a useful pairing: the solution is about controlling access, not about politeness.
 */
public class ProxyServer implements Network {

    private final Network gateway;

    public ProxyServer() {
        this(Gateway.getInstance());
    }

    public ProxyServer(Network gateway) {
        this.gateway = gateway;
    }

    @Override
    public void ftp(String ip, String targetIp) throws ForbiddenAccessException {
        Logger.log(ip + " wants to ftp to " + targetIp);
        filter("ftp", ip, targetIp);
        gateway.ftp(ip, targetIp);
    }

    @Override
    public void telnet(String ip, String targetIp) throws ForbiddenAccessException {
        Logger.log(ip + " wants to telnet to " + targetIp);
        filter("telnet", ip, targetIp);
        gateway.telnet(ip, targetIp);
    }

    /** The access rules. The gateway knows nothing about them. */
    private void filter(String protocol, String ip, String targetIp)
            throws ForbiddenAccessException {

        if (protocol.equals("ftp") && targetIp.startsWith("192")) {
            throw new ForbiddenAccessException("ftp to " + targetIp + " is not permitted");
        }
        if (protocol.equals("telnet") && ip.startsWith("10")) {
            throw new ForbiddenAccessException("telnet from " + ip + " is not permitted");
        }
    }
}
