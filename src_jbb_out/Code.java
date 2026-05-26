import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static String packagePath = "";
    public static HashMap [String, Class] classes = Extensions.operGetIndex(new HashMap, string, Class)();
    public static HashMap [String, Alias] aliases = Extensions.operGetIndex(new HashMap, string, Alias)();
    public static HashMap [String, Int] locals = Extensions.operGetIndex(HashMap, string, Integer)();
    public static void main(String[] args) {
        
    }
    public static [T] T cast2(Object v) {
        return (T)v;
    }
}

