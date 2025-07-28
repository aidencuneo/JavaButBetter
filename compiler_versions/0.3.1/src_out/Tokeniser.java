import java.io.*;
import java.util.*;

public class Tokeniser {
    public enum CharType {
        A , D , S , W ,
    }
    public static CharType charType(char c) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(((LangUtil.isTruthy(c >= 'a')) ? (c <= 'z') : (c >= 'a')))) ? (((LangUtil.isTruthy(c >= 'a')) ? (c <= 'z') : (c >= 'a'))) : ((LangUtil.isTruthy(((LangUtil.isTruthy(c >= 'A')) ? (c <= 'Z') : (c >= 'A')))) ? (((LangUtil.isTruthy(c >= 'A')) ? (c <= 'Z') : (c >= 'A'))) : (Extensions.operEq(c, '_'))))) {
            return CharType.A;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(c >= '0')) ? (c <= '9') : (c >= '0'))) {
            return CharType.D;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ' '))) ? (Extensions.operEq(c, ' ')) : ((LangUtil.isTruthy(Extensions.operEq(c, '\t'))) ? (Extensions.operEq(c, '\t')) : ((LangUtil.isTruthy(Extensions.operEq(c, '\n'))) ? (Extensions.operEq(c, '\n')) : ((LangUtil.isTruthy(Extensions.operEq(c, '\r'))) ? (Extensions.operEq(c, '\r')) : (Extensions.operEq(c, '\f'))))))) {
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
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ' '))) ? (Extensions.operEq(lastType, CharType.W)) : (Extensions.operEq(c, ' ')))) {
                ++ indent;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\t'))) ? (Extensions.operEq(lastType, CharType.W)) : (Extensions.operEq(c, '\t')))) {
                indent += 4;
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(c, '\n'))) ? (Extensions.operEq(c, '\n')) : (Extensions.operEq(c, ';'))))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0)))))))) : (((LangUtil.isTruthy(Extensions.operEq(c, '\n'))) ? (Extensions.operEq(c, '\n')) : (Extensions.operEq(c, ';')))))) {
                lines.add(current);
                current = "";
                if (LangUtil.isTruthy(Extensions.operEq(c, ';'))) {
                    current += " ".repeat(indent);
                    for (++i; i < file.length() && file.charAt(i) == ' ' || file.charAt(i) == '\t'; ++i) {}
                    -- i;
                }
            }
            else if (LangUtil.isTruthy(comment <= 1)) {
                if (LangUtil.isTruthy((LangUtil.isTruthy(comment > 0)) ? (!Extensions.operEq(c, '/')) : (comment > 0))) {
                    comment = 0;
                }
                if (LangUtil.isTruthy(!Extensions.operEq(c, '\r'))) {
                    current += c;
                }
            }
            if (LangUtil.isTruthy(comment >= 2)) {
                
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\\'))) ? (((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt)))) : (Extensions.operEq(c, '\\')))) {
                backslash = !LangUtil.isTruthy(backslash);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\''))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (Extensions.operEq(c, '\'')))) {
                sq = !LangUtil.isTruthy(sq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '"'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (Extensions.operEq(c, '"')))) {
                dq = !LangUtil.isTruthy(dq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '`'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (backslash))))) : (Extensions.operEq(c, '`')))) {
                bt = !LangUtil.isTruthy(bt);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '('))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (Extensions.operEq(c, '(')))) {
                ++ rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ')'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (Extensions.operEq(c, ')')))) {
                -- rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '['))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (Extensions.operEq(c, '[')))) {
                ++ sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ']'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (Extensions.operEq(c, ']')))) {
                -- sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '/'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt))))) : (Extensions.operEq(c, '/')))) {
                if (LangUtil.isTruthy(++ comment >= 2)) {
                    current = current.substring(0 , current.length() - 2);
                }
            }
            if (LangUtil.isTruthy(Extensions.operEq(c, '\n'))) {
                comment = 0;
                lastIndent = indent;
                indent = 0;
            }
            if (LangUtil.isTruthy(!Extensions.operEq(c, '\\'))) {
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
            if (LangUtil.isTruthy(Extensions.operEq(c, ' '))) {
                indent += c;
            }
            else if (LangUtil.isTruthy(Extensions.operEq(c, '\t'))) {
                indent += "    ";
            }
            else {
                break;
            }
        }
        tok.add(new Token(Token.Type.INDENT , indent));
        if (LangUtil.isTruthy(Extensions.operEq(i, line.length()))) {
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
            if (LangUtil.isTruthy(((LangUtil.isTruthy(((LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(type, lastType))) ? (!Extensions.operEq(type, CharType.W)) : (!Extensions.operEq(type, lastType)))) ? ((LangUtil.isTruthy(!Extensions.operEq(type, lastType))) ? (!Extensions.operEq(type, CharType.W)) : (!Extensions.operEq(type, lastType))) : (Extensions.operEq(type, CharType.S))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(("+-*/=><!.?".contains("" + lastChar)))) ? (Extensions.operEq(c, '=')) : (("+-*/=><!.?".contains("" + lastChar))))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '_'))) ? (Extensions.operEq(type, CharType.A)) : (Extensions.operEq(lastChar, '_')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.A))) ? (Extensions.operEq(c, '_')) : (Extensions.operEq(lastType, CharType.A)))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.A))) ? (Extensions.operEq(type, CharType.D)) : (Extensions.operEq(lastType, CharType.A)))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(type, CharType.A))) ? (Extensions.operEq(lastType, CharType.D)) : (Extensions.operEq(type, CharType.A)))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.D))) ? (Extensions.operEq(c, '.')) : (Extensions.operEq(lastType, CharType.D)))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '.'))) ? (Extensions.operEq(type, CharType.D)) : (Extensions.operEq(lastChar, '.')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '-'))) ? (Extensions.operEq(c, '>')) : (Extensions.operEq(lastChar, '-')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '='))) ? (Extensions.operEq(c, '>')) : (Extensions.operEq(lastChar, '=')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '_'))) ? (Extensions.operEq(c, '_')) : (Extensions.operEq(lastChar, '_')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '+'))) ? (Extensions.operEq(c, '+')) : (Extensions.operEq(lastChar, '+')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '-'))) ? (Extensions.operEq(c, '-')) : (Extensions.operEq(lastChar, '-')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '*'))) ? (Extensions.operEq(c, '*')) : (Extensions.operEq(lastChar, '*')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '?'))) ? (Extensions.operEq(c, '?')) : (Extensions.operEq(lastChar, '?')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '.'))) ? (Extensions.operEq(c, '.')) : (Extensions.operEq(lastChar, '.')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '&'))) ? (Extensions.operEq(c, '&')) : (Extensions.operEq(lastChar, '&')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '|'))) ? (Extensions.operEq(c, '|')) : (Extensions.operEq(lastChar, '|')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '\''))) ? (Extensions.operEq(c, '\'')) : (Extensions.operEq(lastChar, '\'')))))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '"'))) ? (Extensions.operEq(c, '"')) : (Extensions.operEq(lastChar, '"')))))) ? (!LangUtil.isTruthy(current.isBlank())) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '"'))) ? (Extensions.operEq(c, '"')) : (Extensions.operEq(lastChar, '"')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '\''))) ? (Extensions.operEq(c, '\'')) : (Extensions.operEq(lastChar, '\'')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '|'))) ? (Extensions.operEq(c, '|')) : (Extensions.operEq(lastChar, '|')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '&'))) ? (Extensions.operEq(c, '&')) : (Extensions.operEq(lastChar, '&')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '.'))) ? (Extensions.operEq(c, '.')) : (Extensions.operEq(lastChar, '.')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '?'))) ? (Extensions.operEq(c, '?')) : (Extensions.operEq(lastChar, '?')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '*'))) ? (Extensions.operEq(c, '*')) : (Extensions.operEq(lastChar, '*')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '-'))) ? (Extensions.operEq(c, '-')) : (Extensions.operEq(lastChar, '-')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '+'))) ? (Extensions.operEq(c, '+')) : (Extensions.operEq(lastChar, '+')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '_'))) ? (Extensions.operEq(c, '_')) : (Extensions.operEq(lastChar, '_')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '='))) ? (Extensions.operEq(c, '>')) : (Extensions.operEq(lastChar, '=')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '-'))) ? (Extensions.operEq(c, '>')) : (Extensions.operEq(lastChar, '-')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '.'))) ? (Extensions.operEq(type, CharType.D)) : (Extensions.operEq(lastChar, '.')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.D))) ? (Extensions.operEq(c, '.')) : (Extensions.operEq(lastType, CharType.D)))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(type, CharType.A))) ? (Extensions.operEq(lastType, CharType.D)) : (Extensions.operEq(type, CharType.A)))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.A))) ? (Extensions.operEq(type, CharType.D)) : (Extensions.operEq(lastType, CharType.A)))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastType, CharType.A))) ? (Extensions.operEq(c, '_')) : (Extensions.operEq(lastType, CharType.A)))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(lastChar, '_'))) ? (Extensions.operEq(type, CharType.A)) : (Extensions.operEq(lastChar, '_')))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(("+-*/=><!.?".contains("" + lastChar)))) ? (Extensions.operEq(c, '=')) : (("+-*/=><!.?".contains("" + lastChar))))))) : (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))))) : (((LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(type, lastType))) ? (!Extensions.operEq(type, CharType.W)) : (!Extensions.operEq(type, lastType)))) ? ((LangUtil.isTruthy(!Extensions.operEq(type, lastType))) ? (!Extensions.operEq(type, CharType.W)) : (!Extensions.operEq(type, lastType))) : (Extensions.operEq(type, CharType.S))))))) {
                tok.add(Token.fromString(current.trim()));
                current = "";
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\\'))) ? (((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt)))) : (Extensions.operEq(c, '\\')))) {
                backslash = !LangUtil.isTruthy(backslash);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '\''))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (Extensions.operEq(c, '\'')))) {
                sq = !LangUtil.isTruthy(sq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '"'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(bt)) ? (bt) : (backslash))))) : (Extensions.operEq(c, '"')))) {
                dq = !LangUtil.isTruthy(dq);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '`'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (backslash))))) : (Extensions.operEq(c, '`')))) {
                bt = !LangUtil.isTruthy(bt);
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '('))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (Extensions.operEq(c, '(')))) {
                ++ rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ')'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(sb > 0)) ? (sb > 0) : (cb > 0))))))) : (Extensions.operEq(c, ')')))) {
                -- rb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '['))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (Extensions.operEq(c, '[')))) {
                ++ sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, ']'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : ((LangUtil.isTruthy(bt)) ? (bt) : ((LangUtil.isTruthy(rb > 0)) ? (rb > 0) : (cb > 0))))))) : (Extensions.operEq(c, ']')))) {
                -- sb;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(c, '/'))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(sq)) ? (sq) : ((LangUtil.isTruthy(dq)) ? (dq) : (bt))))) : (Extensions.operEq(c, '/')))) {
                if (LangUtil.isTruthy(++ comment >= 2)) {
                    tok.remove(tok.size() - 1);
                    break;
                }
            }
            if (LangUtil.isTruthy(comment <= 1)) {
                if (LangUtil.isTruthy((LangUtil.isTruthy(comment > 0)) ? (!Extensions.operEq(c, '/')) : (comment > 0))) {
                    comment = 0;
                }
                current += c;
            }
            if (LangUtil.isTruthy(!Extensions.operEq(c, '\\'))) {
                backslash = false;
            }
            lastChar = c;
        }
        if (LangUtil.isTruthy(current)) { tok.add(Token.fromString(current.trim())); }
        tok = Parser.convertIdentifiers(tok);
        return tok;
    }
    public static ArrayList < String > extractArgsFromExpr(String s) {
        var args = new ArrayList < String > ();
        var tok = Tokeniser.tokLine(s.substring(1 , s.length() - 1));
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.ID))) { args.add(t.value); }
        }
        return args;
    }
    public static ArrayList < ArrayList < Token > > extractCommaExpr(String s) {
        var elems = new ArrayList < ArrayList < Token > > ();
        var tok = Tokeniser.tokLine(s.substring(1 , s.length() - 1));
        var current = new ArrayList < Token > ();
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.COMMA))) {
                elems.add(current);
                current = new ArrayList < Token > ();
            }
            else if (LangUtil.isTruthy(!Extensions.operEq(t.type, Token.Type.INDENT))) {
                current.add(t);
            }
        }
        elems.add(current);
        return elems;
    }
}

