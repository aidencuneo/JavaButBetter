import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        
        var a = Extensions.operAdd(1.5d, 2.0d);
        LangUtil.println(a);
        LangUtil.println(LangUtil.round(a));
    }
}

