import java.io.*;
import java.util.*;

public class CompResult {
    public HashMap<String, Class> classes;
    public String startTemplate;
    public String endTemplate;
    public HashMap<Integer, Integer> lineMap;
    public CompResult(HashMap<String, Class> classes, String startTemplate, String endTemplate, HashMap<Integer, Integer> lineMap) {
        this.classes = classes;
        this.startTemplate = startTemplate;
        this.endTemplate = endTemplate;
        this.lineMap = lineMap;
    }
    public String getCompiledCode(String mainClassName) {
        var out = "";
        if (LangUtil.isTruthy(Extensions.operIn(mainClassName, classes))) {
            out = Extensions.operAdd(out, (Extensions.operGetIndex(classes, mainClassName)));
        }
        for (var c : LangUtil.asIterable(classes)) {
            if (LangUtil.isTruthy(Extensions.operEq(c, "null"))) {
                out = Extensions.operAdd(out, (Extensions.operGetIndex(classes, c).code));
            }
            else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(c, mainClassName)))) {
                out = Extensions.operAdd(out, (Extensions.operGetIndex(classes, c)));
            }
        }
        var packageStr = "";
        if (LangUtil.isTruthy(Compiler.packagePath)) {
            packageStr = Extensions.operAdd(Extensions.operAdd("package ", Compiler.packagePath), ";\n\n");
        }
        return Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(packageStr, startTemplate), "\n"), out), endTemplate);
    }
    public String getLineMapJSON() {
        var offset = Extensions.operAdd(startTemplate.lines().count(), 1);
        var out = "{";
        for (var jbbLine : LangUtil.asIterable(lineMap)) {
            var javaLine = Extensions.operAdd(offset, Extensions.operGetIndex(lineMap, jbbLine));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("\"", jbbLine), "\": "), javaLine), ", ")));
        }
        while (LangUtil.isTruthy(out.endsWith(", "))) { out = LangUtil.slice(out, null, Extensions.operUnarySub(2), 1); }
        return Extensions.operAdd(out, "}");
    }
}

