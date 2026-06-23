import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var age = 10;
        if (!LangUtil.isTruthy(Extensions.operLt(age, 18))) { LangUtil.println(Extensions.operAdd("yeah", age)); }
        age = 18;
        if (!LangUtil.isTruthy(Extensions.operLt(age, 18))) { LangUtil.println(Extensions.operAdd("yeah", age)); }
        age = 17;
        if (!LangUtil.isTruthy(Extensions.operLt(age, 18))) { LangUtil.println(Extensions.operAdd("yeah", age)); }
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

