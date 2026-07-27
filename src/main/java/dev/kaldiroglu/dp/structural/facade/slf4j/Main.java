package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * Client. It logs through the {@link Logger} facade and {@link LoggerFactory}
 * only -- notice it never imports {@link ConsoleLoggingEngine},
 * {@link JulLoggingEngine}, {@link LogEvent}, or any other subsystem class.
 */
public final class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Uses the default backend (console at INFO). The application code below is
        // identical no matter which backend the facade is bound to.
        log.info("Application starting");
        log.info("Loaded {} rows for user {}", 42, "john");   // {} placeholders
        log.warn("Disk usage at {}%", 91);
        log.error("Failed to reach {} after {} retries", "db-primary", 3);

        // DEBUG is below the INFO threshold. The {} form already avoids building the
        // final string, but the ARGUMENT expression would still be evaluated -- so for
        // a genuinely expensive argument, guard it with isDebugEnabled():
        if (log.isDebugEnabled()) {
            log.debug("Full request dump: {}", expensiveDump());
        }
        System.out.println("isDebugEnabled() = " + log.isDebugEnabled());

        // Same client code, different subsystem behind the facade: rebind to the
        // JDK's java.util.logging, then obtain a new logger.
        System.out.println("\n== Rebinding the facade to java.util.logging ==");
        LoggerFactory.useEngine(new JulLoggingEngine());
        Logger jul = LoggerFactory.getLogger(Main.class);
        jul.info("Now routed through java.util.logging - the client never changed");
    }

    private static String expensiveDump() {
        // Stands in for costly work (serialization, large string building) that we
        // must not pay for when the level is disabled.
        return "...lots of expensive-to-build detail...";
    }
}
