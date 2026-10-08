package dev.kaldiroglu.dp.behavioral.state.hw.vending;

final class SoldOut implements VendingState {

    public VendingState insertCoin(VendingMachine machine) {
        machine.log("coin returned: sold out");
        return this;
    }

    public VendingState pressButton(VendingMachine machine) {
        machine.log("sold out");
        return this;
    }

    @Override
    public VendingState refilled(VendingMachine machine) {
        return machine.stock() > 0 ? new WaitingForCoin() : this;
    }
}
