package dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.delegateObject;

/**
 * The <b>narrow interface</b> reified as a first-class object (GoF technique (b), p. 143). A
 * {@link DelegatingPowerAdapter} forwards its Target operations to whichever {@code PowerDelivery}
 * it is given &ndash; pluggability through <b>composition</b>.
 */
public interface PowerDelivery {

    void deliver();

    void cut();
}
