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
            String compiled = Compiler.compileFile(className, fileContent);

            // Write to output file
            try (PrintWriter writer = new PrintWriter(toPath)) {
                new File(toPath).createNewFile();
                writer.println(compiled);
            } catch (IOException e) {}
        }

        System.out.println("Done.");
    }
}
