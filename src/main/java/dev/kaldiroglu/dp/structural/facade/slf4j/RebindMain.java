package dev.kaldiroglu.dp.structural.facade.slf4j;

/** Same client code, different subsystem behind the facade. */
public final class RebindMain {

    public static void main(String[] args) {
        // Rebind to the JDK's java.util.logging, then obtain a new logger.
        System.out.println("== Rebinding the facade to java.util.logging ==");
        LoggerFactory.useEngine(new JulLoggingEngine());
        Logger jul = LoggerFactory.getLogger(RebindMain.class);
        jul.info("Now routed through java.util.logging - the client never changed");
    }
}
