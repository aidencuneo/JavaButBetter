import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.function.Function;

public class LangUtil {
    static {
        Dynamic.registerAll(Extensions.class);
    }
    public static void print(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { LangUtil.callMethod(LangUtil.getField(System.class, "out"), "print", new Object[] {Dynamic.call("operAdd", "", x)}); }
    }
    public static void println(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { LangUtil.callMethod(LangUtil.getField(System.class, "out"), "print", new Object[] {Dynamic.call("operAdd", "", x)}); }
        LangUtil.callMethod(LangUtil.getField(System.class, "out"), "println", new Object[] {""});
    }
    public static <T, R> R nullCheck(T value , Function < T , R > func) {
        return LangUtil.isTruthy(!((boolean) Dynamic.call("operEq", value, null))) ? (LangUtil.callMethod(func, "apply", new Object[] {value})) : (null);
    }
    public static double round(double v , int places) {
        return Math.round(v * Math.pow(10, places)) / Math.pow(10, places);
    }
    public static double round(double v) {
        return Math.round(v);
    }
    public static String roundstr(double v , int places) {
        return LangUtil.callMethod(String.class, "format", new Object[] {Dynamic.call("operAdd", Dynamic.call("operAdd", "%.", places), "f"), v});
    }
    public static String roundstr(double v) {
        return LangUtil.callMethod(String.class, "format", new Object[] {"%f", v});
    }
    public static boolean isTruthy(boolean v) {
        return v;
    }
    public static boolean isTruthy(int v) {
        return !((boolean) Dynamic.call("operEq", v, 0));
    }
    public static boolean isTruthy(double v) {
        return !((boolean) Dynamic.call("operEq", v, 0));
    }
    public static boolean isTruthy(String v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static <T> boolean isTruthy(T [] v) {
        return v.length > 0;
    }
    public static boolean isTruthy(List v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static boolean isTruthy(Object v) {
        if (v instanceof Boolean x) return x;
        if (v instanceof Integer x) return x != 0;
        if (v instanceof Double x) return x != 0;
        if (v instanceof String x) return x == null ? false : !x.isEmpty();
        if (v instanceof List x) return x == null ? false : !x.isEmpty();
        return v != null;
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < Integer > asIterable(int n) {
        var lst = new ArrayList<Integer>();
        for (int i = 0; i < n; ++i) {
            LangUtil.callMethod(lst, "add", new Object[] {i});
        }
        return lst;
    }
    public static <T> Iterable < T > asIterable(Iterable < T > v) {
        return v;
    }
    public static <T> Iterable < T > asIterable(Iterator < T > v) {
        return new IteratorToIterable<T>(v);
    }
    public static <TK, TV> Set < TK > asIterable(Map < TK , TV > v) {
        return LangUtil.callMethod(v, "keySet");
    }
    public static char [] asIterable(String s) {
        return LangUtil.callMethod(s, "toCharArray");
    }
    public static <T> T getField(Object obj , String name) {
        
    try {
        Class<?> c;
        Object target;

        if (obj instanceof Class<?> clazz) {
                        c = clazz;
            target = null;
        } else {
            c = obj.getClass();
            target = obj;
        }

        while (c != null) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return (T) f.get(target);
            } catch (NoSuchFieldException e) {
                                c = c.getSuperclass();
            }
        }

        throw new RuntimeException("Field not found: " + name);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    public static <T> T callMethod(Object obj , String methodName , Object ... args) {
        
    try {
        Class<?> c;
        Object target;

        if (obj instanceof Class<?> clazz) {
                        c = clazz;
            target = null;
        } else {
            c = obj.getClass();
            target = obj;
        }

                Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
            argTypes[i] = args[i] == null ? Object.class : args[i].getClass();

                                                                
        System.out.println("argTypes: " + Arrays.toString(argTypes));

        while (c != null) {
            try {
                                Method m = findCompatibleMethod(c, methodName, argTypes);

                                try {
                    m.setAccessible(true);
                } catch (InaccessibleObjectException e) {
                                    }

                return (T) m.invoke(target, args);
            } catch (NoSuchMethodException e) {
                c = c.getSuperclass();
            }
        }

        throw new RuntimeException("Method not found: " + methodName);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    
static Method findCompatibleMethod(Class<?> c, String name, Class<?>[] argTypes) throws NoSuchMethodException {
    for (Method m : c.getDeclaredMethods()) {
        if (!m.getName().equals(name)) continue;
        Class<?>[] params = m.getParameterTypes();
        if (params.length != argTypes.length) continue;
        boolean ok = true;
        for (int i = 0; i < params.length; i++) {
            if (!isCompatible(params[i], argTypes[i])) {
                ok = false;
                break;
            }
        }
        if (ok) return m;
    }
    throw new NoSuchMethodException();
}
    
static boolean isCompatible(Class<?> param, Class<?> given) {
    if (param.isAssignableFrom(given)) return true;
    if (param.isPrimitive()) {
        if (param == int.class && given == Integer.class) return true;
        if (param == long.class && given == Long.class) return true;
        if (param == double.class && given == Double.class) return true;
        if (param == float.class && given == Float.class) return true;
        if (param == boolean.class && given == Boolean.class) return true;
        if (param == char.class && given == Character.class) return true;
        if (param == byte.class && given == Byte.class) return true;
        if (param == short.class && given == Short.class) return true;
    }
        if (given.isPrimitive()) return isCompatible(param, primitiveToWrapper(given));
    return false;
}
    
static Class<?> primitiveToWrapper(Class<?> c) {
    if (c == int.class) return Integer.class;
    if (c == long.class) return Long.class;
    if (c == double.class) return Double.class;
    if (c == float.class) return Float.class;
    if (c == boolean.class) return Boolean.class;
    if (c == char.class) return Character.class;
    if (c == byte.class) return Byte.class;
    if (c == short.class) return Short.class;
    return c;
}
    public static int indexConvert(int index , int size) {
        if (LangUtil.isTruthy(Dynamic.call("operLt", index, 0))) { index += size; }
        return index;
    }
    
static class IntRange implements Iterator<Integer> {
    public int start;
    public int stop;
    public int step;
    public int current;

    public IntRange(Integer start, Integer stop, Integer step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (step > 0 && stop > start)
            return current < stop;
        if (step < 0 && stop < start)
            return current > stop;
        return step == 0;
    }

    @Override
    public Integer next() {
        if (!hasNext())
            throw new NoSuchElementException();

        int value = current;
        current += step;

        return value;
    }
}
    
static class LongRange implements Iterator<Long> {
    public long start;
    public long stop;
    public long step;
    public long current;

    public LongRange(Long start, Long stop, Long step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (step > 0 && stop > start)
            return current < stop;
        if (step < 0 && stop < start)
            return current > stop;
        return step == 0;
    }

    @Override
    public Long next() {
        if (!hasNext())
            throw new NoSuchElementException();

        long value = current;
        current += step;

        return value;
    }
}
    
static class DoubleRange implements Iterator<Double> {
    public double start;
    public double stop;
    public double step;
    public double current;

    public DoubleRange(Double start, Double stop, Double step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (step > 0 && stop > start)
            return current < stop;
        if (step < 0 && stop < start)
            return current > stop;
        return step == 0;
    }

    @Override
    public Double next() {
        if (!hasNext())
            throw new NoSuchElementException();

        double value = current;
        current += step;

        return value;
    }
}
    
static class IteratorToIterable<T> implements Iterable<T> {
    private final Iterator<T> iterator;

    public IteratorToIterable(Iterator<T> iterator) {
        if (iterator == null)
            throw new IllegalArgumentException();
        this.iterator = iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }
}
}
class Null {
    {
        
    }
}

