/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * The RealSubject: the gateway that actually moves the traffic.
 * <p>
 * It has no idea it is being protected, and no access rule appears anywhere in it.
 */
public class Gateway implements Network {

    private static final Gateway INSTANCE = new Gateway();

    private int ftpCalls;
    private int telnetCalls;

    public static Gateway getInstance() {
        return INSTANCE;
    }

    @Override
    public void ftp(String ip, String targetIp) {
        ftpCalls++;
        System.out.println(ip + " makes an ftp to " + targetIp);
    }

    @Override
    public void telnet(String ip, String targetIp) {
        telnetCalls++;
        System.out.println(ip + " makes a telnet to " + targetIp);
    }

    public int ftpCalls() {
        return ftpCalls;
    }

    public int telnetCalls() {
        return telnetCalls;
    }

    public void resetCounts() {
        ftpCalls = 0;
        telnetCalls = 0;
    }
}
