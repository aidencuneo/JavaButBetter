import java.io.*;
import java.util.*;

public class StringParser {
    public static String unescapeString(String str) {
        var out = "";
        for (int i = 0; i < str.length(); ++i) {
            var c = str.charAt(i);
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\\'))) ? (i + 1 < str.length()) : (Extensions.operEq(c, '\\')))) {
                var next = str.charAt(i + 1);
                ++ i;
                if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(next, 'u'))) ? (i + 4 < str.length()) : (Extensions.operEq(next, 'u')))) {
                    try {
                        var code = Integer.parseInt(str.substring(i + 1, i + 5), 16);
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
}

