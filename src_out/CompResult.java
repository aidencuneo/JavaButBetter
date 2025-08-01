import java.io.*;
import java.util.*;

public class CompResult {
    public HashMap < String , Class > classes;
    public String startTemplate;
    public String endTemplate;
    public CompResult(HashMap < String , Class > classes , String startTemplate , String endTemplate) {
        this . classes = classes;
        this . startTemplate = startTemplate;
        this . endTemplate = endTemplate;
    }
    public String constructClassString(String className) {
        var cl = classes.getOrDefault(className, new Class());
        if (LangUtil.isTruthy(cl.code.isBlank())) { return ""; }
        var accessModStr = MethodAccess.accessModToString(cl.access);
        var separator = LangUtil.isTruthy(accessModStr) ? (" ") : ("");
        var out = accessModStr + separator + "class " + className + " {\n";
        out += "    " + cl.code.trim();
        out += "\n}\n";
        return out;
    }
    public String getCompiledCode(String mainClassName) {
        var out = "";
        if (LangUtil.isTruthy(classes.containsKey(mainClassName))) {
            out += constructClassString(mainClassName);
        }
        for (var c : LangUtil.asIterable(classes.keySet())) {
            if (LangUtil.isTruthy(Extensions.operEq(c, "null"))) {
                out += classes.get(c).code;
            }
            else if (LangUtil.isTruthy(!Extensions.operEq(c, mainClassName))) {
                out += constructClassString(c);
            }
        }
        return startTemplate + "\n" + out + endTemplate;
    }
}

