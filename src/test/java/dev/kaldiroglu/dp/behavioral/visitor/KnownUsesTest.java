package dev.kaldiroglu.dp.behavioral.visitor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.nio.file.FileVisitor;
import java.nio.file.SimpleFileVisitor;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The rows of the Visitor deck's JDK known-uses table, checked against the running JDK. */
class KnownUsesTest {

    private static List<String> methodNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods()).map(Method::getName).toList();
    }

    @Test
    @DisplayName("FileVisitor has preVisitDirectory and postVisitDirectory, and SimpleFileVisitor implements it")
    void fileVisitor() {
        assertTrue(methodNames(FileVisitor.class).containsAll(
                List.of("preVisitDirectory", "visitFile", "visitFileFailed", "postVisitDirectory")));
        assertTrue(FileVisitor.class.isAssignableFrom(SimpleFileVisitor.class));
    }

    @Test
    @DisplayName("ElementVisitor visits classes, methods and fields")
    void elementVisitor() {
        assertTrue(methodNames(javax.lang.model.element.ElementVisitor.class).containsAll(
                List.of("visitType", "visitExecutable", "visitVariable")));
    }

    @Test
    @DisplayName("TreeVisitor visits the syntax tree, and TreeScanner implements it")
    void treeVisitor() throws Exception {
        Class<?> visitor = Class.forName("com.sun.source.tree.TreeVisitor");
        Class<?> scanner = Class.forName("com.sun.source.util.TreeScanner");
        assertTrue(visitor.isAssignableFrom(scanner));
        assertTrue(methodNames(visitor).contains("visitClass"));
    }

    @Test
    @DisplayName("the class-file API gives elements to switch over: ClassElement is sealed and ClassModel holds them")
    void classFileApi() {
        assertTrue(java.lang.classfile.ClassElement.class.isSealed());
        assertTrue(Iterable.class.isAssignableFrom(java.lang.classfile.ClassModel.class));
        assertFalse(methodNames(java.lang.classfile.ClassModel.class).contains("accept"));
    }
}
