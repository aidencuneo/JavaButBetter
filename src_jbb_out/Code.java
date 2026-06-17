import java.io.*;
import java.util.*;

public class Code {
    public static void printmore(Object s, Object s) { LangUtil.println("WHAT", Extensions.operAdd(s, 1), s, s, s, s, s, "END"); }
    public static void main(String[] args) {
        ;
        printmore(1, 2, 3, 4, 5, 6);
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

