import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        ArrayList<Token> exprTok = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
        exprTok [0] = 5;
        a.b = c;
        Type a.b = c;
        Type c = d;
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

