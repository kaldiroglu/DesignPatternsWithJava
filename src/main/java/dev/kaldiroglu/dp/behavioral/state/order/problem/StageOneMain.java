package dev.kaldiroglu.dp.behavioral.state.order.problem;

/**
 * Stage one: four flags. An order is paid, shipped and delivered.
 */
public final class StageOneMain {

    public static void main(String[] args) {
        FlagOrder flags = new FlagOrder();
        flags.pay();
        flags.ship("TR-1");
        flags.deliver();
        System.out.println("Stage one, four flags:   " + flags.events());
    }
}
