package dev.ninesliced.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ReflectionHelper {

    private static final Logger LOGGER = Logger.getLogger(ReflectionHelper.class.getName());

    @Nullable
    public static Field getField(@Nonnull Class<?> clazz, @Nonnull String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            LOGGER.warning("Field not found: " + clazz.getName() + "." + fieldName);
            return null;
        }
    }

    @Nullable
    public static Object getFieldValue(@Nonnull Object instance, @Nonnull String fieldName) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            if (field != null) {
                return field.get(instance);
            }
        } catch (IllegalAccessException e) {
            LOGGER.warning("Cannot access field: " + fieldName);
        }
        return null;
    }

    public static boolean setFieldValue(@Nonnull Object instance, @Nonnull String fieldName, @Nullable Object value) {
        try {
            Field field = getField(instance.getClass(), fieldName);
            if (field != null) {
                field.set(instance, value);
                return true;
            }
        } catch (IllegalAccessException e) {
            LOGGER.warning("Cannot access field for setting: " + fieldName);
        }
        return false;
    }

    @Nullable
    public static Method getMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            LOGGER.warning("Method not found: " + clazz.getName() + "." + methodName);
            return null;
        }
    }

    @Nullable
    public static Object invokeMethod(@Nonnull Object instance, @Nonnull String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method method = getMethod(instance.getClass(), methodName, parameterTypes);
            if (method != null) {
                return method.invoke(instance, args);
            }
        } catch (Exception e) {
            LOGGER.warning("Cannot invoke method: " + methodName + " - " + e.getMessage());
        }
        return null;
    }

    @Nullable
    public static Field getFieldRecursive(@Nonnull Class<?> clazz, @Nonnull String fieldName) {
        Class<?> current = clazz;
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    @Nullable
    public static Object getFieldValueRecursive(@Nonnull Object instance, @Nonnull String fieldName) {
        try {
            Field field = getFieldRecursive(instance.getClass(), fieldName);
            if (field != null) {
                return field.get(instance);
            }
        } catch (IllegalAccessException e) {
            LOGGER.warning("Cannot access field recursively: " + fieldName);
        }
        return null;
    }

    public static boolean setFieldValueRecursive(@Nonnull Object instance, @Nonnull String fieldName, @Nullable Object value) {
        try {
            Field field = getFieldRecursive(instance.getClass(), fieldName);
            if (field != null) {
                field.set(instance, value);
                return true;
            }
        } catch (IllegalAccessException e) {
            LOGGER.warning("Cannot access field recursively for setting: " + fieldName);
        }
        return false;
    }
}
