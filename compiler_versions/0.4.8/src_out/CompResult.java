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
    public String getCompiledCode(String mainClassName) {
        var out = "";
        if (LangUtil.isTruthy(classes.containsKey(mainClassName))) {
            out += classes.get(mainClassName);
        }
        for (var c : LangUtil.asIterable(classes)) {
            if (LangUtil.isTruthy(Extensions.operEq(c, "null"))) {
                out += classes.get(c).code;
            }
            else if (LangUtil.isTruthy(!Extensions.operEq(c, mainClassName))) {
                out += classes.get(c);
            }
        }
        var packageStr = "";
        if (LangUtil.isTruthy(Compiler.packagePath)) {
            packageStr = Extensions.operAdd(Extensions.operAdd("package ", Compiler.packagePath), ";\n\n");
        }
        return Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(packageStr, startTemplate), "\n"), out), endTemplate);
    }
}

