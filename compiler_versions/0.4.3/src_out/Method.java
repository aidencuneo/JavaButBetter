import java.io.*;
import java.util.*;

public class Method {
    public MethodAccess access;
    public String returnType;
    public String args;
    public HashMap < String , Local > locals = new HashMap < > ();
    public Method(MethodAccess access , String returnType , String args) {
        this . access = access;
        this . returnType = returnType;
        this . args = args;
    }
}

