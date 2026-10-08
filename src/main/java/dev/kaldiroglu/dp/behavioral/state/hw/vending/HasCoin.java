package dev.kaldiroglu.dp.behavioral.state.hw.vending;

final class HasCoin implements VendingState {

    public VendingState insertCoin(VendingMachine machine) {
        machine.log("coin returned: one coin is enough");
        return this;
    }

    public VendingState pressButton(VendingMachine machine) {
        machine.takeOneDrink();
        machine.log("drink given");
        return machine.stock() > 0 ? new WaitingForCoin() : new SoldOut();
    }
}
