import java.io.*;
import java.util.*;

public class MethodAccess {
    public AccessMod accessMod = AccessMod.NONE;
    public boolean isStatic = false;
    public MethodAccess(AccessMod accessMod , boolean isStatic) {
        this . accessMod = accessMod;
        this . isStatic = isStatic;
    }
    public static AccessMod accessModFromToken(Token tok) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.type == Token.Type.DEFAULT)) ? (tok.type == Token.Type.DEFAULT) : (tok.value.equals("?")))) { return AccessMod.DEFAULT; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("public"))) ? (tok.value.equals("public")) : (tok.value.equals("+")))) { return AccessMod.PUBLIC; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("private"))) ? (tok.value.equals("private")) : (tok.value.equals("-")))) { return AccessMod.PRIVATE; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("protected"))) ? (tok.value.equals("protected")) : (tok.value.equals("*")))) { return AccessMod.PROTECTED; }
        return AccessMod.NONE;
    }
    public static String accessModToString(AccessMod accessMod) {
        if (LangUtil.isTruthy(accessMod == AccessMod.PUBLIC)) { return "public"; }
        if (LangUtil.isTruthy(accessMod == AccessMod.PRIVATE)) { return "private"; }
        if (LangUtil.isTruthy(accessMod == AccessMod.PROTECTED)) { return "protected"; }
        return "";
    }
    public String toString() {
        return accessModToString(accessMod) + (isStatic ? " static" : "");
    }
}

