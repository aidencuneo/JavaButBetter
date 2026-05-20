import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        LangUtil.println(Arrays.toString(Dynamic.registry.keySet().toArray()));
        
        var a = Dynamic.call("operAdd", 1.5d, 2.0d);
        LangUtil.println(a);
        LangUtil.callMethod(LangUtil.getField(System.class, "out"), "println", new Object[] {LangUtil.callMethod(Math.class, "round", new Object[] {a})});
    }
}

