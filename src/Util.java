import java.io.*;
import java.util.*;

public class Util {
    public static int indexConvert(int index, int size) {
        if (index < 0)
            index += size;
        return index;
    }

    public static <T> T get(ArrayList<T> lst, int index) {
        try {
            index = indexConvert(index, lst.size());
            return lst.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <T> ArrayList<T> select(ArrayList<T> lst, int start, int end) {
        try {
            start = indexConvert(start, lst.size());
            end = indexConvert(end, lst.size());

            return new ArrayList<>(lst.subList(start, end));
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <T> ArrayList<T> select(ArrayList<T> lst, int start) {
        return select(lst, start, lst.size());
    }

    public static <T> String d(ArrayList<T> s) {
        if (s.isEmpty())
            return "[]";

        String out = "[";

        for (int i = 0; i < s.size(); ++i)
            out += s.get(i) + " ";

        return out.substring(0, out.length() - 1) + "]";
    }
}
