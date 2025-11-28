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
        var extendsStr = LangUtil.isTruthy(extendsType) ? (" extends " + extendsType) : ("");
        var implementsStr = LangUtil.isTruthy(implementsType) ? (" implements " + implementsType) : ("");
        var typeArgStr = LangUtil.isTruthy(typeArgs) ? ("<" + typeArgs + ">") : ("");
        var out = accessModStr + "class " + name + typeArgStr + extendsStr + implementsStr + " {\n";
        out += "    " + code.trim();
        out += "\n}\n";
        return out;
    }
}

