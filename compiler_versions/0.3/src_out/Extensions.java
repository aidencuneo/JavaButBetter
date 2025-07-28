import java.io.*;
import java.util.*;

public class Extensions {
    public static int operAdd(int a , int b) {
        return a + b;
    }
    public static long operAdd(long a , long b) {
        return a + b;
    }
    public static double operAdd(double a , double b) {
        return a + b;
    }
    public static int operAdd(boolean a , boolean b) {
        return (a ? 1 : 0) + (b ? 1 : 0);
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

