import java.io.*;
import java.util.*;

public class Code {
    public static String var = "something123";
    public static String getVar() { return (var); }
    public static String setVar(String value) {
        return (var = value);
    }
    public static void main(String[] args) {
        LangUtil.println(getVar());
        LangUtil.println(setVar("what"));
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
        var b = 7;
        var ccc = Extensions.operAdd(a, b);
        LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Hello, world! I'm {}", a), " + "), b), ". "), ccc), "!"));
        LangUtil.println(Extensions.operShl(Extensions.operShl("aiden", "blishen"), "cuneo"));
        var lst = Extensions.operShr(LangUtil.listOf(5, 4, 3), 1);
        LangUtil.println(lst);
        Extensions.operShl(Extensions.operShl(Extensions.operShl(lst, 6), 4), 0);
        LangUtil.println(lst);
    }
}

