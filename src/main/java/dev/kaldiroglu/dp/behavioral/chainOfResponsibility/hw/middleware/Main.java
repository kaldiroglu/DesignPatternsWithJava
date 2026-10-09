package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.middleware;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends three requests through a logging, an admin check and a header link. Every request
 * is logged; the admin check stops Elif at the admin page.
 */
public final class Main {

    public static void main(String[] args) {
        List<String> log = new ArrayList<>();
        Endpoint app = Middleware.chain(
                List.of(Links.logging(log), Links.adminOnly(), Links.poweredBy()),
                request -> "200 " + request.path());

        System.out.println("elif  /home:        " + app.serve(new HttpRequest("/home", "elif")));
        System.out.println("elif  /admin/users: " + app.serve(new HttpRequest("/admin/users", "elif")));
        System.out.println("admin /admin/users: " + app.serve(new HttpRequest("/admin/users", "admin")));
        System.out.println("The log: " + log);
    }
}
