import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.file.StandardWatchEventKinds;

class JavaBB {
    public static boolean writeTo(String path, String text) {
        try (var writer = new PrintWriter(path)) {
            new File(path).createNewFile();
            writer.println(text);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    public static String readFile(String path) {
        try (var scanner = new Scanner(new File(path))) {
            return scanner.useDelimiter("\\Z").next();
        }
        catch (FileNotFoundException e) {
            return "";
        }
    }
    public static void main(String[] args) {
        var verbose = false;
        var watch = false;
        var argv = new ArrayList<String>();
        for (var arg : LangUtil.asIterable(args)) {
            if (LangUtil.isTruthy(Extensions.operIn(arg, LangUtil.listOf("-v", "--version")))) {
                LangUtil.println(Extensions.operAdd("JavaButBetter ", "v0.6.1"));
                LangUtil.exit();
            }
            else if (LangUtil.isTruthy(Extensions.operIn(arg, LangUtil.listOf("-V", "--verbose")))) {
                verbose = true;
            }
            else if (LangUtil.isTruthy(Extensions.operIn(arg, LangUtil.listOf("-W", "--watch")))) {
                watch = true;
            }
            else {
                argv = Extensions.operAdd(argv, LangUtil.listOf(arg));
            }
        }
        var compDir = LangUtil.isTruthy(argv) ? (Extensions.operGetIndex(argv, 0)) : ("src");
        var outDir = LangUtil.isTruthy(Extensions.operGt(Extensions.len(argv), 1)) ? (Extensions.operGetIndex(argv, 1)) : (Extensions.operAdd(compDir, "_out"));
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("--- JavaButBetter ", "v0.6.1"), " ---")); }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Compiling directory \"", compDir), "\" to \""), outDir), "\"")); }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdir();
        }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling Extensions..."); }
        var extensionsRes = ExtensionsCode.get();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling LangUtil..."); }
        var langUtilRes = LangUtilCode.get();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nPrecompiling ", compDir), "...")); }
        var codeMap = new HashMap<String, String>();
        var files = new File(compDir).list();
        for (var name : LangUtil.asIterable(files)) {
            var path = Extensions.operAdd(Extensions.operAdd(compDir, "/"), name);
            var className = Extensions.operGetIndex(name.split("\\."), 0);
            var code = readFile(path);
            if (LangUtil.isTruthy(name.endsWith(".jbb"))) {
                codeMap.put(path, Precompiler.precompileFile(className, code));
            }
        }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nCompiling ", compDir), "...")); }
        for (var name : LangUtil.asIterable(files)) {
            var fromPath = Extensions.operAdd(Extensions.operAdd(compDir, "/"), name);
            var toPath = Extensions.operAdd(Extensions.operAdd(outDir, "/"), name);
            var className = Extensions.operGetIndex(name.split("\\."), 0);
            var code = Precompiler.applyRegexRules(Extensions.operGetIndex(codeMap, fromPath));
            if (LangUtil.isTruthy(name.endsWith(".jbb"))) {
                var res = Compiler.compileFile(className, code);
                if (LangUtil.isTruthy(Extensions.operIn("Extensions", res.classes))) {
                    var newCode = Extensions.operGetIndex(res.classes, "Extensions").code;
                    var extensionsClass = Extensions.operGetIndex(extensionsRes.classes, "Extensions");
                    extensionsClass.code = Extensions.operAdd(extensionsClass.code, (newCode));
                    res.classes.remove("Extensions");
                }
                code = res.getCompiledCode(className);
                toPath = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(outDir, "/"), className), ".java");
            }
            writeTo(toPath, code);
        }
        writeTo(Extensions.operAdd(outDir, "/Extensions.java"), extensionsRes.getCompiledCode("Extensions"));
        writeTo(Extensions.operAdd(outDir, "/LangUtil.java"), langUtilRes.getCompiledCode("LangUtil"));
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\n\nDone."); }
        if (LangUtil.isTruthy(watch)) {
            try {
                var watcher = FileSystems.getDefault().newWatchService();
                Path.of(compDir).register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                LangUtil.println(Extensions.operAdd(Extensions.operAdd("Watching \"", compDir), "\"\n"));
                while (LangUtil.isTruthy(true)) {
                    var key = watcher.take();
                    for (var event : LangUtil.asIterable(key.pollEvents())) {
                        var kind = Extensions.operAdd("", event.kind());
                        var context = event.context();
                        LangUtil.println(Extensions.operAdd(Extensions.operAdd(kind, ": "), context));
                        if (LangUtil.isTruthy(Extensions.operIn(event.kind(), LangUtil.listOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY)))) {
                            var filename = Extensions.operAdd("", (((Path) context)));
                            var extensionsClassRes = ExtensionsCode.get();
                            var fromPath = Extensions.operAdd(Extensions.operAdd(compDir, "/"), filename);
                            var toPath = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(outDir, "/"), Extensions.operGetIndex(filename.split("\\."), 0)), ".java");
                            var className = Extensions.operGetIndex(filename.split("\\."), 0);
                            var fileContent = "";
                            try (var scanner = new Scanner(new File(fromPath))) {
                                fileContent = scanner.useDelimiter("\\Z").next();
                            }
                            catch (FileNotFoundException e) {
                                
                            }
                            if (LangUtil.isTruthy(filename.endsWith(".jbb"))) {
                                var lines = Precompiler.precompileFile(className, fileContent);
                                fileContent = String.join("\n", lines);
                            }
                            fileContent = Precompiler.applyRegexRules(fileContent);
                            var compiled = "";
                            if (LangUtil.isTruthy(filename.endsWith(".jbb"))) {
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
                                toPath = Extensions.operAdd(Extensions.operAdd(outDir, "/"), filename);
                            }
                            writeTo(toPath, compiled);
                            writeTo(Extensions.operAdd(outDir, "/Extensions.java"), extensionsClassRes.getCompiledCode("Extensions"));
                        }
                    }
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(key.reset()))) { break; }
                }
                LangUtil.exit();
            }
            catch (Exception e) {
                
            }
        }
    }
}

