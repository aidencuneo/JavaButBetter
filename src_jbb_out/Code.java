import java.io.*;
import java.util.*;

public class Code {
    public static void printmore(Object s, Object s2) { LangUtil.println("WHAT", s, s, s, s, s, s, "END"); }
    public static int add(int a, int b) { return (Extensions.operAdd(a, b)); }
    public static void main(String[] args) {
        LangUtil.println();
        LangUtil.println(Extensions.operIn(Extensions.operUnarySub(1238234), LangUtil.range(Integer.MIN_VALUE, null, null)));
        LangUtil.println(Extensions.operIn(138234, LangUtil.range(0, null, null)));
        LangUtil.println();
        LangUtil.println(Extensions.operIn(5, LangUtil.range(1, 5, null)));
        LangUtil.println(LangUtil.range(1, 5, null));
        LangUtil.println(Extensions.operIn(10, LangUtil.range(0, 1000, 2)));
        LangUtil.println(Extensions.operIn(11, LangUtil.range(0, 1000, 2)));
        LangUtil.println(LangUtil.range(0, 1000, 2));
        printmore((Extensions.operAnd(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4))), "56???");
        printmore(LangUtil.listOf(1, 2), LangUtil.listOf(3, 4, 5, 6));
        Extensions.operAdd(Extensions.operAdd(5, add(5, 4)), 6);
        LangUtil.println(Extensions.operAnd(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4)));
        LangUtil.println(Extensions.operXor(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4)));
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

