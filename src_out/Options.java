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
        var valueLower = value.toLowerCase();
        if (LangUtil.isTruthy((LangUtil.isTruthy(name.equals("decimal"))) ? (name.equals("decimal")) : (name.equals("default_decimal")))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(valueLower.equals("float"))) ? (valueLower.equals("float")) : (valueLower.equals("f")))) {
                defaultDecimal = "f";
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(valueLower.equals("double"))) ? (valueLower.equals("double")) : (valueLower.equals("d")))) {
                defaultDecimal = "d";
            }
        }
    }
}

