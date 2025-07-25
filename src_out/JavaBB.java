import java.io.*;
import java.util.*;
import java.io.*;
import java.util.*;

public class JavaBB {
    public static void main(String args []) {
        var compDir = "src";
        if (args . length) {
            var compDir = args [0];
        }
        var outDir = compDir + "_out";
        if (args . length > 1) {
            var outDir = args [1];
        }
        if (! new File (outDir) . exists ()) {
            new File (outDir) . mkdir ();
        }
        var files = new File (compDir) . list (new FilenameFilter(); {
            public boolean accept(File dir , String name) {
                return name . endsWith ('.java');
            }
        }
        ); {
            var fileContent = '';
            var fromPath = compDir + '/' + files [i];
            var toPath = outDir + '/' + files [i] . split ('\\.') [0] + ".java";
            var className = files [i] . split ('\\.') [0];
            try scanner = new Scanner (new File(fromPath)); {
                var fileContent = scanner . useDelimiter ('\\Z') . next ();
            }
            catch (FileNotFoundException e); {
                pass;
            }
            var compiled = Compiler . compileFile (className, fileContent);
            try writer = new PrintWriter (toPath); {
                new File (toPath) . createNewFile ();
                writer . println (compiled);
            }
            catch (IOException e); {
                pass;
            }
        }
        System.out.println("Done.");
    }
}

