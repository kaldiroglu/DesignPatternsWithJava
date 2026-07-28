package dev.kaldiroglu.dp.structural.proxy.pm.pm3;

/**
 * The Subject: what a citizen is entitled to ask of the Prime Minister's office.
 * <p>
 * This one interface is the whole difference between {@code pm2} and the pattern. Because
 * {@link RealPM} and {@link ProxyPM} both implement it, a citizen holding a {@code PM} cannot
 * tell which she has — and does not need to be rewritten when the answer changes.
 */
public interface PM {

    void listen(String problem);

    void findJob(String name);
}
