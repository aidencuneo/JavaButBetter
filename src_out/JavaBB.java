import java.io.*;
import java.util.*;
import java.io.*;
import java.util.*;

public class JavaBB {
    {
        public static void main (String args[]) {; {
            String compDir = "src";
            if ((args . length > 0)) {
                var compDir = args [0];
    }
            String outDir = compDir + "_out";
            if ((args . length > 1)) {
                var outDir = args [1];
    }
            if ((! new File (outDir) . exists ())) {
                new File (outDir) . mkdir ();
    }
            var  = new File (compDir) . list (new FilenameFilter() {; {
                public boolean accept (File dir, String name) {; {
                    return name . endsWith (".java");
    }
                };
    }
            } );
            
       }
   }
     i < files . length;
     ++ i ) {; { { {
 }
                String fileContent = "";
                String fromPath = compDir + "/" + files [i];
                String toPath = outDir + "/" + files [i] . split ("\\.") [0] + ".java";
                String className = files [i] . split ("\\.") [0];
                try (Scanner scanner = new Scanner(new File(fromPath))) {; {
                    var fileContent = scanner . useDelimiter ("\\Z") . next ();
    }
                } catch (FileNotFoundException e) { };
                String compiled = Compiler . compileFile (className, fileContent);
                try (PrintWriter writer = new PrintWriter(toPath)) {; {
                    new File (toPath) . createNewFile ();
                    writer . println (compiled);
    }
                } catch (IOException e) { };
    }
            };
            System . out . println ("Done.");
    }
        };
    }
    };
}

