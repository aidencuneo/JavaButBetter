import java.io.*;
import java.util.*;

public class LangUtil {
    public static void print(Object ...) {
        for (var x : LangUtil.asIterable(args)) { System.out.print("" + x); }
    }
    public static void println(Object ...) {
        for (var x : LangUtil.asIterable(args)) { System.out.print("" + x); }
        System.out.println("");
    }
    public static boolean isTruthy(boolean) {
        return v;
    }
    public static boolean isTruthy(int) {
        return !Extensions.operEq(v, 0);
    }
    public static boolean isTruthy(double) {
        return !Extensions.operEq(v, 0);
    }
    public static boolean isTruthy(String) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static <T> boolean isTruthy(T []) {
        return v.length > 0;
    }
    public static boolean isTruthy(List) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !v.isEmpty();
    }
    public static boolean isTruthy(Object) {
        if (v instanceof Boolean x) return x;
        if (v instanceof Integer x) return x != 0;
        if (v instanceof Double x) return x != 0;
        if (v instanceof String x) return x == null ? false : !x.isEmpty();
        if (v instanceof List x) return x == null ? false : !x.isEmpty();
        return !Extensions.operEq(v, null);
    }
    public static <T> T [] asIterable(T []) {
        return v;
    }
    public static List < Integer > asIterable(int) {
        var lst = new ArrayList < Integer > ();
        for (int i = 0; i < n; ++i) {
            lst.add(i);
        }
        return lst;
    }
    public static <T> Iterable < T > asIterable(Iterable < T >) {
        return v;
    }
    public static <T> Iterable < T > asIterable(Iterator < T >) {
        return new IteratorToIterable < T > (v);
    }
    public static <TK, TV> Set < TK > asIterable(HashMap < , TV >) {
        return v.keySet();
    }
    public static char [] asIterable(String) {
        return s.toCharArray();
    }
    public static String slice(String , int , int , int) {
        start = indexConvert(start, s.length());
        end = indexConvert(end, s.length());
        if (LangUtil.isTruthy(Extensions.operEq(step, 1))) {
            return s.substring(start, end);
        }
        var newStr = "";
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            newStr += s.charAt(i);
        }
        return newStr;
    }
    public static String slice(String , Null , Null , int) {
        return slice(s, LangUtil.isTruthy(step > 0) ? (0) : (s.length() - 1), LangUtil.isTruthy(step > 0) ? (s.length()) : (- s.length() - 1), step);
    }
    public static String slice(String , Null , int , int) {
        return slice(s, LangUtil.isTruthy(step > 0) ? (0) : (s.length() - 1), end, step);
    }
    public static String slice(String , int , Null , int) {
        return slice(s, start, LangUtil.isTruthy(step > 0) ? (s.length()) : (- 1), step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > , int , int , int) {
        start = indexConvert(start, v.size());
        end = indexConvert(end, v.size());
        return new ArrayList < > (v.subList(start, end));
    }
    public static <T> ArrayList < T > slice(ArrayList < T > , Null , Null , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.size() - 1), LangUtil.isTruthy(step > 0) ? (v.size()) : (- v.size() - 1), step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > , Null , int , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.size() - 1), end, step);
    }
    public static <T> ArrayList < T > slice(ArrayList < T > , int , Null , int) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (v.size()) : (- 1), step);
    }
    public static <T> List < T > slice(List < T > , int , int , int) {
        start = indexConvert(start, v.size());
        end = indexConvert(end, v.size());
        var lst = new ArrayList < T > ();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            LangUtil.println(i + ", " + start + ", " + end + ", " + step);
            lst.add(v.get(i));
        }
        return lst;
    }
    public static <T> List < T > slice(List < T > , Null , Null , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.size() - 1), LangUtil.isTruthy(step > 0) ? (v.size()) : (- v.size() - 1), step);
    }
    public static <T> List < T > slice(List < T > , Null , int , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.size() - 1), end, step);
    }
    public static <T> List < T > slice(List < T > , int , Null , int) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (v.size()) : (- 1), step);
    }
    public static <T> List < T > slice(T [] , int , int , int) {
        start = indexConvert(start, v.length);
        end = indexConvert(end, v.length);
        var lst = new ArrayList < T > ();
        for (int i = start; step > 0 ? (i < end) : (i > end); i += step) {
            lst.add(Extensions.operGetIndex(v, i));
        }
        return lst;
    }
    public static <T> List < T > slice(T [] , Null , Null , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.length - 1), LangUtil.isTruthy(step > 0) ? (v.length) : (- v.length - 1), step);
    }
    public static <T> List < T > slice(T [] , Null , int , int) {
        return slice(v, LangUtil.isTruthy(step > 0) ? (0) : (v.length - 1), end, step);
    }
    public static <T> List < T > slice(T [] , int , Null , int) {
        return slice(v, start, LangUtil.isTruthy(step > 0) ? (v.length) : (- 1), step);
    }
    public static int indexConvert(int , int) {
        if (LangUtil.isTruthy(index < 0)) { index += size; }
        return index;
    }
    public static IntRange range(int , int , int) {
        return new IntRange(start, stop, step);
    }
    public static IntRange range(int , Null , Null) {
        return range(start, Integer.MAX_VALUE, 1);
    }
    public static IntRange range(int , Null , int) {
        return range(start, LangUtil.isTruthy(step > 0) ? (Integer.MAX_VALUE) : (Integer.MIN_VALUE), step);
    }
    public static IntRange range(int , int , Null) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (- 1));
    }
    public static LongRange range(long , long , long) {
        return new LongRange(start, stop, step);
    }
    public static LongRange range(long , Null , Null) {
        return range(start, Long.MAX_VALUE, 1);
    }
    public static LongRange range(long , Null , long) {
        return range(start, LangUtil.isTruthy(step > 0) ? (Long.MAX_VALUE) : (Long.MIN_VALUE), step);
    }
    public static LongRange range(long , long , Null) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (- 1));
    }
    public static DoubleRange range(double , double , double) {
        return new DoubleRange(start, stop, step);
    }
    public static DoubleRange range(double , Null , Null) {
        return range(start, Double.MAX_VALUE, 1);
    }
    public static DoubleRange range(double , Null , double) {
        return range(start, LangUtil.isTruthy(step > 0) ? (Double.MAX_VALUE) : (Double.MIN_VALUE), step);
    }
    public static DoubleRange range(double , double , Null) {
        return range(start, stop, LangUtil.isTruthy(start < stop) ? (1) : (- 1));
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

