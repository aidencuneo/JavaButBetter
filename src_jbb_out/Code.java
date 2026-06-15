import java.io.*;
import java.util.*;

public class Code {
    public static int five() { return (5); }
    public static void main(String[] args) {
        LangUtil.println(five());
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 1));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 2));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 3));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 4));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 5));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 6));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 7));
        LangUtil.println(Extensions.operUshr(LangUtil.listOf(5, 3, 8, 0), 8));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 1));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 2));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 3));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 4));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 5));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 6));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 7));
        LangUtil.println(Extensions.operUshl(LangUtil.listOf(5, 3, 8, 0), 8));
        var a = 16;
        var b = 5.7f;
        LangUtil.println(Extensions.operOr(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4)));
        LangUtil.println(Extensions.operAnd(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4)));
        LangUtil.println(Extensions.operXor(LangUtil.listOf(1, 2, 3), LangUtil.listOf(2, 3, 4)));
        LangUtil.println(Extensions.operOr(142, 15));
        LangUtil.println(Extensions.operXor(142, 15));
        LangUtil.println(Extensions.operBitNot(142), ' ', Extensions.operBitNot(15));
        var ccc = "hi";
        LangUtil.println("Hello, world! I'm {a} + {b}. {ccc}!");
        LangUtil.println(Extensions.operShl(Extensions.operShl("aiden", "blishen"), "cuneo"));
        var lst = Extensions.operShr(LangUtil.listOf(5, 4, 3), 1);
        LangUtil.println(lst);
        Extensions.operShl(Extensions.operShl(Extensions.operShl(lst, 6), 4), 0);
        LangUtil.println(lst);
        lst = Extensions.operAdd(lst, LangUtil.listOf(2, 3));
        LangUtil.println(lst);
        Extensions.operShr(lst, 1);
        LangUtil.println(lst);
        Extensions.operShl(1, lst);
        LangUtil.println(lst);
        LangUtil.println(LangUtil.max(lst), ' ', LangUtil.min(lst), ' ', LangUtil.sum(lst));
    }
}

