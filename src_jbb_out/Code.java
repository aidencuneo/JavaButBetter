package aidenbc;

import java.io.*;
import java.util.*;

class Code implements Iterable < T > {
    public String mainClassName = "";
    public String currentClass = "";
    public String startTemplate = "";
    public String endTemplate = "";
    public HashMap < String , Class > classes = null;
    public HashMap < String , Alias > aliases = null;
    public HashMap < String , Integer > locals = null;
    public int nextTempVar = 0;
    public int indent = 0;
    public int scope = 0;
    public boolean defaultStatic = false;
    public CompResult compileFile(String className , String code) {
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";
        className = "what";
        code = code;
    } {
        
    } {
        
    }
}
class Code2 extends Iterable < T > {
    {
        
    }
}
public class Code3<T> implements Iterable < T > {
    {
        
    }
}
public class Code4<T> extends Iterable < T > {
    {
        
    }
    public static void main(String[] args) {
        var empty = List.of();
        empty = List.of(1, 2, 3);
        for (var i : LangUtil.asIterable(10)) {
            i = 20; {
                i = 30;
            }
            i = 40;
        }
        var i = 50;
        var lst = List.of(1, 2, 3, "hello");
        var ints = List.of(0, 1, 2, 3);
        LangUtil.println(LangUtil.slice(ints, 0, - 1, 1), LangUtil.slice(ints, null, null, - 1), LangUtil.slice(ints, null, null, 1), LangUtil.slice(ints, null, 2, - 1), LangUtil.slice(ints, 1, null, 1));
        LangUtil.println(Extensions.operEq("69", "69"));
    }
}

