import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Extensions {
    public static int len(String s) {
        return s.length();
    }
    public static <T> int len(Iterable<T> v) {
        var c = 0;
        for (var x : LangUtil.asIterable(v)) { ++ c; }
        return c;
    }
    public static <T> int len(T[] v) {
        return v.length;
    }
    public static char operGetIndex(String s, int i) {
        i = LangUtil.indexConvert(i, s.length());
        return s.charAt(i);
    }
    public static <T> T operGetIndex(T[] v, int i) {
        i = LangUtil.indexConvert(i, v.length);
        return v[i];
    }
    public static <T> T operGetIndex(List<T> v, int i) {
        i = LangUtil.indexConvert(i, v.size());
        return v.get(i);
    }
    public static <TK, TV> TV operGetIndex(Map<TK, TV> v, TK key) {
        return v.get(key);
    }
    public static boolean operEq(int a, int b) {
        return a == b;
    }
    public static boolean operEq(long a, long b) {
        return a == b;
    }
    public static boolean operEq(double a, double b) {
        return a == b;
    }
    public static boolean operEq(boolean a, boolean b) {
        return a == b;
    }
    public static boolean operEq(String a, String b) {
        if (a == null) return b == null;
        return a.equals(b);
    }
    public static boolean operEq(Object a, Object b) {
        if (a == null) return b == null;
        return a.equals(b);
    }
    public static boolean operIn(char c, String s) {
        return !((boolean) Extensions.operEq(s.indexOf(c), Extensions.operUnarySub(1)));
    }
    public static boolean operIn(String part, String s) {
        return !((boolean) Extensions.operEq(s.indexOf(part), Extensions.operUnarySub(1)));
    }
    public static boolean operIn(Object o, List lst) {
        return lst.contains(o);
    }
    public static boolean operIn(Object o, Object[] lst) {
        return Arrays.stream(lst).anyMatch(x -> x.equals(o));
    }
    public static boolean operIn(Object o, Set s) {
        return s.contains(o);
    }
    public static boolean operIn(Object o, Map m) {
        return m.containsKey(o);
    }
    public static boolean operGt(int a, int b) {
        return a > b;
    }
    public static boolean operGt(long a, long b) {
        return a > b;
    }
    public static boolean operGt(double a, double b) {
        return a > b;
    }
    public static boolean operLt(int a, int b) {
        return a < b;
    }
    public static boolean operLt(long a, long b) {
        return a < b;
    }
    public static boolean operLt(double a, double b) {
        return a < b;
    }
    public static boolean operGe(int a, int b) {
        return a >= b;
    }
    public static boolean operGe(long a, long b) {
        return a >= b;
    }
    public static boolean operGe(double a, double b) {
        return a >= b;
    }
    public static boolean operLe(int a, int b) {
        return a <= b;
    }
    public static boolean operLe(long a, long b) {
        return a <= b;
    }
    public static boolean operLe(double a, double b) {
        return a <= b;
    }
    public static int operOr(int a, int b) {
        return a | b;
    }
    public static long operOr(long a, long b) {
        return a | b;
    }
    public static boolean operOr(boolean a, boolean b) {
        return (LangUtil.isTruthy(a)) ? (a) : (b);
    }
    public static <T> List<T> operOr(List<T> a, List<T> b) {
        var res = new ArrayList<T>(a);
        for (var x : LangUtil.asIterable(b)) { if (LangUtil.isTruthy(!((boolean) Extensions.operIn(x, res)))) { res.add(x); } }
        return res;
    }
    public static <T> List<T> operOr(T[] a, T[] b) {
        var res = new ArrayList<T>();
        for (var x : LangUtil.asIterable(a)) { res.add(x); }
        for (var x : LangUtil.asIterable(b)) { if (LangUtil.isTruthy(!((boolean) Extensions.operIn(x, res)))) { res.add(x); } }
        return res;
    }
    public static int operXor(int a, int b) {
        return a ^ b;
    }
    public static long operXor(long a, long b) {
        return a ^ b;
    }
    public static boolean operXor(boolean a, boolean b) {
        return !((boolean) Extensions.operEq(a, b));
    }
    public static int operAnd(int a, int b) {
        return a & b;
    }
    public static long operAnd(long a, long b) {
        return a & b;
    }
    public static boolean operAnd(boolean a, boolean b) {
        return (LangUtil.isTruthy(a)) ? (b) : (a);
    }
    public static <T> List<T> operAnd(List<T> a, List<T> b) {
        var res = new ArrayList<T>();
        for (var x : LangUtil.asIterable(a)) { if (LangUtil.isTruthy((Extensions.operIn(x, b)))) { res.add(x); } }
        return res;
    }
    public static <T> List<T> operAnd(T[] a, T[] b) {
        var res = new ArrayList<T>();
        for (var x : LangUtil.asIterable(a)) { if (LangUtil.isTruthy((Extensions.operIn(x, b)))) { res.add(x); } }
        return res;
    }
    public static int operBitNot(int a) {
        return ~a;
    }
    public static long operBitNot(long a) {
        return ~a;
    }
    public static boolean operBitNot(boolean a) {
        return !LangUtil.isTruthy(a);
    }
    public static int operUnaryAdd(int a) {
        return a;
    }
    public static long operUnaryAdd(long a) {
        return a;
    }
    public static double operUnaryAdd(double a) {
        return a;
    }
    public static int operUnarySub(int a) {
        return -a;
    }
    public static long operUnarySub(long a) {
        return -a;
    }
    public static double operUnarySub(double a) {
        return -a;
    }
    public static int operAdd(int a, int b) {
        return a + b;
    }
    public static long operAdd(long a, long b) {
        return a + b;
    }
    public static double operAdd(double a, double b) {
        return a + b;
    }
    public static String operAdd(String a, Object b) {
        return a + b;
    }
    public static String operAdd(Object a, String b) {
        return a + b;
    }
    public static String operAdd(String a, String b) {
        return a + b;
    }
    public static <T> ArrayList<T> operAdd(List<T> a, List<T> b) {
        var copy = new ArrayList<>(a);
        copy.addAll(b);
        return copy;
    }
    public static int operSub(int a, int b) {
        return a - b;
    }
    public static long operSub(long a, long b) {
        return a - b;
    }
    public static double operSub(double a, double b) {
        return a - b;
    }
    public static int operMul(int a, int b) {
        return a * b;
    }
    public static long operMul(long a, long b) {
        return a * b;
    }
    public static double operMul(double a, double b) {
        return a * b;
    }
    public static String operMul(String a, int b) {
        if (LangUtil.isTruthy(Extensions.operLt(b, 0))) {
            return new StringBuilder(a).reverse().toString().repeat(Extensions.operUnarySub(b));
        }
        return a.repeat(b);
    }
    public static String operMul(int a, String b) {
        return operMul(b, a);
    }
    public static int operDiv(int a, int b) {
        return a / b;
    }
    public static long operDiv(long a, long b) {
        return a / b;
    }
    public static double operDiv(double a, double b) {
        return a / b;
    }
    public static int operMod(int a, int b) {
        return a % b;
    }
    public static long operMod(long a, long b) {
        return a % b;
    }
    public static double operMod(double a, double b) {
        return a % b;
    }
    public static int operShl(int a, int b) {
        return a << b;
    }
    public static long operShl(long a, long b) {
        return a << b;
    }
    public static String operShl(String a, Object b) {
        return Extensions.operAdd(a, b);
    }
    public static String operShl(Object a, String b) {
        return Extensions.operAdd(a, b);
    }
    public static String operShl(String a, String b) {
        return Extensions.operAdd(a, b);
    }
    public static <T> T[] operShl(T[] arr, T elem) {
        var copy = Arrays.copyOf(arr, Extensions.operAdd(arr.length, 1));
        copy [arr.length] = elem;
        return copy;
    }
    public static <T> List<T> operShl(List<T> c, T elem) {
        c.add(elem);
        return c;
    }
    public static <T> T[] operShl(int count, T[] arr) {
        return Arrays.copyOfRange(arr, count, arr.length);
    }
    public static <T> List<T> operShl(int count, List<T> arr) {
        arr.subList(0, count).clear();
        return arr;
    }
    public static int operShr(int a, int b) {
        return a >> b;
    }
    public static long operShr(long a, long b) {
        return a >> b;
    }
    public static String operShr(String a, Object b) {
        return Extensions.operAdd(b, a);
    }
    public static String operShr(Object a, String b) {
        return Extensions.operAdd(b, a);
    }
    public static String operShr(String a, String b) {
        return Extensions.operAdd(b, a);
    }
    public static <T> T[] operShr(T elem, T[] arr) {
        var copy = Arrays.copyOf(arr, Extensions.operAdd(arr.length, 1));
        copy [0] = elem;
        return copy;
    }
    public static <T> List<T> operShr(T elem, List<T> c) {
        c.add(elem);
        return c;
    }
    public static <T> T[] operShr(T[] arr, int count) {
        return Arrays.copyOf(arr, Extensions.operSub(arr.length, count));
    }
    public static <T> List<T> operShr(List<T> arr, int count) {
        arr.subList(Extensions.operSub(arr.size(), count), arr.size()).clear();
        return arr;
    }
    public static Object operUshl(Object a, Object b) {
        throw new UnsupportedOperationException();
    }
    public static int operUshr(int a, int b) {
        return a >>> b;
    }
    public static long operUshr(long a, long b) {
        return a >>> b;
    }
}

