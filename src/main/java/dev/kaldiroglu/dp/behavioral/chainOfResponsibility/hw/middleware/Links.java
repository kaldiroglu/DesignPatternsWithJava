package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

import java.util.List;

/** Three links: one that logs and passes on, one that may stop the request, one that adds a header. */
public final class Links {

    private Links() {
    }

    /** Writes every path to the log, then passes the request on. */
    public static Middleware logging(List<String> log) {
        return (request, next) -> {
            log.add(request.user() + " " + request.path());
            return next.serve(request);
        };
    }

    /** Answers 403 itself for an admin page when the user is not an admin. */
    public static Middleware adminOnly() {
        return (request, next) -> {
            if (request.path().startsWith("/admin") && !request.user().equals("admin")) {
                return "403 forbidden";
            }
            return next.serve(request);
        };
    }

    /** Lets the rest of the chain answer, then adds a header to the answer. */
    public static Middleware poweredBy() {
        return (request, next) -> next.serve(request) + " [powered by the chain]";
    }
}
