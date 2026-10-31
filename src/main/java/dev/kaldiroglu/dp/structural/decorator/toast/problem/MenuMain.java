package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/** Runs the menu the shop can actually sell. */
public class MenuMain {

    public static void main(String[] args) {
        System.out.println("The menu, one class per item:");
        Menu.items().forEach(toast -> System.out.println("  " + toast));
    }
}
