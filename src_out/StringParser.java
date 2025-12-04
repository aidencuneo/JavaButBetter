import java.io.*;
import java.util.*;

public class StringParser {
    public static String unescapeString(String str) {
        var out = "";
        for (int i = 0; i < str.length(); ++i) {
            var c = Extensions.operGetIndex(str, i);
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\\'))) ? (Extensions.operAdd(i, 1) < str.length()) : (Extensions.operEq(c, '\\')))) {
                var next = Extensions.operGetIndex(str, Extensions.operAdd(i, 1));
                ++ i;
                if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(next, 'u'))) ? (Extensions.operAdd(i, 4) < str.length()) : (Extensions.operEq(next, 'u')))) {
                    try {
                        var code = Integer.parseInt(str.substring(Extensions.operAdd(i, 1), Extensions.operAdd(i, 5)), 16);
                        out += (char) code;
                        i += 4;
                    }
                    catch (NumberFormatException e) {
                        out += "\\u";
                    }
                }
                else {
                    switch (next) {
                        case 'b' -> out += '\b';
                        case 'f' -> out += '\f';
                        case 'n' -> out += '\n';
                        case 'r' -> out += '\r';
                        case 't' -> out += '\t';
                        case '\"' -> out += '\"';
                        case '\'' -> out += '\'';
                        case '\\' -> out += '\\';
                        default -> out += next;
                    }
                }
            }
            else {
                out += c;
            }
        }
        return out;
    }
    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }
    public static boolean isPascalCase(String str) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(str))) { return false; }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(Character.isUpperCase(Extensions.operGetIndex(str, 0))))) { return false; }
        for (var c : LangUtil.asIterable(LangUtil.slice(str, 1, null, 1))) {
            if (LangUtil.isTruthy(!LangUtil.isTruthy(Character.isUpperCase(c)))) { return true; }
        }
        return false;
    }
}

