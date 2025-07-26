import java.io.*;
import java.util.*;

public class LangUtil {
    public static void print(Object s)
    System.out.print ("" + s);
    ;
    public static void println(Object s)
    System.out.println ("" + s);
    ;
    public static boolean isTruthy(boolean v)
    return v;
    ;
    public static boolean isTruthy(Object v)
    return (v != null);
    ;
    public static boolean isTruthy(int v)
    return (v != 0);
    ;
    public static boolean isTruthy(double v)
    return (v != 0);
    ;
    public static boolean isTruthy(String v)
    return ! v.isEmpty ();
    ;
    public static <T> boolean isTruthy(T [] v)
    return v.length > 0;
    ;
    public static boolean isTruthy(List v)
    return ! v.isEmpty ();
    ;
    public static <T> T [] asIterable(T [] v)
    return v;
    ;
    public static <T> List asIterable(int n)
    var lst = new ArrayList < Integer > ();
    ;;
    for (int i = 0; i < n; ++i)
    lst.add (i);
    ;;
    return lst;
}

