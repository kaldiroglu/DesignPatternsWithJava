package dev.kaldiroglu.dp.behavioral.state.hw.vending;

/** The <b>State</b>. Each state answers the three requests and returns the next state. */
sealed interface VendingState permits WaitingForCoin, HasCoin, SoldOut {

    VendingState insertCoin(VendingMachine machine);

    VendingState pressButton(VendingMachine machine);

    default VendingState refilled(VendingMachine machine) {
        return this;
    }
}
