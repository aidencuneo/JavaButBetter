import java.io.*;
import java.util.*;

public class CompResult {
    public LinkedHashMap<String, Class> classes;
    public String startTemplate;
    public String endTemplate;
    public LinkedHashMap<Integer, Integer> lineMap;
    public CompResult(LinkedHashMap<String, Class> classes, String startTemplate, String endTemplate, LinkedHashMap<Integer, Integer> lineMap) {
        this.classes = classes;
        this.startTemplate = startTemplate;
        this.endTemplate = endTemplate;
        this.lineMap = lineMap;
    }
    public boolean hasCode() {
        for (var c : LangUtil.asIterable(classes)) { if (LangUtil.isTruthy(Extensions.operGetIndex(classes, c).code)) { return true; } }
        return false;
    }
    public String getCompiledCode() {
        var out = "";
        for (var c : LangUtil.asIterable(classes)) {
            if (LangUtil.isTruthy(Extensions.operEq(c, "null"))) {
                out = Extensions.operAdd(out, (Extensions.operGetIndex(classes, c).code));
            }
            else if (LangUtil.isTruthy(Extensions.operGetIndex(classes, c).code.strip())) {
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
        var out = "{";
        for (var jbbLine : LangUtil.asIterable(lineMap)) {
            var javaLine = Extensions.operGetIndex(lineMap, jbbLine);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("\"", (jbbLine)), "\": "), (javaLine)), ", ")));
        }
        while (LangUtil.isTruthy(out.endsWith(", "))) { out = LangUtil.slice(out, null, Extensions.operUnarySub(2), 1); }
        return Extensions.operAdd(out, "}");
    }
}

