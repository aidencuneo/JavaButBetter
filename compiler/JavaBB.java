import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String[] args) {
        var compDir = "src";
        if (LangUtil.isTruthy(args)) { compDir = args [0]; }
        var outDir = compDir + "_out";
        if (LangUtil.isTruthy(args.length > 1)) { outDir = args [1]; }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdir();
        }
        var files = new File(compDir).list((File dir , String name)-> name.endsWith(".jbb"));
        for (var i : LangUtil.asIterable(files.length)) {
            var fileContent = "";
            var fromPath = compDir + "/" + files [i];
            var toPath = outDir + "/" + files [i].split ("\\.")[0] + ".java";
            var className = files [i].split ("\\.")[0];
            try (var scanner = new Scanner(new File(fromPath))) {
                fileContent = scanner.useDelimiter("\\Z").next();
            }
            catch (FileNotFoundException e) {
                
            }
            var res = Compiler.compileFile(className , fileContent);
            var compiled = res.getCompiledCode(className);
            try (var writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            }
            catch (IOException e) {
                
            }
        }
        System.out.println("\n\nCompiling LangUtil...");
        var res = Compiler.compileFile("LangUtil" , """
// import java.lang.reflect.*

# print(object ... args):
    System.out.print('' + x) for x in args

# println(object ... args):
    System.out.print('' + x) for x in args
    System.out.println('')

# bool isTruthy(bool v):
    ret v

# bool isTruthy(int v):
    ret v != 0

# bool isTruthy(double v):
    ret v != 0

# bool isTruthy(string v):
    ret false if v is null
    inline(return !v.isEmpty();)

# bool (T) isTruthy(T[] v):
    ret v.length > 0

# bool isTruthy(List v):
    ret false if v is null
    inline(return !v.isEmpty();)

# bool isTruthy(object v):
    inline(if (v instanceof Boolean x) return x;)
    inline(if (v instanceof Integer x) return x != 0;)
    inline(if (v instanceof Double x) return x != 0;)
    inline(if (v instanceof String x) return x == null ? false : !x.isEmpty();)
    inline(if (v instanceof List x) return x == null ? false : !x.isEmpty();)
    ret v != null

# T[] (T) asIterable(T[] v):
    ret v

# List<int> asIterable(int n):
    let lst = new ArrayList<int>()
    inline `for (int i = 0; i < n; ++i)`
        lst.add(i)
    ret lst

# Iterable<T> (T) asIterable(Iterable<T> v):
    ret v

# Character[] asIterable(string s):
    ret s.toCharArray()

// # Object get(Object obj, string varname):
//     let method_name = '_get_' + varname
//     let inst = obj

//     try
//         return inst.getClass().getDeclaredMethod(method_name).invoke(inst)
//     catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
//         ...

//     try
//         return inst.getClass().getDeclaredField(varname).get(inst)
//     catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e)
//         return null

// # Object dot(Object obj, string method):
//     try
//         return obj.getClass().getMethod(method)
//     catch (NoSuchMethodException e)
//         return null

// Operators
    """.trim());
        var langUtilCode = res.getCompiledCode("LangUtil");
        try (var writer = new PrintWriter(outDir + "/LangUtil.java")) {
            new File(outDir + "/LangUtil.java").createNewFile();
            writer.println(langUtilCode);
        }
        catch (IOException e) {
            
        }
        System.out.println("\nDone.");
    }
}

