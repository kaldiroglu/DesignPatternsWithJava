package dev.kaldiroglu.dp.behavioral.iterator.hw.calendar;

import java.time.LocalDate;
import java.util.Set;

/**
 * Walks the business days of one week, skipping the weekend and a holiday, without storing a
 * list of dates.
 */
public class Main {

    public static void main(String[] args) {
        LocalDate holiday = LocalDate.of(2026, 10, 29);
        BusinessDays days = new BusinessDays(LocalDate.of(2026, 10, 23), LocalDate.of(2026, 10, 30),
                Set.of(holiday));

        System.out.println("Business days from 2026-10-23 to 2026-10-30, with "
                + holiday + " a holiday:");
        for (LocalDate day : days) {
            System.out.println("  " + day + " " + day.getDayOfWeek());
        }
    }
}
