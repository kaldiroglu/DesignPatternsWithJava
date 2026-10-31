package dev.kaldiroglu.dp.structural.decorator.toast.problem;

import java.util.List;

/** The five items the shop can actually sell, one class per item. */
final class Menu {

    private Menu() {
    }

    static List<Toast> items() {
        return List.of(
                new CheeseToast(),
                new CheeseTomatoToast(),
                new SausageToast(),
                new SausageTomatoToast(),
                new CheeseSausageToast());
    }
}
