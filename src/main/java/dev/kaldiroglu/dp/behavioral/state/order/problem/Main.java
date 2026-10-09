package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Runs the three stages. Flags and the switch work; the enum constants work for the first
 * shipment, but the second shipment's failures are counted from 4 and never send it back.
 */
public final class Main {

    public static void main(String[] args) {
        FlagOrder flags = new FlagOrder();
        flags.pay();
        flags.ship("TR-1");
        flags.deliver();
        System.out.println("Stage one, four flags:   " + flags.events());

        SwitchingOrder switching = new SwitchingOrder();
        switching.pay();
        switching.ship("TR-1");
        try {
            switching.cancel();
        } catch (IllegalStateException refused) {
            System.out.println("Stage two, one switch:   " + refused.getMessage());
        }

        EnumOrder order = new EnumOrder();
        order.pay();
        order.ship("TR-1");
        for (int i = 0; i < 3; i++) {
            order.failDelivery();
        }
        System.out.println("Stage three, first shipment:  " + order.events() + " -> " + order.status());
        order.ship("TR-2");
        for (int i = 0; i < 3; i++) {
            order.failDelivery();
        }
        System.out.println("Stage three, second shipment: " + order.events().subList(
                order.events().indexOf("shipped TR-2"), order.events().size()) + " -> " + order.status());
        System.out.println("The count was not reset, so it never equals 3 again: the courier has no limit.");
    }
}
