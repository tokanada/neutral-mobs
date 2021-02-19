package net.orandja.mcutils;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class QuickUtils {

    public interface noCatchIntf<T> {
        T accept() throws Exception;
    }
    public static <T> T noCatch(noCatchIntf<T> noCatchM) {
        return noCatch(null, noCatchM);
    }

    public static <T> T noCatch(T faultValue, noCatchIntf<T> noCatchM) {
        try{
            return noCatchM.accept();
        } catch(Exception ignored) {
            return faultValue;
        }
    }

    public static <T> T castOrNull(Object object, Class<T> clazz) {
        return clazz.isInstance(object) ? (T) object : null;
    }

    public static Field quickField(Class<?> clazz, Predicate<Field> predicate) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(predicate).map(field -> {field.setAccessible(true); return field; } ).findFirst().get();
    }

    public static Field quickField(Class<?> clazz, Class<?> clazzSearched) {
        return quickField(clazz, field -> field.getType().equals(clazzSearched));
    }

    public static Method quickStaticMethod(Class<?> clazz, Predicate<Method> predicate) {
        return Arrays.stream(clazz.getDeclaredMethods()).filter(method -> Modifier.isStatic(method.getModifiers())).filter(predicate).map(method -> { method.setAccessible(true); return method; }).findFirst().get();
    }
    public static Method quickStaticMethod(Class<?> clazz, Class<?>... argumentClazz) {
        return quickMethod(clazz, true, argumentClazz);
    }
    public static Method quickMethod(Class<?> clazz, Class<?>... argumentClazz) {
        return quickMethod(clazz, false, argumentClazz);
    }

    private static Method quickMethod(Class<?> clazz, boolean statik, Class<?>... argumentClazz) {
        Predicate<Method> predicate = method -> {
            Parameter[] types = method.getParameters();
            if(types.length != argumentClazz.length) {
                return false;
            }

            for(int i = 0; i < types.length; i++) {
                if(!types[i].getType().equals(argumentClazz[i])) {
                    return false;
                }
            }

            return true;
        };
        return statik ? quickStaticMethod(clazz, predicate) : quickMethod(clazz, predicate);
    }

    public static Method quickMethod(Class<?> clazz, Predicate<Method> predicate) {
        return Arrays.stream(clazz.getDeclaredMethods()).filter(predicate).map(method -> { method.setAccessible(true); return method; }).findFirst().get();
    }

    public static <T> T quickGet(Field field, Object source) {
        return (T) noCatch(() -> field.get(source));
    }

    public static <T> T quickInvoke(Method method, Object source, Object... parameters) {
        return (T) noCatch(() -> method.invoke(source, parameters));
    }

    public static <T> T create(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public static <T> T create(T object, Consumer<T>... consumer) {
        for (Consumer<T> tConsumer : consumer) {
            tConsumer.accept(object);
        }
        return object;
    }
}
