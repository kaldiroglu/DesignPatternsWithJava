package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

import java.util.List;

/**
 * Homework 2: a chain where every link may act, and most links pass the request on.
 * <p>
 * GoF's chain stops at the first link that handles the request. A middleware chain is the
 * other form: each link does its own work — logging, checking — and then calls the rest
 * of the chain, unless it decides to answer itself. The link is a lambda that is given
 * the request and the rest of the chain.
 */
@FunctionalInterface
public interface Middleware {

    String handle(HttpRequest request, Endpoint next);

    /** Builds the chain: the first middleware in the list runs first. */
    static Endpoint chain(List<Middleware> links, Endpoint end) {
        Endpoint next = end;
        for (int i = links.size() - 1; i >= 0; i--) {
            Middleware link = links.get(i);
            Endpoint rest = next;
            next = request -> link.handle(request, rest);
        }
        return next;
    }
}
