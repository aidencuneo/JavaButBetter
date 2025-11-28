import java.io.*;
import java.util.*;

public class Extensions {
    public static int len(String) {
        return s.length();
    }
    public static <T> int len(Iterable < T >) {
        var c = 0;
        for (var _ : LangUtil.asIterable(v)) { ++ c; }
        return c;
    }
    public static <T> int len(T []) {
        return v.length;
    }
    public static char operGetIndex(String , int) {
        i = LangUtil.indexConvert(i, s.length());
        return s.charAt(i);
    }
    public static <T> T operGetIndex(T [] , int) {
        i = LangUtil.indexConvert(i, v.length);
        return v[i];
    }
    public static <T> T operGetIndex(List < T > , int) {
        i = LangUtil.indexConvert(i, v.size());
        return v.get(i);
    }
    public static int operAdd(int , int) {
        return a + b;
    }
    public static long operAdd(long , long) {
        return a + b;
    }
    public static double operAdd(double , double) {
        return a + b;
    }
    public static int operAdd(boolean , boolean) {
        return (a ? 1 : 0) + (b ? 1 : 0);
    }
    public static boolean operEq(int , int) {
        return a == b;
    }
    public static boolean operEq(long , long) {
        return a == b;
    }
    public static boolean operEq(double , double) {
        return a == b;
    }
    public static boolean operEq(boolean , boolean) {
        return a == b;
    }
    public static boolean operEq(String , String) {
        return a.equals(b);
    }
    public static boolean operEq(Object , Object) {
        return a.equals(b);
    }
}

