import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        var ending = LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.COMMA)) ? ("") : (";");
        if (LangUtil.isTruthy(Extensions.operEq("hi", "hi"))) {
            LangUtil.println("yes");
        }
        if (LangUtil.isTruthy(Extensions.operEq("hi2", "hi"))) {
            LangUtil.println("yes");
        }
        LangUtil.println((LangUtil.isTruthy(4 > 5) ? (4) : (5)) + 10);
    }
}

