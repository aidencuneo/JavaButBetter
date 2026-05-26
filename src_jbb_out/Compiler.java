import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static String packagePath = "";
    public static HashMap [String, Class] classes = Extensions.operGetIndex(new HashMap, String, Class)();
    public static HashMap [String, Alias] aliases = Extensions.operGetIndex(new HashMap, String, Alias)();
    public static HashMap [String, Int] locals = Extensions.operGetIndex(HashMap, String, Integer)();
    public static void main(String[] args) {
        Something.Cool<Vector.Int, Vector.Something.Else, Hi>();
    }
}

