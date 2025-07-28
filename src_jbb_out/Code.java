import java.io.*;
import java.util.*;

public class Code {
    public static HashMap < String , Integer > name1;
    public static HashMap < String , Integer > name2 = new HashMap < String , Integer > ();
    public static <T> HashMap < String , Integer > name3() {
        return null;
    }
     static void main(String[] args) {
        LangUtil.println(1 > 2 ? 1 : 2 + 10);
        if (LangUtil.isTruthy((LangUtil.isTruthy("something")) ? ("h" > "w" ? "h" : "w") : ("something"))) { 5 < 6.9f ? 5 : 6.9f < 2 ? 6.9f : 2; }
        var ages = new HashMap < String , Integer > ();
        HashMap < String , Integer > ages2 = new HashMap < String , Integer > ();
    }
    public static <T> HashMap < String , String > method(String [] args) {
        return null;
    }
}

