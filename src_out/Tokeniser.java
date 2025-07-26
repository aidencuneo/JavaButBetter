import java.io.*;
import java.util.*;

public class Tokeniser {
    public enum CharType {
        A , D , S , W ,
    }
    public static CharType charType(char c) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(((LangUtil.isTruthy(c >= 'a')) ? (c <= 'z') : (c >= 'a')))) ? (((LangUtil.isTruthy(c >= 'a')) ? (c <= 'z') : (c >= 'a'))) : ((LangUtil.isTruthy(((LangUtil.isTruthy(c >= 'A')) ? (c <= 'Z') : (c >= 'A')))) ? (((LangUtil.isTruthy(c >= 'A')) ? (c <= 'Z') : (c >= 'A'))) : (c == '_')))) {
            return CharType.A;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(c >= '0')) ? (c <= '9') : (c >= '0'))) {
            return CharType.D;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(c == ' ')) ? (c == ' ') : ((LangUtil.isTruthy(c == '\t')) ? (c == '\t') : ((LangUtil.isTruthy(c == '\n')) ? (c == '\n') : ((LangUtil.isTruthy(c == '\r')) ? (c == '\r') : (c == '\f')))))) {
            return CharType.W;
        }
        else {
            return CharType.S;
        }
    }
    public static ArrayList < String > splitFile(String file) {
        ArrayList < String > lines = new ArrayList < > ();
        if (LangUtil.isTruthy(!LangUtil.isTruthy(file))) { return lines; }
        String current = "";
        CharType type = charType(file.charAt(0));
        CharType lastType;
        char lastChar = 0;
        int comment = 0;
        boolean backslash = false;
        boolean sq = false;
        boolean dq = false;
        boolean bt = false;
        int rb = 0;
        int sb = 0;
        int cb = 0;
        int lastIndent = 0;
        int indent = 0;
        for (var i : LangUtil.asIterable(file.length())) {
            char c = file.charAt(i);
            lastType = type;
            type = charType(c);
            if (LangUtil.isTruthy((LangUtil.isTruthy(c == ' ')) ? (lastType == CharType.W) : (c == ' '))) {
                ++ indent;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '\t')) ? (lastType == CharType.W) : (c == '\t'))) {
                indent += 4;
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(((LangUtil.isTruthy(c == '\n')) ? (c == '\n') : (c == ';')))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0)))))))) : (((LangUtil.isTruthy(c == '\n')) ? (c == '\n') : (c == ';'))))) {
                lines.add(current);
                current = "";
                if (LangUtil.isTruthy(c == ';')) {
                    current += " ".repeat(indent);
                    for (++i; i < file.length() && file.charAt(i) == ' ' || file.charAt(i) == '\t'; ++i) {}
                    -- i;
                }
            }
            else if (LangUtil.isTruthy(comment <= 1)) {
                if (LangUtil.isTruthy((LangUtil.isTruthy(comment > 0)) ? (c != '/') : (comment > 0))) {
                    comment = 0;
                }
                if (LangUtil.isTruthy(c != '\r')) {
                    current += c;
                }
            }
            if (LangUtil.isTruthy(comment >= 2)) {
                
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '\\')) ? (((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt)))) : (c == '\\'))) {
                backslash = !LangUtil.isTruthy(backslash);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '\'')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (c == '\''))) {
                sq = !LangUtil.isTruthy(sq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '"')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (c == '"'))) {
                dq = !LangUtil.isTruthy(dq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '`')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (backslash))))) : (c == '`'))) {
                bt = !LangUtil.isTruthy(bt);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '(')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (c == '('))) {
                ++ rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == ')')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (c == ')'))) {
                -- rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '[')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (c == '['))) {
                ++ sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == ']')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (c == ']'))) {
                -- sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '/')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt))))) : (c == '/'))) {
                if (LangUtil.isTruthy(++ comment >= 2)) {
                    current = current.substring(0 , current.length() - 2);
                }
            }
            if (LangUtil.isTruthy(c == '\n')) {
                comment = 0;
                lastIndent = indent;
                indent = 0;
            }
            if (LangUtil.isTruthy(c != '\\')) {
                backslash = false;
            }
            lastChar = c;
        }
        if (LangUtil.isTruthy(current)) { lines.add(current); }
        return lines;
    }
    public static ArrayList < Token > tokLine(String line) {
        var tok = new ArrayList < Token > ();
        if (LangUtil.isTruthy(!LangUtil.isTruthy(line))) { return tok; }
        String indent = "";
        int i = 0;
        for (; i < line.length(); ++i) {
            var c = line.charAt(i);
            if (LangUtil.isTruthy(c == ' ')) {
                indent += c;
            }
            else if (LangUtil.isTruthy(c == '\t')) {
                indent += "    ";
            }
            else {
                break;
            }
        }
        tok.add(new Token(Token.Type.INDENT , indent));
        if (LangUtil.isTruthy(i == line.length())) {
            return tok;
        }
        String current = "";
        CharType type = charType(line.charAt(i));
        CharType lastType;
        char lastChar = 0;
        int comment = 0;
        boolean backslash = false;
        boolean sq = false;
        boolean dq = false;
        boolean bt = false;
        int rb = 0;
        int sb = 0;
        int cb = 0;
        for (; i < line.length(); ++i) {
            char c = line.charAt(i);
            lastType = type;
            type = charType(c);
            if (LangUtil.isTruthy(((LangUtil.isTruthy(((LangUtil.isTruthy((LangUtil.isTruthy(type != lastType)) ? (type != CharType.W) : (type != lastType))) ? ((LangUtil.isTruthy(type != lastType)) ? (type != CharType.W) : (type != lastType)) : (type == CharType.S)))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(("+-*/=><!.?".contains("" + lastChar)))) ? (c == '=') : (("+-*/=><!.?".contains("" + lastChar))))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '_')) ? (type == CharType.A) : (lastChar == '_'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.A)) ? (c == '_') : (lastType == CharType.A))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.A)) ? (type == CharType.D) : (lastType == CharType.A))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(type == CharType.A)) ? (lastType == CharType.D) : (type == CharType.A))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.D)) ? (c == '.') : (lastType == CharType.D))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '.')) ? (type == CharType.D) : (lastChar == '.'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '-')) ? (c == '>') : (lastChar == '-'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '=')) ? (c == '>') : (lastChar == '='))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '_')) ? (c == '_') : (lastChar == '_'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '+')) ? (c == '+') : (lastChar == '+'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '-')) ? (c == '-') : (lastChar == '-'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '*')) ? (c == '*') : (lastChar == '*'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '?')) ? (c == '?') : (lastChar == '?'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '.')) ? (c == '.') : (lastChar == '.'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '&')) ? (c == '&') : (lastChar == '&'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '|')) ? (c == '|') : (lastChar == '|'))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '\'')) ? (c == '\'') : (lastChar == '\''))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '"')) ? (c == '"') : (lastChar == '"'))))) ? (!LangUtil.isTruthy(current.isBlank())) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '"')) ? (c == '"') : (lastChar == '"'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '\'')) ? (c == '\'') : (lastChar == '\''))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '|')) ? (c == '|') : (lastChar == '|'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '&')) ? (c == '&') : (lastChar == '&'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '.')) ? (c == '.') : (lastChar == '.'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '?')) ? (c == '?') : (lastChar == '?'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '*')) ? (c == '*') : (lastChar == '*'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '-')) ? (c == '-') : (lastChar == '-'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '+')) ? (c == '+') : (lastChar == '+'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '_')) ? (c == '_') : (lastChar == '_'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '=')) ? (c == '>') : (lastChar == '='))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '-')) ? (c == '>') : (lastChar == '-'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '.')) ? (type == CharType.D) : (lastChar == '.'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.D)) ? (c == '.') : (lastType == CharType.D))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(type == CharType.A)) ? (lastType == CharType.D) : (type == CharType.A))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.A)) ? (type == CharType.D) : (lastType == CharType.A))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastType == CharType.A)) ? (c == '_') : (lastType == CharType.A))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(lastChar == '_')) ? (type == CharType.A) : (lastChar == '_'))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(("+-*/=><!.?".contains("" + lastChar)))) ? (c == '=') : (("+-*/=><!.?".contains("" + lastChar))))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))))) : (((LangUtil.isTruthy((LangUtil.isTruthy(type != lastType)) ? (type != CharType.W) : (type != lastType))) ? ((LangUtil.isTruthy(type != lastType)) ? (type != CharType.W) : (type != lastType)) : (type == CharType.S)))))) {
                tok.add(Token.fromString(current.trim()));
                current = "";
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(c == '\\')) ? (((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt)))) : (c == '\\'))) {
                backslash = !LangUtil.isTruthy(backslash);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '\'')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (c == '\''))) {
                sq = !LangUtil.isTruthy(sq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '"')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (c == '"'))) {
                dq = !LangUtil.isTruthy(dq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '`')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (backslash))))) : (c == '`'))) {
                bt = !LangUtil.isTruthy(bt);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '(')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (c == '('))) {
                ++ rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == ')')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (c == ')'))) {
                -- rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '[')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (c == '['))) {
                ++ sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == ']')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (c == ']'))) {
                -- sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(c == '/')) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt))))) : (c == '/'))) {
                if (LangUtil.isTruthy(++ comment >= 2)) {
                    tok.remove(tok.size() - 1);
                    break;
                }
            }
            if (LangUtil.isTruthy(comment <= 1)) {
                if (LangUtil.isTruthy((LangUtil.isTruthy(comment > 0)) ? (c != '/') : (comment > 0))) {
                    comment = 0;
                }
                current += c;
            }
            if (LangUtil.isTruthy(c != '\\')) {
                backslash = false;
            }
            lastChar = c;
        }
        if (LangUtil.isTruthy(current)) { tok.add(Token.fromString(current.trim())); }
        return tok;
    }
}

