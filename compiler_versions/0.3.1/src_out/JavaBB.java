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
        var extensionsClassRes = Compiler.compileFile("Extensions" , """
public static class Extensions

// +
int operAdd(int a, int b):
    inline(return a + b;)

long operAdd(long a, long b):
    inline(return a + b;)

double operAdd(double a, double b):
    inline(return a + b;)

int operAdd(bool a, bool b):
    inline(return (a ? 1 : 0) + (b ? 1 : 0);)

// ==
bool operEq(int a, int b):
    inline(return a == b;)

bool operEq(long a, long b):
    inline(return a == b;)

bool operEq(double a, double b):
    inline(return a == b;)

bool operEq(bool a, bool b):
    inline(return a == b;)

bool operEq(string a, string b):
    return a.equals(b)

bool operEq(object a, object b):
    return a.equals(b)
    """.trim());
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
            if (LangUtil.isTruthy(res.classes.containsKey("Extensions"))) {
                var curCode = extensionsClassRes.classes.get("Extensions");
                var newCode = res.classes.get("Extensions");
                extensionsClassRes.classes.put("Extensions" , "    " + curCode + newCode);
                res.classes.remove("Extensions");
            }
            var compiled = res.getCompiledCode(className);
            try (var writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            }
            catch (IOException e) {
                
            }
        }
        LangUtil.println("\n\nCompiling Extensions...");
        try (var writer = new PrintWriter(outDir + "/Extensions.java")) {
            new File(outDir + "/Extensions.java").createNewFile();
            writer.println(extensionsClassRes.getCompiledCode("Extensions"));
        }
        catch (IOException e) {
            
        }
        LangUtil.println("\n\nCompiling LangUtil...");
        var res = Compiler.compileFile("LangUtil" , """
// import java.lang.reflect.*

public static class LangUtil

print(object ... args):
    System.out.print('' + x) for x in args

println(object ... args):
    System.out.print('' + x) for x in args
    System.out.println('')

// isTruthy
bool isTruthy(bool v):
    ret v

bool isTruthy(int v):
    ret v != 0

bool isTruthy(double v):
    ret v != 0

bool isTruthy(string v):
    ret false if v is null
    inline(return !v.isEmpty();)

(T) bool isTruthy(T[] v):
    ret v.length > 0

bool isTruthy(List v):
    ret false if v is null
    inline(return !v.isEmpty();)

bool isTruthy(object v):
    inline(if (v instanceof Boolean x) return x;)
    inline(if (v instanceof Integer x) return x != 0;)
    inline(if (v instanceof Double x) return x != 0;)
    inline(if (v instanceof String x) return x == null ? false : !x.isEmpty();)
    inline(if (v instanceof List x) return x == null ? false : !x.isEmpty();)
    ret v != null

// asIterable
(T) T[] asIterable(T[] v):
    ret v

List<Int> asIterable(int n):
    let lst = new ArrayList<Int>()
    inline `for (int i = 0; i < n; ++i)`
        lst.add(i)
    ret lst

(T) Iterable<T> asIterable(Iterable<T> v):
    ret v

char[] asIterable(string s):
    ret s.toCharArray()

// len
int len(string s):
    ret s.length()

(T) int len(Iterable<T> v):
    let c = 0
    ++c for _ in v
    ret c

(T) int len(T[] v):
    ret v.length

// object get(object obj, string varname):
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

// object dot(object obj, string method):
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
        LangUtil.println("\n\nDone.");
    }
}

