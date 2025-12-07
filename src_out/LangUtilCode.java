import java.io.*;
import java.util.*;

public class LangUtilCode {
    public static CompResult get() {
        return Compiler.compileFile("LangUtil", """
import java.lang.reflect.*
import java.util.function.Function

class Null
    ...

public static class LangUtil

print(object ... args):
    System.out.print('' + x) for x in args

println(object ... args):
    System.out.print('' + x) for x in args
    System.out.println('')

// nullCheck
(T, R) R nullCheck(T value, Function<T, R> func):
    ret func.apply(value) if value != null else null

// round
double round(double v, int places):
    return Math.round(v * 10 ** places) / 10 ** places

double round(double v):
    return Math.round(v)

// roundstr
string roundstr(double v, int places):
    return string.format("%." + places + "f", v)

string roundstr(double v):
    return string.format("%f", v)

// isTruthy
bool isTruthy(bool v):
    ret v

bool isTruthy(int v):
    ret v != 0

bool isTruthy(double v):
    ret v != 0

bool isTruthy(string v):
    ret false if v is null
    inline(return !v.isEmpty();)

(T) bool isTruthy(T[] v):
    ret v.length > 0

bool isTruthy(List v):
    ret false if v is null
    inline(return !v.isEmpty();)

bool isTruthy(object v):
    inline(if (v instanceof Boolean x) return x;)
    inline(if (v instanceof Integer x) return x != 0;)
    inline(if (v instanceof Double x) return x != 0;)
    inline(if (v instanceof String x) return x == null ? false : !x.isEmpty();)
    inline(if (v instanceof List x) return x == null ? false : !x.isEmpty();)
    ret v != null

// asIterable
(T) T[] asIterable(T[] v):
    ret v

List<Int> asIterable(int n):
    lst = new ArrayList<Int>()
    inline(for (int i = 0; i < n; ++i))
        lst.add(i)
    ret lst

(T) Iterable<T> asIterable(Iterable<T> v):
    ret v

(T) Iterable<T> asIterable(Iterator<T> v):
    ret new IteratorToIterable<T>(v)

(TK, TV) Set<TK> asIterable(Map<TK, TV> v):
    ret v.keySet()

char[] asIterable(string s):
    ret s.toCharArray()

// slice (string)
string slice(string s, int start, int end, int step):
    start = indexConvert(start, s.length())
    end = indexConvert(end, s.length())

    if step == 1
        return s.substring(start, end)

    newStr = ""
    inline(for (int i = start; step > 0 ? (i < end) : (i > end); i += step))
        newStr += s.charAt(i)
    ret newStr

string slice(string s, Null start, Null end, int step):
    return slice(s, step > 0 ? 0 : s.length() - 1, step > 0 ? s.length() : -s.length() - 1, step)

string slice(string s, Null start, int end, int step):
    return slice(s, step > 0 ? 0 : s.length() - 1, end, step)

string slice(string s, int start, Null end, int step):
    return slice(s, start, step > 0 ? s.length() : -1, step)

// slice (ArrayList)
(T) ArrayList<T> slice(ArrayList<T> v, int start, int end, int step):
    start = indexConvert(start, v.size())
    end = indexConvert(end, v.size())
    return new ArrayList<>(v.subList(start, end))

(T) ArrayList<T> slice(ArrayList<T> v, Null start, Null end, int step):
    return slice(v, step > 0 ? 0 : v.size() - 1, step > 0 ? v.size() : -v.size() - 1, step)

(T) ArrayList<T> slice(ArrayList<T> v, Null start, int end, int step):
    return slice(v, step > 0 ? 0 : v.size() - 1, end, step)

(T) ArrayList<T> slice(ArrayList<T> v, int start, Null end, int step):
    return slice(v, start, step > 0 ? v.size() : -1, step)

// slice (List)
(T) List<T> slice(List<T> v, int start, int end, int step):
    start = indexConvert(start, v.size())
    end = indexConvert(end, v.size())

    lst = new ArrayList<T>()
    inline(for (int i = start; step > 0 ? (i < end) : (i > end); i += step))
        println i + ", " + start + ", " + end + ", " + step
        lst.add(v.get(i))
    ret lst

(T) List<T> slice(List<T> v, Null start, Null end, int step):
    return slice(v, step > 0 ? 0 : v.size() - 1, step > 0 ? v.size() : -v.size() - 1, step)

(T) List<T> slice(List<T> v, Null start, int end, int step):
    return slice(v, step > 0 ? 0 : v.size() - 1, end, step)

(T) List<T> slice(List<T> v, int start, Null end, int step):
    return slice(v, start, step > 0 ? v.size() : -1, step)

// slice (array)
(T) List<T> slice(T[] v, int start, int end, int step):
    start = indexConvert(start, v.length)
    end = indexConvert(end, v.length)

    lst = new ArrayList<T>()
    inline(for (int i = start; step > 0 ? (i < end) : (i > end); i += step))
        lst.add(v[i])
    ret lst

(T) List<T> slice(T[] v, Null start, Null end, int step):
    return slice(v, step > 0 ? 0 : v.length - 1, step > 0 ? v.length : -v.length - 1, step)

(T) List<T> slice(T[] v, Null start, int end, int step):
    return slice(v, step > 0 ? 0 : v.length - 1, end, step)

(T) List<T> slice(T[] v, int start, Null end, int step):
    return slice(v, start, step > 0 ? v.length : -1, step)

// getField (reflection)
(T) T getField(object obj, string name):
    try
        c = obj.getClass()

        while c
            try
                f = c.getDeclaredField(name)
                f.setAccessible(true)
                return (T) f.get(obj)
            catch NoSuchFieldException e
                c .= getSuperclass()

        inline(throw new RuntimeException("Field not found: " + name);)
    catch Exception e
        inline(throw new RuntimeException(e);)

// getMethod (reflection)
(T) T callMethod(object obj, string methodName, object... args):
    try
        c = obj.getClass()
        inline(var argTypes = new Class<?>[args.length];)
        for i in [..args.length]
            inline(argTypes[i] = args[i] == null ? Object.class : args[i].getClass();)

        while c
            try
                m = c.getDeclaredMethod(methodName, argTypes)
                m.setAccessible(true)
                return (T) m.invoke(obj, args)
            catch NoSuchMethodException e
                c .= getSuperclass()

        inline(throw new RuntimeException("Method not found: " + methodName);)
    catch Exception e
        inline(throw new RuntimeException(e);)

// indexConvert (helper function)
int indexConvert(int index, int size):
    index += size if index < 0
    return index

// range (int)
IntRange range(int start, int stop, int step):
    return new IntRange(start, stop, step)

IntRange range(int start, Null stop, Null step):
    return range(start, Int.MAX_VALUE, 1)

IntRange range(int start, Null stop, int step):
    return range(start, step > 0 ? Int.MAX_VALUE : Int.MIN_VALUE, step)

IntRange range(int start, int stop, Null step):
    return range(start, stop, start < stop ? 1 : -1)

// range (long)
LongRange range(long start, long stop, long step):
    return new LongRange(start, stop, step)

LongRange range(long start, Null stop, Null step):
    return range(start, Long.MAX_VALUE, 1)

LongRange range(long start, Null stop, long step):
    return range(start, step > 0 ? Long.MAX_VALUE : Long.MIN_VALUE, step)

LongRange range(long start, long stop, Null step):
    return range(start, stop, start < stop ? 1 : -1)

// range (double)
DoubleRange range(double start, double stop, double step):
    return new DoubleRange(start, stop, step)

DoubleRange range(double start, Null stop, Null step):
    return range(start, Double.MAX_VALUE, 1)

DoubleRange range(double start, Null stop, double step):
    return range(start, step > 0 ? Double.MAX_VALUE : Double.MIN_VALUE, step)

DoubleRange range(double start, double stop, Null step):
    return range(start, stop, start < stop ? 1 : -1)

// // range (char)
// CharRange range(char start, char stop, char step):
//     return new CharRange(start, stop, step)

// CharRange range(char start, Null stop, Null step):
//     return range(start, Char.MAX_VALUE, 1)

// CharRange range(char start, Null stop, char step):
//     return range(start, step > 0 ? Char.MAX_VALUE : Char.MIN_VALUE, step)

// CharRange range(char start, char stop, Null step):
//     return range(start, stop, start < stop ? 1 : -1)

// IntRange class
inline(
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
})

// LongRange class
inline(
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
})

// DoubleRange class
inline(
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
})

// Iterator to iterable class
inline(
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
})
    """.trim());
    }
}

