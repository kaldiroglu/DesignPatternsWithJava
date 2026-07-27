package dev.kaldiroglu.dp.structural.decorator.toast.problem;

public class SausageToast extends AbstractToast {

    public SausageToast() {
        name = "Sausage toast";
    }

    @Override
    public int calculatePrice() {
        return 6;
    }
}
