package dev.kaldiroglu.dp.behavioral.mediator;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Reads which types a class holds references to: its field types, and the element types of its lists. */
public final class Fields {

    private Fields() {
    }

    /** The types a class's instance fields refer to, including the type inside a generic field. */
    public static List<Class<?>> heldBy(Class<?> type) {
        List<Class<?>> held = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                continue;
            }
            held.add(field.getType());
            Type generic = field.getGenericType();
            if (generic instanceof ParameterizedType parameterized) {
                Arrays.stream(parameterized.getActualTypeArguments())
                        .filter(t -> t instanceof Class<?>)
                        .forEach(t -> held.add((Class<?>) t));
            }
        }
        return held;
    }

    /** True if the class holds a reference to any of the given types. */
    public static boolean holdsAny(Class<?> type, List<Class<?>> others) {
        return heldBy(type).stream().anyMatch(held -> others.stream().anyMatch(o -> o.isAssignableFrom(held)));
    }
}
