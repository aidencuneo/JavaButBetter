import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.function.Function;

public class LangUtil {
    public static void print(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { LangUtil.callMethod(LangUtil.getField(System.class, "out"), "print", Extensions.operAdd("", x)); }
    }
    public static void println(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { LangUtil.callMethod(LangUtil.getField(System.class, "out"), "print", Extensions.operAdd("", x)); }
        LangUtil.callMethod(LangUtil.getField(System.class, "out"), "println", "");
    }
    public static <T, R> R nullCheck(T value , Function < T , R > func) {
        return LangUtil.isTruthy(!Extensions.operEq(value, null)) ? (LangUtil.callMethod(func, "apply", value)) : (null);
    }
    public static double round(double v , int places) {
        return Extensions.operDiv(LangUtil.callMethod(Math.class, "round", Extensions.operMul(v, Math.pow(10, places))), Math.pow(10, places));
    }
    public static double round(double v) {
        return LangUtil.callMethod(Math.class, "round", v);
    }
    public static String roundstr(double v , int places) {
        return LangUtil.callMethod(String.class, "format", Extensions.operAdd(Extensions.operAdd("%.", places), "f"), v);
    }
    public static String roundstr(double v) {
        return LangUtil.callMethod(String.class, "format", "%f", v);
    }
    public static boolean isTruthy(boolean v) {
        return v;
    }
    public static boolean isTruthy(int v) {
        return !Extensions.operEq(v, 0);
    }
    public static boolean isTruthy(double v) {
        return !Extensions.operEq(v, 0);
    }
    public static boolean isTruthy(String v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static <T> boolean isTruthy(T [] v) {
        return LangUtil.getField(v, "length") > 0;
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
        return !Extensions.operEq(v, null);
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < Integer > asIterable(int n) {
        var lst = new ArrayList < Integer > ();
        for (int i = 0; i < n; ++i) {
            LangUtil.callMethod(lst, "add", i);
        }
        return lst;
    }
    public static <T> Iterable < T > asIterable(Iterable < T > v) {
        return v;
    }
    public static <T> Iterable < T > asIterable(Iterator < T > v) {
        return new IteratorToIterable < T > (v);
    }
    public static <TK, TV> Set < TK > asIterable(Map < TK , TV > v) {
        return LangUtil.callMethod(v, "keySet");
    }
    public static char [] asIterable(String s) {
        return LangUtil.callMethod(s, "toCharArray");
    }
    public static String slice(String s , int start , int end , int step) {
        start = indexConvert(start, LangUtil.callMethod(s, "length"));
        end = indexConvert(end, LangUtil.callMethod(s, "length"));
        if (LangUtil.isTruthy(Extensions.operEq(step, 1))) {
            return LangUtil.callMethod(s, "substring", start, end);
        }
        var newStr = "";
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            newStr += LangUtil.callMethod(s, "charAt", i);
        }
        return newStr;
    }
    public static String slice(String s , Null start , Null end , int step) {
        return slice(s, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(s, "length"), 1)), LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(s, "length")) : (Extensions.operSub(Extensions.operUnarySub(LangUtil.callMethod(s, "length")), 1)), step);
    }
    public static String slice(String s , Null start , int end , int step) {
        return slice(s, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(s, "length"), 1)), end, step);
    }
    public static String slice(String s , int start , Null end , int step) {
        return slice(s, start, LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(s, "length")) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > v , int start , int end , int step) {
        start = indexConvert(start, LangUtil.callMethod(v, "size"));
        end = indexConvert(end, LangUtil.callMethod(v, "size"));
        return new ArrayList < > (LangUtil.callMethod(v, "subList", start, end));
    }
    public static <T> ArrayList < T > slice(ArrayList < T > v , Null start , Null end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(v, "size"), 1)), LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(v, "size")) : (Extensions.operSub(Extensions.operUnarySub(LangUtil.callMethod(v, "size")), 1)), step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > v , Null start , int end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(v, "size"), 1)), end, step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > v , int start , Null end , int step) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(v, "size")) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> List < T > slice(List < T > v , int start , int end , int step) {
        start = indexConvert(start, LangUtil.callMethod(v, "size"));
        end = indexConvert(end, LangUtil.callMethod(v, "size"));
        var lst = new ArrayList < T > ();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(i, ", "), start), ", "), end), ", "), step));
            LangUtil.callMethod(lst, "add", LangUtil.callMethod(v, "get", i));
        }
        return lst;
    }
    public static <T> List < T > slice(List < T > v , Null start , Null end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(v, "size"), 1)), LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(v, "size")) : (Extensions.operSub(Extensions.operUnarySub(LangUtil.callMethod(v, "size")), 1)), step);
    }
    public static <T> List < T > slice(List < T > v , Null start , int end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.callMethod(v, "size"), 1)), end, step);
    }
    public static <T> List < T > slice(List < T > v , int start , Null end , int step) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (LangUtil.callMethod(v, "size")) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> List < T > slice(T [] v , int start , int end , int step) {
        start = indexConvert(start, LangUtil.getField(v, "length"));
        end = indexConvert(end, LangUtil.getField(v, "length"));
        var lst = new ArrayList < T > ();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            LangUtil.callMethod(lst, "add", Extensions.operGetIndex(v, i));
        }
        return lst;
    }
    public static <T> List < T > slice(T [] v , Null start , Null end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.getField(v, "length"), 1)), LangUtil.isTruthy(step > 0) ? (LangUtil.getField(v, "length")) : (Extensions.operSub(Extensions.operUnarySub(LangUtil.getField(v, "length")), 1)), step);
    }
    public static <T> List < T > slice(T [] v , Null start , int end , int step) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (Extensions.operSub(LangUtil.getField(v, "length"), 1)), end, step);
    }
    public static <T> List < T > slice(T [] v , int start , Null end , int step) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (LangUtil.getField(v, "length")) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> T getField(Object obj , String name) {
        try {
            var c = LangUtil.callMethod(obj, "getClass");
            while (LangUtil.isTruthy(c)) {
                try {
                    var f = LangUtil.callMethod(c, "getDeclaredField", name);
                    LangUtil.callMethod(f, "setAccessible", true);
                    return LangUtil.callMethod((T)f, "get", obj);
                }
                catch (NoSuchFieldException e) {
                    c = LangUtil.callMethod(c, "getSuperclass");
                }
            }
            throw new RuntimeException("Field not found: " + name);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T callMethod(Object obj , String methodName , Object ... args) {
        try {
            var c = LangUtil.callMethod(obj, "getClass");
            var argTypes = new Class<?>[args.length];
            for (var i : LangUtil.asIterable(LangUtil.range(0, LangUtil.getField(args, "length"), null))) {
                argTypes[i] = args[i] == null ? Object.class : args[i].getClass();
            }
            while (LangUtil.isTruthy(c)) {
                try {
                    var m = LangUtil.callMethod(c, "getDeclaredMethod", methodName, argTypes);
                    LangUtil.callMethod(m, "setAccessible", true);
                    return LangUtil.callMethod((T)m, "invoke", obj, args);
                }
                catch (NoSuchMethodException e) {
                    c = LangUtil.callMethod(c, "getSuperclass");
                }
            }
            throw new RuntimeException("Method not found: " + methodName);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static int indexConvert(int index , int size) {
        if (LangUtil.isTruthy(index < 0)) { index += size; }
        return index;
    }
    public static IntRange range(int start , int stop , int step) {
        return new IntRange(start, stop, step);
    }
    public static IntRange range(int start , Null stop , Null step) {
        return range(start, LangUtil.getField(Integer.class, "MAX_VALUE"), 1);
    }
    public static IntRange range(int start , Null stop , int step) {
        return range(start, LangUtil.isTruthy(step > 0) ? (LangUtil.getField(Integer.class, "MAX_VALUE")) : (LangUtil.getField(Integer.class, "MIN_VALUE")), step);
    }
    public static IntRange range(int start , int stop , Null step) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (Extensions.operUnarySub(1)));
    }
    public static LongRange range(long start , long stop , long step) {
        return new LongRange(start, stop, step);
    }
    public static LongRange range(long start , Null stop , Null step) {
        return range(start, LangUtil.getField(Long.class, "MAX_VALUE"), 1);
    }
    public static LongRange range(long start , Null stop , long step) {
        return range(start, LangUtil.isTruthy(step > 0) ? (LangUtil.getField(Long.class, "MAX_VALUE")) : (LangUtil.getField(Long.class, "MIN_VALUE")), step);
    }
    public static LongRange range(long start , long stop , Null step) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (Extensions.operUnarySub(1)));
    }
    public static DoubleRange range(double start , double stop , double step) {
        return new DoubleRange(start, stop, step);
    }
    public static DoubleRange range(double start , Null stop , Null step) {
        return range(start, LangUtil.getField(Double.class, "MAX_VALUE"), 1);
    }
    public static DoubleRange range(double start , Null stop , double step) {
        return range(start, LangUtil.isTruthy(step > 0) ? (LangUtil.getField(Double.class, "MAX_VALUE")) : (LangUtil.getField(Double.class, "MIN_VALUE")), step);
    }
    public static DoubleRange range(double start , double stop , Null step) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (Extensions.operUnarySub(1)));
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

