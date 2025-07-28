import java.io.*;
import java.util.*;

public class CompResult {
    public HashMap < String , String > classes;
    public HashMap < String , AccessMod > classAccess;
    public String startTemplate;
    public String endTemplate;
    public CompResult(HashMap < String , String > classes , HashMap < String , AccessMod > classAccess , String startTemplate , String endTemplate) {
        this . classes = classes;
        this . classAccess = classAccess;
        this . startTemplate = startTemplate;
        this . endTemplate = endTemplate;
    }
    public String constructClassString(String className) {
        var code = classes.getOrDefault(className, "");
        var accessMod = classAccess.getOrDefault(className, AccessMod.DEFAULT);
        if (LangUtil.isTruthy(code.isBlank())) { return ""; }
        var accessModStr = MethodAccess.accessModToString(accessMod);
        var separator = (LangUtil.isTruthy(accessModStr.length() > 0) ? (" ") : (""));
        var out = accessModStr + separator + "class " + className + " {\n";
        out += "    " + code.trim();
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
                out += classes.get(c);
            }
            else if (LangUtil.isTruthy(!Extensions.operEq(c, mainClassName))) {
                out += constructClassString(c);
            }
        }
        return startTemplate + "\n" + out + endTemplate;
    }
}

