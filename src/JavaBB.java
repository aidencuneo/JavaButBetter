import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String args[]) {
        // Get input and output directories
        String compDir = "src";
        
        if (args.length > 0)
            compDir = args[0];

        String outDir = compDir + "_out";

        if (args.length > 1)
            outDir = args[1];

        // Create directory if it doesn't exist
        if (!new File(outDir).exists())
            new File(outDir).mkdir();

        // Compile files one by one
        String[] files = new File(compDir).list((File dir, String name) -> name.endsWith(".jbb"));

        // String[] files = new File(compDir).list();

        for (int i = 0; i < files.length; ++i) {
            String fileContent = "";
            String fromPath = compDir + "/" + files[i];
            String toPath = outDir + "/" + files[i].split("\\.")[0] + ".java";
            String className = files[i].split("\\.")[0];

            // Read input file
            try (Scanner scanner = new Scanner(new File(fromPath))) {
                fileContent = scanner.useDelimiter("\\Z").next();
            } catch (FileNotFoundException e) {}

            // Compile this file
            CompResult res = Compiler.compileFile(className, fileContent);
            String compiled = res.getCompiledCode(className);

            // Write to output file
            try (PrintWriter writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            } catch (IOException e) {}
        }

        // System.exit(0);

        // Compile LangUtil
        CompResult res = Compiler.compileFile("LangUtil", """
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

# List asIterable(int n):
    let lst = new ArrayList<Integer>();
    inline `for (int i = 0; i < n; ++i)`
        lst.add(i);
    ret lst

# List asIterable(List v):
    ret v

# char[] asIterable(string s):
    ret s.toCharArray()
        """.trim());
        String langUtilCode = res.getCompiledCode("LangUtil");

        try (PrintWriter writer = new PrintWriter(outDir + "/LangUtil.java")) {
            new File(outDir + "/LangUtil.java").createNewFile();
            writer.println(langUtilCode);
        } catch (IOException e) {}

        System.out.println("Done.");
    }
}
