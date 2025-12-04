import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        LangUtil.println(Extensions.operAdd(10, "aiden"));
        LangUtil.println(Extensions.operAdd("what", "what"));
        LangUtil.println(Extensions.operMul("w", 10));
        LangUtil.println(Extensions.operMul("aiden", Extensions.operUnarySub(2)));
        LangUtil.println(Extensions.operMul(Extensions.operUnarySub(3), ":aideN"));
    }
}

