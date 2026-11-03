package dev.kaldiroglu.dp.creational.factoryMethod;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/** Reads the package's classes and sources, so that tests count the code. */
final class SourceFacts {

    private static final Path ROOT = Path.of("src/main/java/dev/kaldiroglu/dp/creational/factoryMethod");

    private SourceFacts() {
    }

    /** The source of a file under factoryMethod, without comments. */
    static String sourceOf(String relativePath) throws IOException {
        return Files.readString(ROOT.resolve(relativePath))
                .replaceAll("(?s)/\\*.*?\\*/", "")
                .replaceAll("//[^\\n]*", "");
    }

    /** The top-level classes in the same package as the given class. */
    static List<Class<?>> classesBeside(Class<?> anchor)
            throws IOException, URISyntaxException, ClassNotFoundException {
        Path classes = Path.of(anchor.getProtectionDomain().getCodeSource().getLocation().toURI());
        Path folder = classes.resolve(anchor.getPackageName().replace('.', '/'));
        List<Class<?>> found = new ArrayList<>();
        try (Stream<Path> files = Files.list(folder)) {
            for (Path file : (Iterable<Path>) files::iterator) {
                String name = file.getFileName().toString();
                if (name.endsWith(".class") && !name.contains("$")) {
                    found.add(Class.forName(anchor.getPackageName() + "." + name.replace(".class", "")));
                }
            }
        }
        found.sort(Comparator.comparing(Class::getName));
        return found;
    }

    /** The concrete classes beside the given type that are assignable to it, the type included. */
    static List<Class<?>> concreteSubtypes(Class<?> type)
            throws IOException, URISyntaxException, ClassNotFoundException {
        return classesBeside(type).stream()
                .filter(type::isAssignableFrom)
                .filter(c -> !c.isInterface() && !Modifier.isAbstract(c.getModifiers()))
                .toList();
    }
}
