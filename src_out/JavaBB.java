import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.file.StandardWatchEventKinds;

class JavaBB {
    public static HashMap<String, String> codeMap = new HashMap<>();
    public static CompResult extensionsRes = null;
    public static CompResult langUtilRes = null;
    public static boolean writeTo(String path, String text) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(path).getParentFile().exists()))) {
            new File(path).getParentFile().mkdirs();
        }
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
        catch (FileNotFoundException | NoSuchElementException e) {
            return "";
        }
    }
    public static void readAllToMap(String folder) {
        for (var name : LangUtil.asIterable(new File(folder).list())) {
            var path = Extensions.operAdd(Extensions.operAdd(folder, "/"), name);
            if (LangUtil.isTruthy(new File(path).isDirectory())) {
                readAllToMap(path);
            }
            else {
                codeMap.put(path, readFile(path));
            }
        }
    }
    public static void precompileAll() {
        for (var path : LangUtil.asIterable(codeMap)) {
            var className = Extensions.operGetIndex(path.split("\\."), 0);
            codeMap.put(path, Precompiler.precompileFile(className, Extensions.operGetIndex(codeMap, path)));
        }
    }
    public static void compileAndWriteAll(String compDir, String outDir) {
        for (var path : LangUtil.asIterable(codeMap)) {
            var name = new File(path).getName();
            var barePath = LangUtil.slice(path, Extensions.len(compDir), null, 1);
            var barePathName = Extensions.operGetIndex(barePath.split("\\."), 0);
            var toPath = Extensions.operAdd(Extensions.operAdd(outDir, "/"), barePath);
            var className = Extensions.operGetIndex(name.split("\\."), 0);
            var code = Extensions.operGetIndex(codeMap, path);
            if (LangUtil.isTruthy(name.endsWith(".jbb"))) {
                code = Precompiler.applyRegexRules(code, "jbb");
                var res = Compiler.compileFile(className, code);
                if (LangUtil.isTruthy(Extensions.operIn("Extensions", res.classes))) {
                    var newCode = Extensions.operGetIndex(res.classes, "Extensions").code;
                    var extensionsClass = Extensions.operGetIndex(extensionsRes.classes, "Extensions");
                    extensionsClass.code = Extensions.operAdd(extensionsClass.code, (newCode));
                    res.classes.remove("Extensions");
                }
                code = res.getCompiledCode(className);
                code = Precompiler.applyRegexRules(code, "java");
                toPath = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(outDir, "/"), barePathName), ".java");
            }
            writeTo(toPath, code);
        }
    }
    public static void main(String[] args) {
        var verbose = false;
        var watch = false;
        var argv = new ArrayList<String>();
        for (var arg : LangUtil.asIterable(args)) {
            if (LangUtil.isTruthy(Extensions.operIn(arg, LangUtil.listOf("-v", "--version")))) {
                LangUtil.println(Extensions.operAdd("JavaButBetter ", "v0.7.2"));
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
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("--- JavaButBetter ", "v0.7.2"), " ---")); }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Compiling directory \"", compDir), "\" to \""), outDir), "\"")); }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdirs();
        }
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling Extensions..."); }
        extensionsRes = ExtensionsCode.get();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling LangUtil..."); }
        langUtilRes = LangUtilCode.get();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println(Extensions.operAdd(Extensions.operAdd("\nReading all files in ", compDir), "...")); }
        readAllToMap(compDir);
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nPrecompiling all files..."); }
        precompileAll();
        if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nCompiling all files..."); }
        compileAndWriteAll(compDir, outDir);
        writeTo(Extensions.operAdd(outDir, "/Extensions.java"), extensionsRes.getCompiledCode("Extensions"));
        writeTo(Extensions.operAdd(outDir, "/LangUtil.java"), langUtilRes.getCompiledCode("LangUtil"));
        if (LangUtil.isTruthy(!LangUtil.isTruthy(watch))) {
            if (LangUtil.isTruthy(verbose)) { LangUtil.println("\nDone."); }
            return;
        }
        while (LangUtil.isTruthy(true)) {
            try {
                var watcher = FileSystems.getDefault().newWatchService();
                Path.of(compDir).register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
                LangUtil.println(Extensions.operAdd(Extensions.operAdd("\nWatching \"", compDir), "\"\n"));
                while (LangUtil.isTruthy(true)) {
                    var key = watcher.take();
                    for (var event : LangUtil.asIterable(key.pollEvents())) {
                        var kind = Extensions.operAdd("", event.kind());
                        var context = event.context();
                        LangUtil.println(Extensions.operAdd(Extensions.operAdd(kind, ": "), context));
                        if (LangUtil.isTruthy(Extensions.operIn(event.kind(), LangUtil.listOf(StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY)))) {
                            var name = Extensions.operAdd("", (((Path) context)));
                            extensionsRes = ExtensionsCode.get();
                            var fromPath = Extensions.operAdd(Extensions.operAdd(compDir, "/"), name);
                            var toPath = Extensions.operAdd(Extensions.operAdd(outDir, "/"), name);
                            var className = Extensions.operGetIndex(name.split("\\."), 0);
                            var code = readFile(fromPath);
                            if (LangUtil.isTruthy(name.endsWith(".jbb"))) {
                                code = Precompiler.precompileFile(className, code);
                                code = Precompiler.applyRegexRules(code, "jbb");
                                var res = Compiler.compileFile(className, code);
                                if (LangUtil.isTruthy(Extensions.operIn("Extensions", res.classes))) {
                                    var newCode = Extensions.operGetIndex(res.classes, "Extensions").code;
                                    var extensionsClass = Extensions.operGetIndex(extensionsRes.classes, "Extensions");
                                    extensionsClass.code = Extensions.operAdd(extensionsClass.code, (newCode));
                                    res.classes.remove("Extensions");
                                }
                                code = res.getCompiledCode(className);
                                code = Precompiler.applyRegexRules(code, "java");
                                toPath = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(outDir, "/"), className), ".java");
                            }
                            writeTo(toPath, code);
                            writeTo(Extensions.operAdd(outDir, "/Extensions.java"), extensionsRes.getCompiledCode("Extensions"));
                        }
                    }
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(key.reset()))) { break; }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

