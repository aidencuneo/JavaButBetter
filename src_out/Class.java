import java.io.*;
import java.util.*;

public class Class {
    public String name = "";
    public String code = "";
    public AccessMod access = AccessMod.PUBLIC;
    public Class() {
        
    }
    public Class(String name , String code , AccessMod access) {
        this . name = name;
        this . code = code;
        this . access = access;
    }
}

