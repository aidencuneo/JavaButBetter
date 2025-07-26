import java.io.*;
import java.util.*;
import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String args []) {
        var compDir = "src";
        if (LangUtil.isTruthy(args.length)) {
            var compDir = args [0];
        }
        var outDir = compDir + "_out";
        if (LangUtil.isTruthy(args.length > 1)) {
            var outDir = args [1];
        }
        if (LangUtil.isTruthy(! new File (outDir).exists ())) {
            new File (outDir).mkdir ();
        } {
            var fileContent = "";
            var fromPath = compDir + '/' + files [i];
            var toPath = outDir + '/' + files [i].split ("\\.")[0] + ".java";
            var className = files [i].split ("\\.")[0];
            try scanner = new Scanner (new File (fromPath)); {
                var fileContent = scanner.useDelimiter ("\\Z").next ();
            }
            catch (FileNotFoundException e); {
                
            }
            var compiled = Compiler.compileFile (className , fileContent);
            try writer = new PrintWriter (toPath); {
                new File (toPath).createNewFile ();
                writer.println (compiled);
            }
            catch (IOException e); {
                
            }
        }
        System.out.println("Done.");
    }
}

