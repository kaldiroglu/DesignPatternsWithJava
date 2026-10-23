package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/**
 * The same ten seconds of prices, read by stage three's polling readers and by listeners.
 */
public final class TwoWaysMain {

    public static void main(String[] args) {
        System.out.println(TenSeconds.polling());
        System.out.println(TenSeconds.listeners());
    }
}
