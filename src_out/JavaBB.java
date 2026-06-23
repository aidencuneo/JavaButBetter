import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.file.StandardWatchEventKinds;

public class JavaBB {
    public static void main(String[] args) {
        var verbose = false;
        var watch = false;
        var argv = new ArrayList<String>();
        for (var arg : LangUtil.asIterable(args)) {
            if (LangUtil.isTruthy(Extensions.operIn(arg, LangUtil.listOf("-v", "--version")))) {
                LangUtil.println(Extensions.operAdd("JavaButBetter ", "v0.6.0"));
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
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("--- JavaButBetter ", "v0.6.0"), " ---")); }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Compiling directory \"", compDir), "\" to \""), outDir), "\"")); }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdir();
        }
        var files = new File(compDir).list();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling Extensions..."); }
        var extensionsClassRes = ExtensionsCode.get();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nPrecompiling ", compDir), "...")); }
        var fileContentMap = new HashMap<String, String>();
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
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("\n\nCompiling ", compDir), "...")); }
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
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\n\nCompiling LangUtil..."); }
        var res = LangUtilCode.get();
        var langUtilCode = res.getCompiledCode("LangUtil");
        try (var writer = new PrintWriter(Extensions.operAdd(outDir, "/LangUtil.java"))) {
            new File(Extensions.operAdd(outDir, "/LangUtil.java")).createNewFile();
            writer.println(langUtilCode);
        }
        catch (IOException e) {
            
        }
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
                            extensionsClassRes = ExtensionsCode.get();
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
                                res = Compiler.compileFile(className, fileContent);
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
                            try (var writer = new PrintWriter(toPath)) {
                                new File(toPath).createNewFile();
                                writer.println(compiled);
                            }
                            catch (IOException e) {
                                
                            }
                            try (var writer = new PrintWriter(Extensions.operAdd(outDir, "/Extensions.java"))) {
                                new File(Extensions.operAdd(outDir, "/Extensions.java")).createNewFile();
                                writer.println(extensionsClassRes.getCompiledCode("Extensions"));
                            }
                            catch (IOException e) {
                                
                            }
                        }
                    }
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(key.reset()))) { break; }
                }
                LangUtil.exit();
            }
            catch (Exception e) {
                
            }
        }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\n\nDone."); }
    }
}

