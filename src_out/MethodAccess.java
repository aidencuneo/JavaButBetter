import java.io.*;
import java.util.*;

public class MethodAccess {
    public AccessMod accessMod = AccessMod.NONE;
    public boolean isStatic = false;
    public MethodAccess(AccessMod accessMod , boolean isStatic) {
        var this . accessMod = accessMod;
        var this . isStatic = isStatic;
    }
    public static AccessMod accessModFromToken(Token tok) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(tok.type, Token.Type.DEFAULT))) ? (Extensions.operEq(tok.type, Token.Type.DEFAULT)) : (Extensions.operEq(tok.value, "?")))) { return AccessMod.DEFAULT; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(tok.value, "public"))) ? (Extensions.operEq(tok.value, "public")) : (Extensions.operEq(tok.value, "+")))) { return AccessMod.PUBLIC; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(tok.value, "private"))) ? (Extensions.operEq(tok.value, "private")) : (Extensions.operEq(tok.value, "-")))) { return AccessMod.PRIVATE; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(tok.value, "protected"))) ? (Extensions.operEq(tok.value, "protected")) : (Extensions.operEq(tok.value, "*")))) { return AccessMod.PROTECTED; }
        return AccessMod.NONE;
    }
    public static String accessModToString(AccessMod accessMod) {
        if (LangUtil.isTruthy(Extensions.operEq(accessMod, AccessMod.PUBLIC))) { return "public"; }
        if (LangUtil.isTruthy(Extensions.operEq(accessMod, AccessMod.PRIVATE))) { return "private"; }
        if (LangUtil.isTruthy(Extensions.operEq(accessMod, AccessMod.PROTECTED))) { return "protected"; }
        return "";
    }
    public String toString() {
        return accessModToString(accessMod) + (LangUtil.isTruthy(isStatic) ? (" static") : (""));
    }
}

