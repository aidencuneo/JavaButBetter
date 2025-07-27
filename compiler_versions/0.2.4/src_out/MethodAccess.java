import java.io.*;
import java.util.*;

public class MethodAccess {
    public AccessMod accessMod = AccessMod.PUBLIC;
    public boolean isStatic = false;
    public MethodAccess(AccessMod accessMod , boolean isStatic) {
        this . accessMod = accessMod;
        this . isStatic = isStatic;
    }
    public static AccessMod accessModFromToken(Token tok) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("public"))) ? (tok.value.equals("public")) : (tok.value.equals("+")))) { return AccessMod.PUBLIC; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("private"))) ? (tok.value.equals("private")) : (tok.value.equals("-")))) { return AccessMod.PRIVATE; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok.value.equals("protected"))) ? (tok.value.equals("protected")) : (tok.value.equals("*")))) { return AccessMod.PROTECTED; }
        return AccessMod.DEFAULT;
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

