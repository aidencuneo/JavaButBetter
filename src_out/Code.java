import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var a = "String1";
        var b = "String2";
        if (LangUtil.isTruthy(( == b))) {
            System.out.println("Hello, World!");
        }
        if (LangUtil.isTruthy(( == b.substring(0 , b.size() - 1)))) {
            System.out.println("No");
        }
    }
}

