package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import java.util.ArrayList;
import java.util.List;

/** A subsystem class. */
public class ReceiptService {

    private final List<String> sent = new ArrayList<>();

    public void email(String customerId, String paymentReference, String shipmentReference) {
        sent.add(customerId + "/" + paymentReference + "/" + shipmentReference);
    }

    public List<String> sent() {
        return List.copyOf(sent);
    }
}
