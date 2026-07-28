package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

/** The Subject: somewhere a price can be obtained from. */
public interface PriceService {

    Money priceOf(String sku);
}
