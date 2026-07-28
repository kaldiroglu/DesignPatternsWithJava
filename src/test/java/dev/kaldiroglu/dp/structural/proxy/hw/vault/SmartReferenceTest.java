package dev.kaldiroglu.dp.structural.proxy.hw.vault;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** GoF's three smart-reference duties: counting, loading on first access, and locking. */
class SmartReferenceTest {

    private DocumentHandle contract;

    @BeforeEach
    void setUp() {
        StoredDocument.resetOpens();
        contract = new DocumentHandle("contract.txt", "the original terms");
    }

    @Test
    @DisplayName("the document is not opened until somebody reads it")
    void loadedOnFirstAccess() {
        contract.acquire();
        assertFalse(contract.isOpen());
        assertEquals(0, StoredDocument.opensPerformed());

        contract.read();
        assertTrue(contract.isOpen());
        assertEquals(1, StoredDocument.opensPerformed());
    }

    @Test
    @DisplayName("two holders, two closes, one actual close")
    void referenceCounting() {
        contract.acquire();
        contract.acquire();
        contract.read();
        assertEquals(2, contract.holders());

        contract.close();
        assertTrue(contract.isOpen(), "one holder left, so it stays open");
        assertEquals(1, contract.holders());

        contract.close();
        assertFalse(contract.isOpen(), "the last one out shuts the door");
        assertEquals(0, contract.holders());
    }

    @Test
    @DisplayName("a second writer is refused rather than silently overwriting the first")
    void locking() {
        contract.acquire();
        contract.lockForWriting("Ayse");
        contract.writeAs("Ayse", "the amended terms");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> contract.write("Bora's version"));
        assertTrue(thrown.getMessage().contains("being edited by Ayse"));

        contract.unlock("Ayse");
        assertEquals("the amended terms", contract.read());
    }

    @Test
    @DisplayName("somebody who does not hold the lock cannot write through it")
    void theLockIsOwned() {
        contract.acquire();
        contract.lockForWriting("Ayse");

        assertThrows(IllegalStateException.class, () -> contract.writeAs("Bora", "no"));
        assertThrows(IllegalStateException.class, () -> contract.lockForWriting("Bora"));
    }

    @Test
    @DisplayName("using it without acquiring it is a mistake, and says so")
    void mustAcquireFirst() {
        assertThrows(IllegalStateException.class, () -> contract.read());
        assertThrows(IllegalStateException.class, () -> contract.write("x"));
    }

    @Test
    @DisplayName("closing more often than acquiring is harmless")
    void extraClosesAreIgnored() {
        contract.acquire();
        contract.read();
        contract.close();
        contract.close();

        assertEquals(0, contract.holders());
        assertFalse(contract.isOpen());
    }

    @Test
    @DisplayName("the handle is substitutable for the document")
    void substitutable() {
        assertTrue(Document.class.isAssignableFrom(DocumentHandle.class));
        assertTrue(Document.class.isAssignableFrom(StoredDocument.class));
    }
}
