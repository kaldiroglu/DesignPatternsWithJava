package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem;

/**
 * Added by the catalog team after checkout was written. A gift card carries no tax — the
 * tax is charged when it is spent — and it is sent by e-mail, so it is not shipped.
 */
public record GiftCard(String name, int price) implements Item {
}
