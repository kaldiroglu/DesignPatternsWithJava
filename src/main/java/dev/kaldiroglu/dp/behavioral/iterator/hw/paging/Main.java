package dev.kaldiroglu.dp.behavioral.iterator.hw.paging;

import java.util.List;

/** Searches paged results and stops early, so the pages after the stop are never fetched. */
public class Main {

    public static void main(String[] args) {
        PagedIterator<String> results = new PagedIterator<>(number -> {
            System.out.println("  fetching page " + number);
            return number < 5 ? List.of("p" + number + "a", "p" + number + "b") : List.of();
        });

        System.out.println("Looking for p1a in five pages of results:");
        while (results.hasNext()) {
            String item = results.next();
            System.out.println("  read " + item);
            if (item.equals("p1a")) {
                break;
            }
        }
        System.out.println("Found p1a. Pages fetched: " + results.pagesFetched() + " of 5.");
    }
}
