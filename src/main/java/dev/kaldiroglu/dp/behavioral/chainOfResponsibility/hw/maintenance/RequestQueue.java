package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.hw.maintenance;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * The queue from the homework. It knows only the first developer in the chain, and never
 * which developer takes which kind of request.
 */
public final class RequestQueue {

    private final Queue<Request> waiting = new ArrayDeque<>();
    private final Developer first;

    public RequestQueue(Developer first) {
        this.first = first;
    }

    public void add(Request request) {
        waiting.add(request);
    }

    public List<String> processAll() {
        List<String> assignments = new ArrayList<>();
        while (!waiting.isEmpty()) {
            assignments.add(first.take(waiting.remove()));
        }
        return assignments;
    }
}
