import java.io.*;
import java.util.*;

public class Class {
    public AccessMod access = AccessMod.PUBLIC;
    public String name = "";
    public String typeArgs = "";
    public String implementsType = "";
    public String extendsType = "";
    public String code = "";
    public Class(String name) {
        this . name = name;
    }
    public String toString() {
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

