import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var x = 7;
        var z = 5;
        LangUtil.println(Extensions.operLt(5, Extensions.operSub(x, 1)) && Extensions.operLt(Extensions.operSub(x, 1), 8) && Extensions.operLe(8, Extensions.operAdd(Extensions.operMul(2, z), 1)) && Extensions.operLe(Extensions.operAdd(Extensions.operMul(2, z), 1), 11));
        z = 4;
        x = 6;
        LangUtil.println(Extensions.operLt(5, Extensions.operSub(x, 1)) && Extensions.operLt(Extensions.operSub(x, 1), 8) && Extensions.operLe(8, Extensions.operAdd(Extensions.operMul(2, z), 1)) && Extensions.operLe(Extensions.operAdd(Extensions.operMul(2, z), 1), 11));
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

