package dev.kaldiroglu.dp.behavioral.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

import static org.junit.jupiter.api.Assertions.*;

/** The Java rows of the Observer deck's known-uses table, checked against the running JDK. */
class KnownUsesTest {

    @Test
    @DisplayName("PropertyChangeSupport tells listeners the property name, old value and new value")
    void propertyChangeSupport() {
        PropertyChangeSupport support = new PropertyChangeSupport(this);
        List<PropertyChangeEvent> events = new ArrayList<>();
        support.addPropertyChangeListener(events::add);

        support.firePropertyChange("price", 100, 102);

        assertEquals(1, events.size());
        assertEquals("price", events.getFirst().getPropertyName());
        assertEquals(100, events.getFirst().getOldValue());
        assertEquals(102, events.getFirst().getNewValue());
    }

    @Test
    @DisplayName("SubmissionPublisher is a Flow publisher, and a subscriber asks how many items it can take")
    void flow() throws Exception {
        assertTrue(Flow.Publisher.class.isAssignableFrom(SubmissionPublisher.class));
        assertNotNull(Flow.Subscription.class.getMethod("request", long.class));
    }

    @Test
    @DisplayName("ActionListener is a listener interface with one method")
    void actionListener() {
        assertTrue(java.util.EventListener.class.isAssignableFrom(java.awt.event.ActionListener.class));
        assertEquals(1, java.awt.event.ActionListener.class.getDeclaredMethods().length);
    }
}
