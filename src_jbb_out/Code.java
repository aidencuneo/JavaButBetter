import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static String packagePath = "";
    public static HashMap<String, Class> classes = new HashMap<String, Class>();
    public static HashMap<String, Alias> aliases = new HashMap<String, Alias>();
    public static HashMap<String, Integer> locals = new HashMap<String, Integer>();
    public static void main(String[] args) {
        
    }
    public static <T> T cast2(Object v) {
        return (T)v;
    }
}

