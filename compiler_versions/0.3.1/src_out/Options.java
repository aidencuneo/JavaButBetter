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
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(name, "decimal"))) ? (Extensions.operEq(name, "decimal")) : (Extensions.operEq(name, "default_decimal")))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(valueLower, "float"))) ? (Extensions.operEq(valueLower, "float")) : (Extensions.operEq(valueLower, "f")))) {
                defaultDecimal = "f";
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(valueLower, "double"))) ? (Extensions.operEq(valueLower, "double")) : (Extensions.operEq(valueLower, "d")))) {
                defaultDecimal = "d";
            }
        }
    }
}

