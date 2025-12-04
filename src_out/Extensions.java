import java.io.*;
import java.util.*;

public class Extensions {
    public static int len(String s) {
        return s.length();
    }
    public static <T> int len(Iterable < T > v) {
        var c = 0;
        for (var x : LangUtil.asIterable(v)) { ++ c; }
        return c;
    }
    public static <T> int len(T [] v) {
        return v.length;
    }
    public static char operGetIndex(String s , int i) {
        i = LangUtil.indexConvert(i, s.length());
        return s.charAt(i);
    }
    public static <T> T operGetIndex(T [] v , int i) {
        i = LangUtil.indexConvert(i, v.length);
        return v[i];
    }
    public static <T> T operGetIndex(List < T > v , int i) {
        i = LangUtil.indexConvert(i, v.size());
        return v.get(i);
    }
    public static <TK, TV> TV operGetIndex(Map < TK , TV > v , TK key) {
        return v.get(key);
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
    public static int operAdd(int a , int b) {
        return a + b;
    }
    public static long operAdd(long a , long b) {
        return a + b;
    }
    public static double operAdd(double a , double b) {
        return a + b;
    }
    public static String operAdd(String a , Object b) {
        return a + b;
    }
    public static String operAdd(Object a , String b) {
        return a + b;
    }
    public static int operSub(int a , int b) {
        return a - b;
    }
    public static long operSub(long a , long b) {
        return a - b;
    }
    public static double operSub(double a , double b) {
        return a - b;
    }
    public static int operMul(int a , int b) {
        return a * b;
    }
    public static long operMul(long a , long b) {
        return a * b;
    }
    public static double operMul(double a , double b) {
        return a * b;
    }
    public static String operMul(String a , int b) {
        if (LangUtil.isTruthy(b < 0)) {
            return new StringBuilder(a).reverse().toString().repeat(Extensions.operUnarySub(b));
        }
        return a.repeat(b);
    }
    public static int operDiv(int a , int b) {
        return a / b;
    }
    public static long operDiv(long a , long b) {
        return a / b;
    }
    public static double operDiv(double a , double b) {
        return a / b;
    }
    public static int operMod(int a , int b) {
        return a % b;
    }
    public static long operMod(long a , long b) {
        return a % b;
    }
    public static double operMod(double a , double b) {
        return a % b;
    }
    public static boolean operEq(int a , int b) {
        return a == b;
    }
    public static boolean operEq(long a , long b) {
        return a == b;
    }
    public static boolean operEq(double a , double b) {
        return a == b;
    }
    public static boolean operEq(boolean a , boolean b) {
        return a == b;
    }
    public static boolean operEq(String a , String b) {
        return a.equals(b);
    }
    public static boolean operEq(Object a , Object b) {
        return a.equals(b);
    }
}

