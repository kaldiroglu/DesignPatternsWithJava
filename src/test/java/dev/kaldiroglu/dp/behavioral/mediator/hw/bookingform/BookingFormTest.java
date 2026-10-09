package dev.kaldiroglu.dp.behavioral.mediator.hw.bookingform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 3: the booking form decides, in one method, whether Book is enabled. */
class BookingFormTest {

    @Test
    @DisplayName("Book is enabled only when a room and a date are chosen and the people fit")
    void bookNeedsAllThree() {
        BookingForm form = new BookingForm();
        form.chooseRoom("Small");
        assertFalse(form.bookEnabled());
        form.chooseDate("2026-10-12");
        assertFalse(form.bookEnabled(), "no attendees yet");
        form.setAttendees(4);
        assertTrue(form.bookEnabled());

        form.clickBook();
        assertEquals(List.of("Small on 2026-10-12 for 4"), form.booked());
    }

    @Test
    @DisplayName("too many people for the room disables Book and warns, and a larger room fixes both")
    void theRoomIsTooSmall() {
        BookingForm form = new BookingForm();
        form.chooseDate("2026-10-12");
        form.setAttendees(6);
        form.chooseRoom("Small");

        assertFalse(form.bookEnabled());
        assertEquals("Small holds 4 people", form.warning());
        form.clickBook();
        assertTrue(form.booked().isEmpty());

        form.chooseRoom("Large");
        assertTrue(form.bookEnabled());
        assertEquals("", form.warning());
    }
}
