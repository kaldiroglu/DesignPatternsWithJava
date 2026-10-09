package dev.kaldiroglu.dp.behavioral.iterator.hw;

import dev.kaldiroglu.dp.behavioral.iterator.hw.bom.PartIterator;
import dev.kaldiroglu.dp.behavioral.iterator.hw.bom.PartLine;
import dev.kaldiroglu.dp.behavioral.iterator.hw.calendar.BusinessDays;
import dev.kaldiroglu.dp.behavioral.iterator.hw.paging.PagedIterator;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.solution.ProductCatalog;
import dev.kaldiroglu.dp.structural.composite.bom.solution.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** The worked solutions of the Iterator homework, and the figures its speaker notes quote. */
class HomeworkTest {

    // ------------------------------------------------------------ 1 · parts of a bicycle

    @Test
    @DisplayName("two wheels with 36 spokes each give one line of 72 spokes")
    void spokesAreMultipliedDownTheTree() {
        ProductCatalog.Bicycle bike = ProductCatalog.cityBicycle();
        bike.wheel().changeQuantity(bike.spoke(), 36);

        List<PartLine> spokes = new ArrayList<>();
        for (PartLine line : PartIterator.partsOf(bike.bicycle())) {
            if (line.part() == bike.spoke()) {
                spokes.add(line);
            }
        }

        assertEquals(1, spokes.size(), "one wheel object used twice gives one spoke line");
        assertEquals(72, spokes.getFirst().quantity());
        assertEquals("72 x " + bike.spoke().name(), spokes.getFirst().toString());
    }

    @Test
    @DisplayName("the part iterator returns parts only: assemblies and services are skipped")
    void onlyPartsComeOut() {
        ProductCatalog.Bicycle bike = ProductCatalog.cityBicycle();
        bike.frame().add(new Service(Catalog.POWDER_COATING));

        List<String> names = new ArrayList<>();
        PartIterator.partsOf(bike.bicycle()).forEach(line -> names.add(line.part().name()));

        assertFalse(names.contains(bike.wheel().name()));
        assertFalse(names.contains(bike.hub().name()));
        assertFalse(names.contains(Catalog.POWDER_COATING.name()));
        assertTrue(names.contains(bike.spoke().name()));
    }

    @Test
    @DisplayName("the part iterator throws NoSuchElementException after the last part")
    void partIteratorEnds() {
        PartIterator parts = new PartIterator(ProductCatalog.cityBicycle().hub());
        while (parts.hasNext()) {
            parts.next();
        }

        assertThrows(NoSuchElementException.class, parts::next);
    }

    // ------------------------------------------------------------ 2 · business days

    @Test
    @DisplayName("business days skip weekends and holidays, and no list of dates is stored")
    void businessDaysSkipWeekendsAndHolidays() {
        // Friday 2026-10-23 to Friday 2026-10-30; 2026-10-29 is a holiday.
        LocalDate holiday = LocalDate.of(2026, 10, 29);
        BusinessDays days = new BusinessDays(LocalDate.of(2026, 10, 23), LocalDate.of(2026, 10, 30),
                Set.of(holiday));

        List<LocalDate> walked = new ArrayList<>();
        days.forEach(walked::add);

        assertEquals(List.of(
                LocalDate.of(2026, 10, 23),
                LocalDate.of(2026, 10, 26),
                LocalDate.of(2026, 10, 27),
                LocalDate.of(2026, 10, 28),
                LocalDate.of(2026, 10, 30)), walked);
    }

    // ------------------------------------------------------------ 3 · paged results

    @Test
    @DisplayName("a caller that stops early fetches only the pages up to the one where it stopped")
    void pagesAfterTheStopAreNeverFetched() {
        List<Integer> asked = new ArrayList<>();
        PagedIterator<String> results = new PagedIterator<>(number -> {
            asked.add(number);
            return number < 5 ? List.of("p" + number + "a", "p" + number + "b") : List.of();
        });

        String found = null;
        while (results.hasNext()) {
            String item = results.next();
            if (item.equals("p1a")) {
                found = item;
                break;
            }
        }

        assertEquals("p1a", found);
        assertEquals(2, results.pagesFetched());
        assertEquals(List.of(0, 1), asked);
    }

    @Test
    @DisplayName("a caller that walks to the end gets every item, and the empty page ends the walk")
    void walkingToTheEndFetchesEveryPage() {
        PagedIterator<String> results = new PagedIterator<>(
                number -> number < 3 ? List.of("p" + number) : List.of());

        List<String> all = new ArrayList<>();
        results.forEachRemaining(all::add);

        assertEquals(List.of("p0", "p1", "p2"), all);
        assertEquals(4, results.pagesFetched(), "three pages and the empty one");
        assertThrows(NoSuchElementException.class, results::next);
    }
}
