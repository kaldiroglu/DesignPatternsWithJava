package dev.kaldiroglu.dp.behavioral.state.hw.vending;

final class WaitingForCoin implements VendingState {

    public VendingState insertCoin(VendingMachine machine) {
        machine.log("coin accepted");
        return new HasCoin();
    }

    public VendingState pressButton(VendingMachine machine) {
        machine.log("insert a coin first");
        return this;
    }
}
