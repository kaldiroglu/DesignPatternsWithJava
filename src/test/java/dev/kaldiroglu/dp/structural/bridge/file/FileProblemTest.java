package dev.kaldiroglu.dp.structural.bridge.file;

import dev.kaldiroglu.dp.structural.bridge.file.problem.Department;
import dev.kaldiroglu.dp.structural.bridge.file.problem.EvernoteBoundFinanceManager;
import dev.kaldiroglu.dp.structural.bridge.file.problem.FinanceEvernoteManager;
import dev.kaldiroglu.dp.structural.bridge.file.problem.FinanceSharepointManager;
import dev.kaldiroglu.dp.structural.bridge.file.problem.InsuranceEvernoteManager;
import dev.kaldiroglu.dp.structural.bridge.file.problem.Store;
import dev.kaldiroglu.dp.structural.bridge.file.problem.SwitchingFileManager;
import dev.kaldiroglu.dp.structural.bridge.file.problem.VendorStores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The three naive designs, and the way each of them fails.
 * <p>
 * Each design works. That is the point of the package: none of them is stupid, and every one
 * of them is what a reasonable team writes next. What these tests measure is the bill.
 */
class FileProblemTest {

    private static final String PATH = "claims/2026/case-4021";
    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/structural/bridge/file/problem/";

    private final VendorStores stores = new VendorStores();

    private static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }

    // ------------------------------------------- design 1: a switch on each axis

    @Test
    @DisplayName("switch: it works, and both departments get their own rule on Evernote")
    void theSwitchWorks() {
        SwitchingFileManager manager = new SwitchingFileManager(stores);
        for (int i = 1; i <= 8; i++) {
            manager.save(Department.FINANCE, Store.EVERNOTE, PATH, "draft " + i);
            manager.save(Department.INSURANCE, Store.EVERNOTE, PATH, "draft " + i);
        }

        assertEquals(5, stores.versionsHeld("Evernote", "finance/" + PATH));
        assertEquals(2, stores.versionsHeld("Evernote", "insurance/" + PATH));
        assertEquals("draft 8", stores.latestContent("Evernote", "finance/" + PATH));
    }

    @Test
    @DisplayName("switch: insurance keeps two versions on Evernote and all eight on FileNet")
    void theForgottenRetentionRule() {
        SwitchingFileManager manager = new SwitchingFileManager(stores);
        for (int i = 1; i <= 8; i++) {
            manager.save(Department.INSURANCE, Store.EVERNOTE, PATH, "draft " + i);
            manager.save(Department.INSURANCE, Store.FILENET, PATH, "draft " + i);
        }

        assertEquals(2, stores.versionsHeld("Evernote", "insurance/" + PATH));

        // The same department, the same rule, a different store — and the rule is simply
        // not there. Nothing threw. Insurance is now holding six versions it is not allowed
        // to keep, and only an auditor or this assertion can tell.
        assertEquals(8, stores.versionsHeld("FileNet", "insurance!" + PATH));
        assertEquals(2, Department.INSURANCE.retainedVersions());
    }

    @Test
    @DisplayName("switch: six branches by hand, and the retention rule written in five")
    void theRulesLeak() throws Exception {
        String source = Files.readString(Path.of(SOURCE + "SwitchingFileManager.java"));
        String body = source.substring(source.indexOf("public final class"));

        int leaves = countOf(body, "case EVERNOTE ->")
                   + countOf(body, "case SHAREPOINT ->")
                   + countOf(body, "case FILENET ->");
        assertEquals(6, leaves, "branches, one per pair, written by hand");

        // Six branches store something; only five of them then trim.
        assertEquals(5, countOf(body, "kept.size() -"), "branches that apply a retention rule");
    }

    // ------------------------------------------- design 2: a class per pair

    @Test
    @DisplayName("class per pair: it works, and the class name has to state both axes")
    void classPerPairWorks() {
        FinanceEvernoteManager finance = new FinanceEvernoteManager(stores);
        InsuranceEvernoteManager insurance = new InsuranceEvernoteManager(stores);
        for (int i = 1; i <= 8; i++) {
            finance.save(PATH, "draft " + i);
            insurance.save(PATH, "draft " + i);
        }

        assertEquals(5, stores.versionsHeld("Evernote", "finance/" + PATH));
        assertEquals(2, stores.versionsHeld("Evernote", "insurance/" + PATH));

        for (Class<?> pair : Arrays.asList(FinanceEvernoteManager.class,
                InsuranceEvernoteManager.class, FinanceSharepointManager.class)) {
            String name = pair.getSimpleName();
            boolean namesADepartment = name.startsWith("Finance") || name.startsWith("Insurance");
            assertTrue(namesADepartment, name + " states its department");
            assertFalse(name.replace("Finance", "").replace("Insurance", "")
                    .equals("Manager"), name + " states its store as well");
        }
    }

    @Test
    @DisplayName("class per pair: one rule, written once per store; one store, once per rule")
    void theSameThingTwice() throws Exception {
        String financeEvernote = Files.readString(Path.of(SOURCE + "FinanceEvernoteManager.java"));
        String insuranceEvernote =
                Files.readString(Path.of(SOURCE + "InsuranceEvernoteManager.java"));
        String financeSharepoint =
                Files.readString(Path.of(SOURCE + "FinanceSharepointManager.java"));

        // Same store, two departments: the vendor call is duplicated.
        assertTrue(financeEvernote.contains("evernoteCreateNote"));
        assertTrue(insuranceEvernote.contains("evernoteCreateNote"));

        // Same department, two stores: the retention number is duplicated.
        assertTrue(financeEvernote.contains("KEEP = 5"));
        assertTrue(financeSharepoint.contains("KEEP = 5"));

        // 2 departments x 3 stores. Three of the six are written out in this package.
        assertEquals(6, Department.values().length * Store.values().length);
    }

    // ------------------------------------------- design 3: inherit the store

    @Test
    @DisplayName("inherit: the store is the superclass, so it cannot be changed at all")
    void theStoreIsWeldedOn() {
        EvernoteBoundFinanceManager manager = new EvernoteBoundFinanceManager(stores);
        for (int i = 1; i <= 8; i++) {
            manager.save(PATH, "draft " + i);
        }
        assertEquals(5, stores.versionsHeld("Evernote", "finance/" + PATH));

        // There is no setStore, and there cannot be: a superclass is chosen when the code is
        // compiled. When the Evernote contract ends, this object cannot follow the documents.
        assertTrue(Arrays.stream(EvernoteBoundFinanceManager.class.getMethods())
                .noneMatch(m -> m.getName().toLowerCase().contains("setstore")
                        || m.getName().toLowerCase().contains("setprovider")));

        // And the vendor is in the type itself, so every caller that names this class
        // names the vendor too.
        assertTrue(EvernoteBoundFinanceManager.class.getSimpleName().contains("Evernote"));
    }

    // ------------------------------------------- and what the pattern costs instead

    @Test
    @DisplayName("the bridge does what none of the three can: move a live manager to another store")
    void theBridgeAnswer() {
        FileProvider evernote = new EvernoteProvider();
        FileProvider sharepoint = new SharepointProvider();

        FileManager finance = new FinanceFileManager(evernote);
        finance.save(PATH, "draft 1");

        // The same object, a different store, decided while the program runs. None of the
        // three designs above can express this line.
        finance.setProvider(sharepoint);
        finance.save(PATH, "draft 2");

        assertEquals("draft 1", new String(evernote.read(evernote.open(PATH))));
        assertEquals("draft 2", new String(sharepoint.read(sharepoint.open(PATH))));
        assertEquals(5, finance.retainedVersions());
    }
}
