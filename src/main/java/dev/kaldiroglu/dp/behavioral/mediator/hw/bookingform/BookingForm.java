package dev.kaldiroglu.dp.behavioral.mediator.hw.bookingform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Homework 3: a meeting-room booking form, with the form itself as the <b>Mediator</b>.
 * <p>
 * Three fields depend on each other: the room has a capacity, the number of attendees must
 * fit in it, and Book is enabled only when a room and a date are chosen and the people fit.
 * Each field reports a change; the form decides what follows, in {@link #changed}. No
 * field knows another.
 */
public final class BookingForm {

    private static final Map<String, Integer> CAPACITY = Map.of("Small", 4, "Large", 12);

    private String room = "";
    private String date = "";
    private int attendees;
    private boolean bookEnabled;
    private String warning = "";
    private final List<String> booked = new ArrayList<>();

    public void chooseRoom(String room) {
        this.room = room;
        changed();
    }

    public void chooseDate(String date) {
        this.date = date;
        changed();
    }

    public void setAttendees(int attendees) {
        this.attendees = attendees;
        changed();
    }

    public void clickBook() {
        if (bookEnabled) {
            booked.add(room + " on " + date + " for " + attendees);
        }
    }

    /** Every rule of the form, in one place. */
    private void changed() {
        int capacity = CAPACITY.getOrDefault(room, 0);
        boolean fits = attendees > 0 && attendees <= capacity;
        warning = !room.isEmpty() && attendees > capacity
                ? room + " holds " + capacity + " people"
                : "";
        bookEnabled = !room.isEmpty() && !date.isEmpty() && fits;
    }

    public boolean bookEnabled() {
        return bookEnabled;
    }

    public String warning() {
        return warning;
    }

    public List<String> booked() {
        return List.copyOf(booked);
    }
}
