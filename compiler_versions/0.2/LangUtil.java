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
    public static boolean isTruthy(boolean v) {
        return v;
    }
    public static boolean isTruthy(Object v) {
        return v != null;
    }
    public static boolean isTruthy(int v) {
        return v != 0;
    }
    public static boolean isTruthy(double v) {
        return v != 0;
    }
    public static boolean isTruthy(String v) {
        return !LangUtil.isTruthy(v.isEmpty());
    }
    public static <T> boolean isTruthy(T [] v) {
        return v.length > 0;
    }
    public static boolean isTruthy(List v) {
        return !LangUtil.isTruthy(v.isEmpty());
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
    public static Object get(Object obj , String varname) {
        var method_name = "_get_" + varname;
        var inst = obj;
        try {
            return inst.getClass().getDeclaredMethod(method_name).invoke(inst);
        }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            
        }
        try {
            return inst.getClass().getDeclaredField(varname).get(inst);
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            return null;
        }
    }
    public static Object dot(Object obj , String method) {
        try {
            return obj.getClass().getMethod(method);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
}

