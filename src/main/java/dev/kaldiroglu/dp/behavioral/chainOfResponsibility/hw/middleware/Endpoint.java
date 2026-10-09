package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

/** The end of the line: something that turns a request into a response. */
@FunctionalInterface
public interface Endpoint {

    String serve(HttpRequest request);
}
