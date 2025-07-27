import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class LangUtil {
    public static void print(object s) {
        System.out.print("" + s);
    }
    public static void println(object s) {
        System.out.println("" + s);
    }
    public static Boolean isTruthy(Boolean v) {
        return v;
    }
    public static Boolean isTruthy(object v) {
        return v != null;
    }
    public static Boolean isTruthy(Integer v) {
        return v != 0;
    }
    public static Boolean isTruthy(Double v) {
        return v != 0;
    }
    public static Boolean isTruthy(String v) {
        return !LangUtil.isTruthy((((v) != null) ? (v) : ("")).isEmpty());
    }
    public static <T> Boolean isTruthy(T [] v) {
        return v.length > 0;
    }
    public static Boolean isTruthy(List v) {
        return !LangUtil.isTruthy((((v) != null) ? (v) : (new List())).isEmpty());
    }
    public static <T> T [] asIterable(T [] v) {
        return v;
    }
    public static List < int > asIterable(Integer n) {
        var lst = new ArrayList < int > ();
        for (int i = 0; i < n; ++i) {
            lst.add(i);
        }
        return lst;
    }
    public static <T> Iterable < T > asIterable(Iterable < T > v) {
        return v;
    }
    public static Character [] asIterable(String s) {
        return s.toCharArray();
    }
}

