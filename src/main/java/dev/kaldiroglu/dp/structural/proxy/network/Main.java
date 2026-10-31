/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * Three requests: one refused by the telnet rule, one by the ftp rule, one allowed.
 */
public class Main {

    public static void main(String[] args) {
        Network network = NetworkServer.getInstance().getNetwork();
        String myIp = "10.0.0.2";
        Gateway gateway = Gateway.getInstance();

        // The log and the counts are shared by the whole JVM, so a run starts them at zero.
        Logger.clear();
        gateway.resetCounts();

        attempt(() -> network.telnet(myIp, "88.168.2.200"));   // refused: from 10.*
        attempt(() -> network.ftp(myIp, "192.168.2.200"));     // refused: to 192.*
        attempt(() -> network.ftp(myIp, "202.168.2.200"));     // allowed

        System.out.println();
        System.out.printf("requests logged by the proxy : %d%n", Logger.entries().size());
        System.out.printf("requests that reached the gateway : %d%n",
                gateway.ftpCalls() + gateway.telnetCalls());
        System.out.println("""

                Three requests logged, one forwarded. The two refused ones never
                reached the gateway — and the gateway contains no rule that could
                have refused them.

                Same shape as pm3.ProxyPM, in a domain with no people in it.""");
    }

    private interface Request {
        void send() throws ForbiddenAccessException;
    }

    private static void attempt(Request request) {
        try {
            request.send();
        } catch (ForbiddenAccessException e) {
            System.out.println("refused: " + e.getMessage());
        }
    }
}
