import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var a = 16;
        var b = 5.7f;
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

