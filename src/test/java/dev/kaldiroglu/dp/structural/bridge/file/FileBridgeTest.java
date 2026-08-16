package dev.kaldiroglu.dp.structural.bridge.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void mPlusNNotMTimesN() throws Exception {
        // Counted from the package, not from two lists written here. Listing the classes and
        // then asserting 2 + 3 == 5 would prove something about integers, and would go on
        // passing the day a fourth store is added and the slide still says five.
        List<Class<?>> types = typesIn(FileManager.class.getPackageName());

        long departments = types.stream()
                .filter(FileManager.class::isAssignableFrom)
                .filter(t -> t != FileManager.class)
                .count();
        long stores = types.stream()
                .filter(FileProvider.class::isAssignableFrom)
                .filter(t -> !Modifier.isAbstract(t.getModifiers()))
                .count();

        assertEquals(2, departments, "refined abstractions");
        assertEquals(3, stores, "concrete implementors");
        assertEquals(5, departments + stores, "m + n, the classes that carry the two axes");
        assertEquals(6, departments * stores, "m x n, the grid a class-per-pair design writes");

        assertEquals(FileProvider.class,
                java.util.Arrays.stream(FileManager.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("provider"))
                        .findFirst().orElseThrow().getType());
    }

    /**
     * Every top-level class file in a package, loaded.
     * <p>
     * All roots are scanned, not just the first. This package name exists under both
     * {@code target/classes} and {@code target/test-classes}, and {@code getResource}
     * returns whichever comes first on the classpath — which is how this method silently
     * found nothing but tests the first time it was written.
     */
    private static List<Class<?>> typesIn(String packageName) throws Exception {
        List<URL> roots = java.util.Collections.list(
                FileBridgeTest.class.getClassLoader().getResources(packageName.replace('.', '/')));
        assertFalse(roots.isEmpty(), "package not on the test classpath: " + packageName);

        List<Class<?>> types = new ArrayList<>();
        for (URL root : roots) {
            try (Stream<Path> files = Files.list(Path.of(root.toURI()))) {
                for (Path file : files.sorted().toList()) {
                    String name = file.getFileName().toString();
                    if (name.endsWith(".class") && !name.contains("$")) {
                        Class<?> type = Class.forName(
                                packageName + '.' + name.substring(0, name.length() - 6));
                        if (!types.contains(type)) {
                            types.add(type);
                        }
                    }
                }
            }
        }
        return types;
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
