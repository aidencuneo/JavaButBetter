import java.io.*;
import java.util.*;

public class Code {
    public int field = 10;
    public int age = 21;
    public String name = "aiden";
    public String name2 = "value";
    public static void main(String[] args) {
        var some1000_a = (Math.pow(1.0f, 1.0f) * 2 + 5 - 1);
        System.out.println("hello" + ", " + 10);
        if (LangUtil.isTruthy(List.of(1 , 2 , "hi"))) { System.out.println("nice"); }
        if (LangUtil.isTruthy(List.of(1))) { System.out.println("yeah"); }
        for (var i : LangUtil.asIterable(10)) { System.out.println(i); }
        for (var c : LangUtil.asIterable("hello\n")) { System.out.print(0 + c + " "); }
        var n = 20;
        for (var i : LangUtil.asIterable(n)) {
            System.out.println(i);
            System.out.println(i * 2);
        }
    }
    public String _get_name() {
        return name.toUpperCase();
    }
}

