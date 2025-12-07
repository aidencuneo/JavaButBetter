import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String[] args) {
        LangUtil.println(DynamicCode.get());
        System.exit(0);
        var compDir = LangUtil.isTruthy(args) ? (Extensions.operGetIndex(args, 0)) : ("src");
        var outDir = LangUtil.isTruthy(Extensions.len(args) > 1) ? (Extensions.operGetIndex(args, 1)) : (Extensions.operAdd(compDir, "_out"));
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdir();
        }
        var files = new File(compDir).list();
        LangUtil.println("\nCompiling Extensions...");
        var extensionsClassRes = ExtensionsCode.get();
        LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nPrecompiling ", compDir), "..."));
        HashMap<String, String> fileContentMap = new HashMap<>();
        for (var i : LangUtil.asIterable(Extensions.len(files))) {
            var fileContent = "";
            var fromPath = Extensions.operAdd(Extensions.operAdd(compDir, "/"), Extensions.operGetIndex(files, i));
            var className = Extensions.operGetIndex(Extensions.operGetIndex(files, i).split("\\."), 0);
            try (var scanner = new Scanner(new File(fromPath))) {
                fileContent = scanner.useDelimiter("\\Z").next();
            }
            catch (FileNotFoundException e) {
                
            }
            if (LangUtil.isTruthy(Extensions.operGetIndex(files, i).endsWith(".jbb"))) {
                var lines = Precompiler.precompileFile(className, fileContent);
                fileContent = String.join("\n", lines);
                fileContentMap.put(Extensions.operGetIndex(files, i), fileContent);
            }
        }
        LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nCompiling ", compDir), "..."));
        for (var i : LangUtil.asIterable(Extensions.len(files))) {
            var fromPath = Extensions.operAdd(Extensions.operAdd(compDir, "/"), Extensions.operGetIndex(files, i));
            var toPath = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(outDir, "/"), Extensions.operGetIndex(Extensions.operGetIndex(files, i).split("\\."), 0)), ".java");
            var className = Extensions.operGetIndex(Extensions.operGetIndex(files, i).split("\\."), 0);
            var fileContent = Extensions.operGetIndex(fileContentMap, Extensions.operGetIndex(files, i));
            fileContent = Precompiler.applyRegexRules(fileContent);
            var compiled = "";
            if (LangUtil.isTruthy(Extensions.operGetIndex(files, i).endsWith(".jbb"))) {
                var res = Compiler.compileFile(className, fileContent);
                if (LangUtil.isTruthy(Extensions.operIn("Extensions", res.classes))) {
                    var newCode = Extensions.operGetIndex(res.classes, "Extensions").code;
                    var extensionsClass = Extensions.operGetIndex(extensionsClassRes.classes, "Extensions");
                    extensionsClass.code = extensionsClass.code + newCode;
                    res.classes.remove("Extensions");
                }
                compiled = res.getCompiledCode(className);
            }
            else {
                compiled = fileContent;
                toPath = Extensions.operAdd(Extensions.operAdd(outDir, "/"), Extensions.operGetIndex(files, i));
            }
            try (var writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            }
            catch (IOException e) {
                
            }
        }
        try (var writer = new PrintWriter(Extensions.operAdd(outDir, "/Extensions.java"))) {
            new File(Extensions.operAdd(outDir, "/Extensions.java")).createNewFile();
            writer.println(extensionsClassRes.getCompiledCode("Extensions"));
        }
        catch (IOException e) {
            
        }
        LangUtil.println("\n\nCompiling LangUtil...");
        var res = LangUtilCode.get();
        var langUtilCode = res.getCompiledCode("LangUtil");
        try (var writer = new PrintWriter(Extensions.operAdd(outDir, "/LangUtil.java"))) {
            new File(Extensions.operAdd(outDir, "/LangUtil.java")).createNewFile();
            writer.println(langUtilCode);
        }
        catch (IOException e) {
            
        }
        LangUtil.println("\n\nDone.");
    }
}

