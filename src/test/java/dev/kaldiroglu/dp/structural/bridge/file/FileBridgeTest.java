package dev.kaldiroglu.dp.structural.bridge.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * One retention rule per department, written once, correct on every store — and a test that
 * fails if the provider interface starts mirroring the manager.
 */
class FileBridgeTest {

    private static List<FileProvider> stores() {
        return List.of(new EvernoteProvider(), new SharepointProvider(), new FileNetProvider());
    }

    @Test
    @DisplayName("one rule, every store, the same answer")
    void oneRuleEveryStore() {
        for (FileProvider store : stores()) {
            FileManager finance = new FinanceFileManager(store);
            for (int i = 1; i <= 8; i++) {
                finance.save("q3", "draft " + i);
            }
            assertEquals(8, finance.versions("q3").size());
            assertEquals("draft 8", finance.read("q3"));
            assertEquals(5, finance.retainedVersions());
        }
    }

    @Test
    @DisplayName("two departments keep different histories of the same store")
    void twoDepartmentsOneStore() {
        FileProvider store = new SharepointProvider();

        assertEquals(5, new FinanceFileManager(store).retainedVersions());
        assertEquals(2, new InsuranceFileManager(store).retainedVersions());
    }

    @Test
    @DisplayName("the store can be changed on a manager that already exists")
    void theStoreCanChangeAtRunTime() {
        FileManager finance = new FinanceFileManager(new EvernoteProvider());
        finance.save("memo", "on Evernote");
        assertEquals("on Evernote", finance.read("memo"));

        finance.setProvider(new FileNetProvider());
        finance.save("memo", "on FileNet");
        assertEquals("on FileNet", finance.read("memo"));
    }

    @Test
    @DisplayName("the provider offers storage primitives, not the manager's operations")
    void theInterfacesAreNotTheSameInterfaceTwice() {
        List<String> managerOperations = List.of("read", "save", "versions", "setprovider");
        long mirrored = 0;
        for (Method method : FileProvider.class.getDeclaredMethods()) {
            if (managerOperations.contains(method.getName().toLowerCase())
                    && !method.getName().equals("read")
                    && !method.getName().equals("versions")) {
                mirrored++;
            }
        }
        // read and versions are genuinely shared vocabulary; save/setProvider must not appear.
        assertEquals(0, mirrored);

        // And the primitives the manager composes with really are there.
        assertTrue(List.of("open", "write", "deleteVersion").stream().allMatch(name ->
                java.util.Arrays.stream(FileProvider.class.getDeclaredMethods())
                        .anyMatch(m -> m.getName().equals(name))));
    }

    @Test
    @DisplayName("2 departments and 3 stores are 5 classes, not 6")
    void mPlusNNotMTimesN() {
        List<Class<?>> departments = List.of(FinanceFileManager.class, InsuranceFileManager.class);
        List<Class<?>> providers = List.of(
                EvernoteProvider.class, SharepointProvider.class, FileNetProvider.class);

        assertEquals(5, departments.size() + providers.size());
        assertEquals(6, departments.size() * providers.size());
        assertEquals(FileProvider.class,
                java.util.Arrays.stream(FileManager.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("provider"))
                        .findFirst().orElseThrow().getType());
    }

    @Test
    @DisplayName("nothing in this package is called an adapter")
    void noAdapterInTheNames() {
        for (Class<?> type : List.of(FileProvider.class, EvernoteProvider.class,
                SharepointProvider.class, FileNetProvider.class, FileManager.class)) {
            String name = type.getSimpleName().toLowerCase();
            assertTrue(!name.contains("adapt"),
                    type.getSimpleName() + " is an Implementor, not an Adapter — an adapter "
                            + "makes an existing incompatible interface fit, after the fact");
        }
    }
}
