package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Stage two: one status and a switch. A shipped order refuses to be cancelled.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        SwitchingOrder switching = new SwitchingOrder();
        switching.pay();
        switching.ship("TR-1");
        try {
            switching.cancel();
        } catch (IllegalStateException refused) {
            System.out.println("Stage two, one switch:   " + refused.getMessage());
        }
    }
}
