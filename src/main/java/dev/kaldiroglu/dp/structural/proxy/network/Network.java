/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 */

package dev.kaldiroglu.dp.structural.proxy.network;

/**
 * The Subject: what any network endpoint offers.
 * <p>
 * {@link Gateway} is the real thing and {@link ProxyServer} stands in front of it. Because
 * both implement this, {@link NetworkServer} can hand out either and no client can tell.
 */
public interface Network {

    void telnet(String ip, String targetIp) throws ForbiddenAccessException;

    void ftp(String ip, String targetIp) throws ForbiddenAccessException;
}
