import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String[] args) {
        String compDir = "src";
        if (LangUtil.isTruthy(args.length > 0)) {
            compDir = args [0];
        }
        String outDir = compDir + "_out";
        if (LangUtil.isTruthy(args.length > 1)) {
            outDir = args [1];
        }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(new File(outDir).exists()))) {
            new File(outDir).mkdir();
        }
        String [] files = new File(compDir).list((File dir , String name)-> name.endsWith(".jbb"));
        for (var i : LangUtil.asIterable(files.length)) {
            String fileContent = "";
            String fromPath = compDir + "/" + files [i];
            String toPath = outDir + "/" + files [i].split ("\\.")[0] + ".java";
            String className = files [i].split ("\\.")[0];
            try (var scanner = new Scanner(new File(fromPath))) {
                fileContent = scanner.useDelimiter("\\Z").next();
            }
            catch (FileNotFoundException e) {
                
            }
            CompResult res = Compiler.compileFile(className , fileContent);
            String compiled = res.getCompiledCode(className);
            try (var writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            }
            catch (IOException e) {
                
            }
        }
        CompResult res = Compiler.compileFile("LangUtil" , """
import java.lang.reflect.*

# print(Object s):
    System.out.print('' + s)

# println(Object s):
    System.out.println('' + s)

# bool isTruthy(bool v):
    ret v

# bool isTruthy(Object v):
    ret v != null

# bool isTruthy(int v):
    ret v != 0

# bool isTruthy(double v):
    ret v != 0

# bool isTruthy(string v):
    ret !v.isEmpty()

# bool (T) isTruthy(T[] v):
    ret v.length > 0

# bool isTruthy(List v):
    ret !v.isEmpty()

# T[] (T) asIterable(T[] v):
    ret v

# List<Integer> asIterable(int n):
    let lst = new ArrayList<Integer>()
    inline `for (int i = 0; i < n; ++i)`
        lst.add(i)
    ret lst

# Iterable<T> (T) asIterable(Iterable<T> v):
    ret v

# char[] asIterable(string s):
    ret s.toCharArray()

# Object get(Object obj, string varname):
    let method_name = '_get_' + varname
    let inst = obj

    try
        return inst.getClass().getDeclaredMethod(method_name).invoke(inst)
    catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        ...

    try
        return inst.getClass().getDeclaredField(varname).get(inst)
    catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e)
        return null

# Object dot(Object obj, string method):
    try
        return obj.getClass().getMethod(method)
    catch (NoSuchMethodException e)
        return null

    """.trim());
        String langUtilCode = res.getCompiledCode("LangUtil");
        try (var writer = new PrintWriter(outDir + "/LangUtil.java")) {
            new File(outDir + "/LangUtil.java").createNewFile();
            writer.println(langUtilCode);
        }
        catch (IOException e) {
            
        }
        System.out.println("Done.");
    }
}

