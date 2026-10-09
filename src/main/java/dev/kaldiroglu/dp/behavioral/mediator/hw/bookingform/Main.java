package dev.kaldiroglu.dp.behavioral.mediator.hw.bookingform;

/**
 * Books a room for six people. The small room disables Book and shows a warning; the
 * large room enables it. The form decides this, not the fields.
 */
public final class Main {

    public static void main(String[] args) {
        BookingForm form = new BookingForm();
        form.chooseRoom("Small");
        form.chooseDate("2026-10-12");
        form.setAttendees(6);
        System.out.println("Small room: Book enabled " + form.bookEnabled() + ", warning '" + form.warning() + "'");

        form.chooseRoom("Large");
        System.out.println("Large room: Book enabled " + form.bookEnabled() + ", warning '" + form.warning() + "'");

        form.clickBook();
        System.out.println("Booked: " + form.booked());
    }
}
