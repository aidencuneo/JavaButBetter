import java.io.*;
import java.util.*;

public class LangUtil {
    public static void print(Object s) {
        System.out.print("" + s);
    }

    public static void println(Object s) {
        System.out.println("" + s);
    }

    public static <T> String d(ArrayList<T> s) {
        String out = "[";

        for (int i = 0; i < s.size(); ++i)
            out += s.get(i) + ", ";

        return out.substring(0, out.length() - 2) + "]";
    }
}
