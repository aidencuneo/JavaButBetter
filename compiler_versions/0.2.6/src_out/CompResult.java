import java.io.*;
import java.util.*;

public class CompResult {
    HashMap<String, String> classes;
    HashMap<String, AccessMod> classAccess;
    public String startTemplate;
    public String endTemplate;
    public CompResult(HashMap < String , String > classes , HashMap < String , AccessMod > classAccess , String startTemplate , String endTemplate) {
        this . classes = classes;
        this . classAccess = classAccess;
        this . startTemplate = startTemplate;
        this . endTemplate = endTemplate;
    }
    public String constructClassString(String className) {
        String code = classes.getOrDefault(className , "");
        AccessMod accessMod = classAccess.getOrDefault(className , AccessMod.DEFAULT);
        if (LangUtil.isTruthy(code.isBlank())) { return ""; }
        String accessModStr = MethodAccess.accessModToString(accessMod);
        String separator = (accessModStr.length() > 0 ? " " : "");
        String out = accessModStr + separator + "class " + className + " {\n";
        out += "    " + code.trim();
        out += "\n}\n";
        return out;
    }
    public String getCompiledCode(String mainClassName) {
        String out = "";
        if (LangUtil.isTruthy(classes.containsKey(mainClassName))) {
            out += constructClassString(mainClassName);
        }
        for (var c : LangUtil.asIterable(classes.keySet())) {
            if (LangUtil.isTruthy(c.equals("null"))) {
                out += classes.get(c);
            }
            else if (LangUtil.isTruthy(!LangUtil.isTruthy(c.equals(mainClassName)))) {
                out += constructClassString(c);
            }
        }
        return startTemplate + "\n" + out + endTemplate;
    }
}

