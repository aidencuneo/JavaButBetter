import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var empty = List.of();
        var lst = List.of(1, 2, 3, "hello");
        var ints = List.of(0, 1, 2, 3);
        LangUtil.println(LangUtil.slice(ints, 0, - 1, 1), LangUtil.slice(ints, null, null, - 1), LangUtil.slice(ints, null, null, 1), LangUtil.slice(ints, null, 2, - 1), LangUtil.slice(ints, 1, null, 1));
        LangUtil.println(Extensions.operEq("69", "69"));
    }
}

