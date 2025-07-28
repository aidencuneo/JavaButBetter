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
        return v != 0;
    }
    public static boolean isTruthy(double v) {
        return v != 0;
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
}

