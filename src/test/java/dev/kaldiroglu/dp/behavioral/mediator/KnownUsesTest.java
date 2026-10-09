package dev.kaldiroglu.dp.behavioral.mediator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import static org.junit.jupiter.api.Assertions.*;

/** The JDK known use quoted in the Part 4 notes: a ButtonGroup is the mediator of its buttons. */
class KnownUsesTest {

    @BeforeAll
    static void noScreen() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    @DisplayName("select Large and Small is deselected, although neither button knows the other: small false, large true")
    void aButtonGroup() {
        JRadioButton small = new JRadioButton("Small");
        JRadioButton large = new JRadioButton("Large");
        ButtonGroup group = new ButtonGroup();
        group.add(small);
        group.add(large);

        small.setSelected(true);
        large.setSelected(true);

        assertFalse(small.isSelected());
        assertTrue(large.isSelected());
    }
}
