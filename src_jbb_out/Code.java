import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var a = 16;
        var b = 5.7f;
        var ccc = "hi";
        LangUtil.println("Hello, world! I'm {a} + {b}. {ccc}!");
        LangUtil.exit();
        var lst = List.of(5, 4, 3);
        LangUtil.println(lst);
        LangUtil.println(LangUtil.max(lst), LangUtil.min(lst), LangUtil.sum(lst));
    }
}

