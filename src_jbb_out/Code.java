import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var lst = new ArrayList < Integer > ();
        lst.add(5);
        if (LangUtil.isTruthy((LangUtil.isTruthy(lst)) ? (lst.get(0)) : (lst))) {
            LangUtil.println("yes");
        }
        lst.remove(0);
        if (LangUtil.isTruthy((LangUtil.isTruthy(lst)) ? (lst.get(0)) : (lst))) {
            LangUtil.println("yes?");
        }
        LangUtil.println(lst);
    }
}

