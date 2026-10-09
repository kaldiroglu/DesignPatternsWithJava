package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 2: a middleware chain, where every link may act and most pass the request on. */
class MiddlewareTest {

    @Test
    @DisplayName("every link runs for a normal page, in the order of the list")
    void everyLinkRuns() {
        List<String> log = new ArrayList<>();
        Endpoint server = Middleware.chain(
                List.of(Links.logging(log), Links.adminOnly(), Links.poweredBy()),
                request -> "200 " + request.path());

        assertEquals("200 /home [powered by the chain]", server.serve(new HttpRequest("/home", "elif")));
        assertEquals(List.of("elif /home"), log);
    }

    @Test
    @DisplayName("the admin check answers 403 itself, so the links after it never run")
    void theAdminCheckStopsTheRequest() {
        List<String> log = new ArrayList<>();
        List<String> reached = new ArrayList<>();
        Endpoint server = Middleware.chain(
                List.of(Links.logging(log), Links.adminOnly(), Links.poweredBy()),
                request -> {
                    reached.add(request.path());
                    return "200 " + request.path();
                });

        assertEquals("403 forbidden", server.serve(new HttpRequest("/admin/users", "elif")));
        assertEquals(List.of("elif /admin/users"), log, "the logging link before it still ran");
        assertTrue(reached.isEmpty());
        assertEquals("200 /admin/users [powered by the chain]",
                server.serve(new HttpRequest("/admin/users", "admin")));
    }

    @Test
    @DisplayName("the list decides the order: with the admin check first, a refused request is not logged")
    void theListDecidesTheOrder() {
        List<String> log = new ArrayList<>();
        Endpoint server = Middleware.chain(
                List.of(Links.adminOnly(), Links.logging(log)),
                request -> "200 " + request.path());

        assertEquals("403 forbidden", server.serve(new HttpRequest("/admin", "elif")));
        assertTrue(log.isEmpty());
    }
}
