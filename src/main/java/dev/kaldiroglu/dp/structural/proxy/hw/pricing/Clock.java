package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

/** Time, injected — so the cache's expiry can be tested without any test sleeping. */
public interface Clock {

    long millis();

    static ManualClock manual() {
        return new ManualClock();
    }

    /** A clock that moves only when a test tells it to. */
    final class ManualClock implements Clock {

        private long now;

        @Override
        public long millis() {
            return now;
        }

        public void advance(long millis) {
            now += millis;
        }
    }
}
