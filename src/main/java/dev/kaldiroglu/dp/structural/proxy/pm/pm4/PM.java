package dev.kaldiroglu.dp.structural.proxy.pm.pm4;

/**
 * The Subject: what a citizen is entitled to ask of the Prime Minister's office.
 * <p>
 * The same interface as in {@code pm3}. In this stage {@code Citizen} depends on it and on
 * nothing else, so a citizen cannot tell whether it holds the proxy or the real subject.
 */
public interface PM {

    void listen(String problem);

    void findJob(String name);
}
