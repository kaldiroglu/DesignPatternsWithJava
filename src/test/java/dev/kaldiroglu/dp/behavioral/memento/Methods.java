package dev.kaldiroglu.dp.behavioral.memento;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/** Reads the public methods of a class. */
public final class Methods {

    private Methods() {
    }

    /** The names of the public methods a class declares that start with "set". */
    public static List<String> publicSettersOf(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .filter(name -> name.startsWith("set"))
                .sorted()
                .toList();
    }
}
