import java.io.*;
import java.util.*;

public class Util {
    public static <T> T get(ArrayList<T> lst, int index) {
        try {
            if (index < 0)
                return lst.get(lst.size() + index);
            return lst.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <T> String d(ArrayList<T> s) {
        if (s.size() == 0)
            return "[]";

        String out = "[";

        for (int i = 0; i < s.size(); ++i)
            out += s.get(i) + " ";

        return out.substring(0, out.length() - 1) + "]";
    }
}
