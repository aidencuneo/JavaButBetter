import java.io.*;
import java.util.*;

public class Code {
    public static AccessMod access = AccessMod.PUBLIC;
    public static String name = "";
    public static String typeArgs = "";
    public static String implementsType = "";
    public static String extendsType = "";
    public static String code = "";
    public static Code(String name) {
        this . name = name;
    }
    public static String toString() {
        if (LangUtil.isTruthy(code.isBlank())) { return ""; }
        var accessModStr = MethodAccess.accessModToString(access);
        accessModStr += LangUtil.isTruthy(accessModStr) ? (" ") : ("");
        var extendsStr = LangUtil.isTruthy(extendsType) ? (Extensions.operAdd(" extends ", extendsType)) : ("");
        var implementsStr = LangUtil.isTruthy(implementsType) ? (Extensions.operAdd(" implements ", implementsType)) : ("");
        var typeArgStr = LangUtil.isTruthy(typeArgs) ? (Extensions.operAdd(Extensions.operAdd("<", typeArgs), ">")) : ("");
        var out = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(accessModStr, "class "), name), typeArgStr), extendsStr), implementsStr), " {\n");
        out += Extensions.operAdd("    ", code.trim());
        out += "\n}\n";
        return out;
    }
}

