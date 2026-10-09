package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

/** A request to a web server: the path and the user who sent it. */
public record HttpRequest(String path, String user) {
}
