package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/**
 * Three screens and a lambda listen to the feed. Every change reaches all of them, and the
 * alert at 105 fires.
 */
public final class ListenersMain {

    public static void main(String[] args) {
        System.out.println(TenSeconds.listeners());
    }
}
