import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.function.*;
import java.util.stream.*;

public class LangUtil {
    public static void print(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print(String.valueOf(x)); }
    }
    public static void println(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print(String.valueOf(x)); }
        System.out.println("");
    }
    public static void exit(int code) {
        System.exit(code);
    }
    public static void exit() {
        System.exit(0);
    }
    public static <T, R> R nullCheck(T value, Function<T, R> func) {
        return LangUtil.isTruthy(!((boolean) Extensions.operEq(value, null))) ? (func.apply(value)) : (null);
    }
    public static <T> void statNullCheck(T value, Consumer<T> cons) {
        if (LangUtil.isTruthy(!((boolean) Extensions.operEq(value, null)))) { cons.accept(value); }
    }
    public static double round(double v, int places) {
        return Math.round(v * Math.pow(10, places)) / Math.pow(10, places);
    }
    public static double round(double v) {
        return Math.round(v);
    }
    public static String roundstr(double v, int places) {
        return String.format(Extensions.operAdd(Extensions.operAdd("%.", places), "f"), v);
    }
    public static String roundstr(double v) {
        return String.format("%f", v);
    }
    @SafeVarargs
    public static <T> List<T> listOf(T ... args) {
        return new ArrayList<>(Arrays.asList(args));
    }
    public static <T> Stream<T> arrstream(T[] v) {
        return Arrays.stream(v);
    }
    public static <T> Stream<T> arrstream(Iterable<T> v) {
        return StreamSupport.stream(v.spliterator(), false);
    }
    public static <T> Stream<T> arrstream(Iterator<T> v) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(v, Spliterator.ORDERED), false);
    }
    public static <TK, TV> Stream<Map.Entry<TK, TV>> arrstream(Map<TK, TV> v) {
        return arrstream(v.entrySet());
    }
    public static <T extends Comparable<? super T>> T min(T[] v) {
        return arrstream(v).min(Comparator.naturalOrder()).orElse(null);
    }
    public static <T extends Comparable<? super T>> T min(Iterable<T> v) {
        return arrstream(v).min(Comparator.naturalOrder()).orElse(null);
    }
    public static <TK, TV extends Comparable<? super TV>> TK min(Map<TK, TV> v) {
        return (v.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null));
    }
    public static <T extends Comparable<? super T>> T max(T[] v) {
        return arrstream(v).max(Comparator.naturalOrder()).orElse(null);
    }
    public static <T extends Comparable<? super T>> T max(Iterable<T> v) {
        return arrstream(v).max(Comparator.naturalOrder()).orElse(null);
    }
    public static <TK, TV extends Comparable<? super TV>> TK max(Map<TK, TV> v) {
        return (v.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null));
    }
    public static int sum(int[] v) {
        return Arrays.stream(v).sum();
    }
    public static int sum(Iterable<Integer> v, int ... ignore) {
        return arrstream(v).mapToInt(Integer::intValue).sum();
    }
    public static int sum(Iterator<Integer> v, int ... ignore) {
        return arrstream(v).mapToInt(Integer::intValue).sum();
    }
    public static long sum(long[] v) {
        return Arrays.stream(v).sum();
    }
    public static long sum(Iterable<Long> v, long ... ignore) {
        return arrstream(v).mapToLong(Long::longValue).sum();
    }
    public static long sum(Iterator<Long> v, long ... ignore) {
        return arrstream(v).mapToLong(Long::longValue).sum();
    }
    public static double sum(double[] v) {
        return Arrays.stream(v).sum();
    }
    public static double sum(Iterable<Double> v, double ... ignore) {
        return arrstream(v).mapToDouble(Double::doubleValue).sum();
    }
    public static double sum(Iterator<Double> v, double ... ignore) {
        return arrstream(v).mapToDouble(Double::doubleValue).sum();
    }
    public static String sum(String[] v) {
        return arrstream(v).collect(Collectors.joining());
    }
    public static String sum(Iterable<String> v, String ... ignore) {
        return arrstream(v).collect(Collectors.joining());
    }
    public static String sum(Iterator<String> v, String ... ignore) {
        return arrstream(v).collect(Collectors.joining());
    }
    public static <T> boolean none(T[] v) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return false; } }
        return true;
    }
    public static <T> boolean none(Iterable<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return false; } }
        return true;
    }
    public static <T> boolean none(Iterator<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return false; } }
        return true;
    }
    public static <T> boolean any(T[] v) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return true; } }
        return false;
    }
    public static <T> boolean any(Iterable<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return true; } }
        return false;
    }
    public static <T> boolean any(Iterator<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(__)) { return true; } }
        return false;
    }
    public static <T> boolean all(T[] v) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(!LangUtil.isTruthy(__))) { return false; } }
        return true;
    }
    public static <T> boolean all(Iterable<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(!LangUtil.isTruthy(__))) { return false; } }
        return true;
    }
    public static <T> boolean all(Iterator<T> v, int ... ignore) {
        for (var __ : LangUtil.asIterable(v)) { if (LangUtil.isTruthy(!LangUtil.isTruthy(__))) { return false; } }
        return true;
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
    public static <T> boolean isTruthy(T[] v) {
        return v.length > 0;
    }
    public static <T> boolean isTruthy(List<T> v) {
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
    public static <T> T[] asIterable(T[] v) {
        return v;
    }
    public static List<Integer> asIterable(int n) {
        var lst = new ArrayList<Integer>();
        for (int i = 0; i < n; ++i) {
            lst.add(i);
        }
        return lst;
    }
    public static <T> Iterable<T> asIterable(Iterable<T> v) {
        return v;
    }
    public static <T> Iterable<T> asIterable(Iterator<T> v) {
        return new IteratorToIterable<T>(v);
    }
    public static <TK, TV> Set<TK> asIterable(Map<TK, TV> v) {
        return v.keySet();
    }
    public static char[] asIterable(String s) {
        return s.toCharArray();
    }
    public static String slice(String s, int start, int end, int step) {
        start = indexConvert(start, s.length());
        end = indexConvert(end, s.length());
        if (LangUtil.isTruthy(Extensions.operEq(step, 1))) {
            return s.substring(start, end);
        }
        var newStr = "";
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            newStr = Extensions.operAdd(newStr, (s.charAt(i)));
        }
        return newStr;
    }
    public static String slice(String s, Null start, Null end, int step) {
        return slice(s, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(s.length(), 1)), LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (s.length()) : (Extensions.operSub(Extensions.operUnarySub(s.length()), 1)), step);
    }
    public static String slice(String s, Null start, int end, int step) {
        return slice(s, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(s.length(), 1)), end, step);
    }
    public static String slice(String s, int start, Null end, int step) {
        return slice(s, start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (s.length()) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> ArrayList<T> slice(ArrayList<T> v, int start, int end, int step) {
        start = indexConvert(start, v.size());
        end = indexConvert(end, v.size());
        return new ArrayList<>(v.subList(start, end));
    }
    public static <T> ArrayList<T> slice(ArrayList<T> v, Null start, Null end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.size(), 1)), LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.size()) : (Extensions.operSub(Extensions.operUnarySub(v.size()), 1)), step);
    }
    public static <T> ArrayList<T> slice(ArrayList<T> v, Null start, int end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.size(), 1)), end, step);
    }
    public static <T> ArrayList<T> slice(ArrayList<T> v, int start, Null end, int step) {
        return slice(v, start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.size()) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> ArrayList<T> slice(List<T> v, int start, int end, int step) {
        start = indexConvert(start, v.size());
        end = indexConvert(end, v.size());
        var lst = new ArrayList<T>();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            lst.add(v.get(i));
        }
        return lst;
    }
    public static <T> ArrayList<T> slice(List<T> v, Null start, Null end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.size(), 1)), LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.size()) : (Extensions.operSub(Extensions.operUnarySub(v.size()), 1)), step);
    }
    public static <T> ArrayList<T> slice(List<T> v, Null start, int end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.size(), 1)), end, step);
    }
    public static <T> ArrayList<T> slice(List<T> v, int start, Null end, int step) {
        return slice(v, start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.size()) : (Extensions.operUnarySub(1)), step);
    }
    public static <T> ArrayList<T> slice(T[] v, int start, int end, int step) {
        start = indexConvert(start, v.length);
        end = indexConvert(end, v.length);
        var lst = new ArrayList<T>();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            lst.add(Extensions.operGetIndex(v, i));
        }
        return lst;
    }
    public static <T> ArrayList<T> slice(T[] v, Null start, Null end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.length, 1)), LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.length) : (Extensions.operSub(Extensions.operUnarySub(v.length), 1)), step);
    }
    public static <T> ArrayList<T> slice(T[] v, Null start, int end, int step) {
        return slice(v, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (0) : (Extensions.operSub(v.length, 1)), end, step);
    }
    public static <T> ArrayList<T> slice(T[] v, int start, Null end, int step) {
        return slice(v, start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (v.length) : (Extensions.operUnarySub(1)), step);
    }
    public static int indexConvert(int index, int size) {
        if (LangUtil.isTruthy(Extensions.operLt(index, 0))) { index = Extensions.operAdd(index, (size)); }
        return index;
    }
    public static IntRange range(int start, int stop, int step) {
        return new IntRange(start, stop, step);
    }
    public static IntRange range(int start, Null stop, Null step) {
        return range(start, Integer.MAX_VALUE, 1);
    }
    public static IntRange range(int start, Null stop, int step) {
        return range(start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (Integer.MAX_VALUE) : (Integer.MIN_VALUE), step);
    }
    public static IntRange range(int start, int stop, Null step) {
        return range(start, stop, LangUtil.isTruthy(Extensions.operLt(start, stop)) ? (1) : (Extensions.operUnarySub(1)));
    }
    public static LongRange range(long start, long stop, long step) {
        return new LongRange(start, stop, step);
    }
    public static LongRange range(long start, Null stop, Null step) {
        return range(start, Long.MAX_VALUE, 1);
    }
    public static LongRange range(long start, Null stop, long step) {
        return range(start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (Long.MAX_VALUE) : (Long.MIN_VALUE), step);
    }
    public static LongRange range(long start, long stop, Null step) {
        return range(start, stop, LangUtil.isTruthy(Extensions.operLt(start, stop)) ? (1) : (Extensions.operUnarySub(1)));
    }
    public static DoubleRange range(double start, double stop, double step) {
        return new DoubleRange(start, stop, step);
    }
    public static DoubleRange range(double start, Null stop, Null step) {
        return range(start, Double.MAX_VALUE, 1);
    }
    public static DoubleRange range(double start, Null stop, double step) {
        return range(start, LangUtil.isTruthy(Extensions.operGt(step, 0)) ? (Double.MAX_VALUE) : (Double.MIN_VALUE), step);
    }
    public static DoubleRange range(double start, double stop, Null step) {
        return range(start, stop, LangUtil.isTruthy(Extensions.operLt(start, stop)) ? (1) : (Extensions.operUnarySub(1)));
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

