import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        LangUtil.println(Extensions.operGetIndex((new HashMap<>(Map.ofEntries(Map.entry(5, (new HashMap<>(Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3))))), Map.entry(10, (new HashMap<>(Map.ofEntries(Map.entry("z", 3), Map.entry("y", 2), Map.entry("x", 1)))))))), 5));
        var dict = (new HashMap<>(Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2), Map.entry("c", 3))));
        LangUtil.println(dict);
        var ccc = "hi";
        LangUtil.println("Hello, world! I'm {a} + {b}. {ccc}!");
        LangUtil.println(Extensions.operShl(Extensions.operShl("aiden", "blishen"), "cuneo"));
        var lst = Extensions.operShr(LangUtil.listOf(5, 4, 3), 1);
        LangUtil.println(lst);
        Extensions.operShl(Extensions.operShl(Extensions.operShl(lst, 6), 4), 0);
        LangUtil.println(lst);
        LangUtil.println(LangUtil.max(lst), ' ', LangUtil.min(lst), ' ', LangUtil.sum(lst));
    }
}

