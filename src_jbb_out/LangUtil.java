import java.io.*;
import java.util.*;

public class LangUtil {
    public static void print(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print("" + x); }
    }
    public static void println(Object ... args) {
        for (var x : LangUtil.asIterable(args)) { System.out.print("" + x); }
        System.out.println("");
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
        return !Extensions.operEq(v, null);
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < Integer > asIterable(int n) {
        var lst = new ArrayList < Integer > ();
        for (int i = 0; i < n; ++i) {
            lst.add(i);
        }
        return lst;
    }
    public static <T> Iterable < T > asIterable(Iterable < T > v) {
        return v;
    }
    public static char [] asIterable(String s) {
        return s.toCharArray();
    }
    public static IntRange range(Integer start , Integer stop , Integer step) {
        return new IntRange(start, stop, step);
    }
    public static LongRange range(Long start , Long stop , Long step) {
        return new LongRange(start, stop, step);
    }
    public static DoubleRange range(Double start , Double stop , Double step) {
        return new DoubleRange(start, stop, step);
    }
    public static CharRange range(Character start , Character stop , Character step) {
        return new CharRange(start, stop, step);
    }
    
class IntRange implements Iterator<Integer> {
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
            return start < stop;
        if (step < 0 && stop < start)
            return start > stop;
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
    
class LongRange implements Iterator<Long> {
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
            return start < stop;
        if (step < 0 && stop < start)
            return start > stop;
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
    
class DoubleRange implements Iterator<Double> {
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
            return start < stop;
        if (step < 0 && stop < start)
            return start > stop;
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
    
class CharRange implements Iterator<Character> {
    public char start;
    public char stop;
    public char step;
    public char current;

    public CharRange(Character start, Character stop, Character step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (step > 0 && stop > start)
            return start < stop;
        if (step < 0 && stop < start)
            return start > stop;
        return step == 0;
    }

    @Override
    public Character next() {
        if (!hasNext())
            throw new NoSuchElementException();

        char value = current;
        current += step;

        return value;
    }
}
}

