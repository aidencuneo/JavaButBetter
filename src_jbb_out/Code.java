import java.io.*;
import java.util.*;

public class Code {
    public static HashMap<String, Integer> hs = (new HashMap<>(Map.ofEntries()));
    public static HashMap<String, Integer> hs2 = (new HashMap<>(Map.ofEntries()));
    public static HashMap<String, Integer> hs3 = (new HashMap<>(Map.ofEntries()));
    public static HashMap<Boolean, Boolean> hs4 = (new HashMap<>(Map.ofEntries()));
    public static HashMap<ArrayList<Boolean>, HashMap<Double, Float>> hs5 = (new HashMap<>(Map.ofEntries()));
    public static String s = "";
    public static int i = 0;
    public static float f = 0f;
    public static boolean b = false;
    public static double d = 0.0f;
    public static void main(String[] args) {
        LangUtil.println(Extensions.operGetIndex((new HashMap<>(Map.ofEntries(Map.entry(5, (new HashMap<>(Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3))))), Map.entry(10, (new HashMap<>(Map.ofEntries(Map.entry("z", 3), Map.entry("y", 2), Map.entry("x", 1)))))))), 5));
        var dict = (new HashMap<>(Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3))));
        LangUtil.println(dict);
        Extensions.operSetIndex(dict, "d", 4);
        LangUtil.println(Extensions.operGetIndex(dict, "d"));
        LangUtil.println(dict);
        var c = LangUtil.listOf(1, 2, 3);
        Extensions.operSetIndex(c, 0, 5);
        LangUtil.println(c);
        var a = 5;
        b = 7;
        var ccc = Extensions.operAdd(a, b);
        LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Hello, world! I'm {}", (a)), " + "), (b)), ". "), (ccc)), "!"));
        LangUtil.println(Extensions.operShl(Extensions.operShl("aiden", "blishen"), "cuneo"));
    }
}

