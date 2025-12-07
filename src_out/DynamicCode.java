import java.io.*;
import java.util.*;

public class DynamicCode {
    public static String get() {
        return """
import java.lang.reflect.Method;
import java.util.*;

public class Dynamic {
    public static void main(String[] args) {
        try {
            register("operAdd", Dynamic.class.getMethod("operAdd", int.class, int.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Object a = 1;
        Object b = 2;
        var res = call("operAdd", a, 2);
        System.out.println(res);
    }

    public static int operAdd(int a, int b) {
        return a + b;
    }

    static final Map<Signature, Method> registry = new HashMap<>();

    public record Signature(String name, Class<?>[] argTypes) {}

    public static void register(String name, Method m) {
        registry.put(new Signature(name, m.getParameterTypes()), m);
    }

    public static Object call(String name, Object... args) {
        Class<?>[] givenTypes = Arrays.stream(args)
            .map(a -> a == null ? Object.class : a.getClass())
            .toArray(Class<?>[]::new);

        // Search for the best matching overload
        for (var entry : registry.entrySet()) {
            Signature sig = entry.getKey();
            if (!sig.name.equals(name)) continue;

            Class<?>[] paramTypes = sig.argTypes;
            if (paramTypes.length != givenTypes.length) continue;

            boolean match = true;
            for (int i = 0; i < paramTypes.length; i++) {
                if (!isCompatible(paramTypes[i], givenTypes[i])) {
                    match = false;
                    break;
                }
            }

            if (match) {
                try {
                    return entry.getValue().invoke(null, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException("No overload for " + name + " matching argument types " + Arrays.toString(givenTypes));
    }

    static boolean isCompatible(Class<?> param, Class<?> given) {
        if (param.isAssignableFrom(given)) return true;

        // primitive vs wrapper
        if (param == int.class && given == Integer.class) return true;
        if (param == long.class && given == Long.class) return true;
        if (param == double.class && given == Double.class) return true;
        if (param == float.class && given == Float.class) return true;

        // add more if desired (char, short, byte)
        return false;
    }

}
    """.trim();
    }
}

