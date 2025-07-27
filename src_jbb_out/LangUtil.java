import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class LangUtil {
    public static void print(Object s) {
        System.out.print("" + s);
    }
    public static void println(Object s) {
        System.out.println("" + s);
    }
    public static Boolean isTruthy(Boolean v) {
        return v;
    }
    public static Boolean isTruthy(Object v) {
        return v != null;
    }
    public static Boolean isTruthy(Integer v) {
        return v != 0;
    }
    public static Boolean isTruthy(Double v) {
        return v != 0;
    }
    public static Boolean isTruthy(String v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !LangUtil.isTruthy(v.isEmpty());
    }
    public static <T> Boolean isTruthy(T [] v) {
        return v.length > 0;
    }
    public static Boolean isTruthy(List v) {
        if (LangUtil.isTruthy(v == null)) { return false; }
        return !LangUtil.isTruthy(v.isEmpty());
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < Integer > asIterable(Integer n) {
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

