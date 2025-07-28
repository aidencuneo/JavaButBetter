import java.io.*;
import java.util.*;

public class Util {
    public static Integer indexConvert(Integer index , Integer size) {
        if (LangUtil.isTruthy(index < 0)) { index += size; }
        return index;
    }
    public static < T > T get(ArrayList < T > lst , Integer index) {
        try {
            index = indexConvert(index , lst.size());
            return lst.get(index);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    public static < T > ArrayList < T > select(ArrayList < T > lst , Integer start , Integer end) {
        try {
            start = indexConvert(start , lst.size());
            end = indexConvert(end , lst.size());
            return new ArrayList < > (lst.subList(start , end));
        }
        catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            return new ArrayList < T > ();
        }
    }
    public static < T > ArrayList < T > select(ArrayList < T > lst , Integer start) {
        return select(lst , start , lst.size());
    }
    public static String select(String str , Integer start , Integer end) {
        try {
            start = indexConvert(start , str.length());
            end = indexConvert(end , str.length());
            return str.substring(start , end);
        }
        catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            return "";
        }
    }
    public static String select(String str , Integer start) {
        return select(str , start , str.length());
    }
    public static < T > String d(ArrayList < T > s) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(s))) { return "[]"; }
        var out = "[";
        for (var e : LangUtil.asIterable(s)) { out += e + " "; }
        return out.substring(0 , out.length() - 1) + "]";
    }
}

