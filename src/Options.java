import java.io.*;
import java.util.*;

public class Options {
    public static String defaultDecimal;
    public static void reset() {
        defaultDecimal = "f";
    }
    static {
        reset();
    }
    public static void setOption(String name , String value) {
        name = name.toLowerCase();
        String valueLower = value.toLowerCase();
        if (LangUtil.isTruthy((name.equals("decimal") | | name.equals("default_decimal")))) {
            if (LangUtil.isTruthy((valueLower.equals("float") | | valueLower.equals("f")))) {
                defaultDecimal = "f";
            }
            else (LangUtil.isTruthy(if(valueLower.equals("double") | | valueLower.equals("d")))) {
                defaultDecimal = "d";
            }
        }
    }
}

