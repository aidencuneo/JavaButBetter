import java.io.*;
import java.util.*;

public class Util {
    public static int indexConvert(int index, int size) {
        if (LangUtil.isTruthy(Extensions.operLt(index, 0))) { index = Extensions.operAdd(index, (size)); }
        return index;
    }
    public static T get(ArrayList<T> lst, int index) throws T {
        try {
            index = indexConvert(index, lst.size());
            return lst.get(index);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public static ArrayList<T> select(ArrayList<T> lst, int start, int end) throws T {
        try {
            start = indexConvert(start, lst.size());
            end = indexConvert(end, lst.size());
            return new ArrayList[](lst.subList(start, end));
        }
        catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            return new ArrayList<T>();
        }
    }
    public static ArrayList<T> select(ArrayList<T> lst, int start) throws T {
        return select(lst, start, lst.size());
    }
    public static String select(String str, int start, int end) {
        try {
            start = indexConvert(start, str.length());
            end = indexConvert(end, str.length());
            return str.substring(start, end);
        }
        catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            return "";
        }
    }
    public static String select(String str, int start) {
        return select(str, start, str.length());
    }
    public static String d(ArrayList<T> s) throws T {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(s))) { return "[]"; }
        var out = "[";
        for (var e : LangUtil.asIterable(s)) { out = Extensions.operAdd(out, (Extensions.operAdd(e, " "))); }
        return Extensions.operAdd(out.substring(0, Extensions.operSub(out.length(), 1)), "]");
    }
}

