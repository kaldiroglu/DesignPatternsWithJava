package dev.kaldiroglu.dp.behavioral.iterator.hw.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Homework 2: the business days between two dates.
 * <p>
 * There is no collection behind this iterator. It holds a date and computes the next one
 * when asked, skipping weekends and holidays. A year of business days is never stored. This
 * is the point of the exercise: an iterator hides how elements are produced, and "stored in
 * a list" is only one way.
 */
public final class BusinessDays implements Iterable<LocalDate> {

    private final LocalDate from;
    private final LocalDate to;
    private final Set<LocalDate> holidays;

    /** Every business day from {@code from} up to and including {@code to}. */
    public BusinessDays(LocalDate from, LocalDate to, Set<LocalDate> holidays) {
        this.from = from;
        this.to = to;
        this.holidays = Set.copyOf(holidays);
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return new Iterator<>() {
            private LocalDate next = firstOnOrAfter(from);

            @Override
            public boolean hasNext() {
                return !next.isAfter(to);
            }

            @Override
            public LocalDate next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("no business days left");
                }
                LocalDate current = next;
                next = firstOnOrAfter(current.plusDays(1));
                return current;
            }
        };
    }

    private LocalDate firstOnOrAfter(LocalDate day) {
        LocalDate candidate = day;
        while (isWeekend(candidate) || holidays.contains(candidate)) {
            candidate = candidate.plusDays(1);
        }
        return candidate;
    }

    private static boolean isWeekend(LocalDate day) {
        return day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
