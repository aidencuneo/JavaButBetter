import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.function.Function;

public class LangUtil {
    public static void print(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print(String.valueOf(x)); }
    }
    public static void println(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print(String.valueOf(x)); }
        System.out.println("");
    }
    public static <T, R> R nullCheck(T value , Function < T , R > func) {
        return LangUtil.isTruthy(!((boolean) Extensions.operEq(value, null))) ? (func.apply(value)) : (null);
    }
    public static double round(double v , int places) {
        return Math.round(v * Math.pow(10, places)) / Math.pow(10, places);
    }
    public static double round(double v) {
        return Math.round(v);
    }
    public static String roundstr(double v , int places) {
        return String.format(Extensions.operAdd(Extensions.operAdd("%.", places), "f"), v);
    }
    public static String roundstr(double v) {
        return String.format("%f", v);
    }
    public static boolean isTruthy(boolean v) {
        return v;
    }
    public static boolean isTruthy(int v) {
        return !((boolean) Extensions.operEq(v, 0));
    }
    public static boolean isTruthy(double v) {
        return !((boolean) Extensions.operEq(v, 0));
    }
    public static boolean isTruthy(String v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static <T> boolean isTruthy(T [] v) {
        return v.length > 0;
    }
    public static <T> boolean isTruthy(List < T > v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static boolean isTruthy(Object v) {
        
        if (v instanceof Boolean x) return isTruthy(x);
        if (v instanceof Integer x) return isTruthy(x);
        if (v instanceof Double x) return isTruthy(x);
        if (v instanceof String x) return isTruthy(x);
        if (v instanceof List x) return isTruthy(x);
        return v != null;
    
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < Integer > asIterable(int n) {
        var lst = new ArrayList<Integer>();
        for (int i = 0; i < n; ++i) {
            lst.add(i);
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
        return v.keySet();
    }
    public static char [] asIterable(String s) {
        return s.toCharArray();
    }
    public static int indexConvert(int index , int size) {
        if (LangUtil.isTruthy(Extensions.operLt(index, 0))) { index += size; }
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

