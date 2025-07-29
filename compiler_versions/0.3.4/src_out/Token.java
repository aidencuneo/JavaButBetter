import java.io.*;
import java.util.*;

public class Token {
    public enum Type {
        BLANK,
        INDENT,
        ID,
        SCOPE,
        SYMBOL,
        EXPR,
        SQUARE_EXPR,
        BRACE_EXPR,
        OPTION,
        NULL,
        TRUE,
        FALSE,
        NUM,
        STRING,
        CHAR,
        TEMPLATE_STRING,
        UNARY_OPER,
        BIN_OPER,
        ASSIGN,
        COMP_ASSIGN,
        DOT,
        COMMA,
        RANGE,
        LAMBDA,
        ARROW,
        INCREMENT,
        DECREMENT,
        INLINE,
        RETURN,
        BREAK,
        CONTINUE,
        PASS,
        IF,
        ELIF,
        ELSE,
        WHILE,
        UNTIL,
        FOR,
        IN,
        TRY,
        CATCH,
        DEFAULT,
        ACCESS_MOD,
        STATIC,
        INSTANCE,
        IMPORT,
        CLASS,
        ENUM,
        ALIAS,
    }
    public Type type;
    public String value;
    public Token(Type type , String value) {
        this . type = type;
        this . value = value;
    }
    public Token() {
        this . type = Type.BLANK;
        this . value = "";
    }
    public static static Token fromString(String v) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(v))) { return new Token(Type.BLANK, v); }
        Type t;
        char f = v.charAt(0);
        char l = v.charAt(v.length() - 1);
        if (LangUtil.isTruthy(Extensions.operEq(v.length(), 0))) {
            t = Type.BLANK;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.length() > 0)) ? (v.isBlank()) : (v.length() > 0))) {
            t = Type.INDENT;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, ":"))) {
            t = Type.SCOPE;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "option"))) {
            t = Type.OPTION;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "null"))) {
            t = Type.NULL;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "true"))) {
            t = Type.TRUE;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "false"))) {
            t = Type.FALSE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '"'))) ? (Extensions.operEq(l, '"')) : (Extensions.operEq(f, '"')))) {
            t = Type.STRING;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.startsWith("'''"))) ? (v.endsWith("'''")) : (v.startsWith("'''")))) {
            String vNoQuotes = v.substring(3, v.length() - 3);
            String realStr = StringParser.unescapeString(vNoQuotes);
            if (LangUtil.isTruthy(!Extensions.operEq(realStr.length(), 1))) {
                t = Type.STRING;
                v = "\"\"\"" + vNoQuotes + "\"\"\"";
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '\''))) ? (Extensions.operEq(l, '\'')) : (Extensions.operEq(f, '\'')))) {
            String vNoQuotes = v.substring(1, v.length() - 1);
            String realStr = StringParser.unescapeString(vNoQuotes);
            if (LangUtil.isTruthy(!Extensions.operEq(realStr.length(), 1))) {
                t = Type.STRING;
                v = '"' + StringParser.escapeDoubleQuotes(vNoQuotes) + '"';
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '`'))) ? (Extensions.operEq(l, '`')) : (Extensions.operEq(f, '`')))) {
            t = Type.TEMPLATE_STRING;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '('))) ? (Extensions.operEq(l, ')')) : (Extensions.operEq(f, '(')))) {
            t = Type.EXPR;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '['))) ? (Extensions.operEq(l, ']')) : (Extensions.operEq(f, '[')))) {
            t = Type.SQUARE_EXPR;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(f, '{'))) ? (Extensions.operEq(l, '}')) : (Extensions.operEq(f, '{')))) {
            t = Type.BRACE_EXPR;
        }
        else if (LangUtil.isTruthy((List.of("+", "-", "*", "/", "%", "&", "|", "and", "or", "xor", "nor", "&&", "||", "^", ">", "<", ">=", "<=", "==", "!=", "is").contains(v)))) {
            t = Type.BIN_OPER;
        }
        else if (LangUtil.isTruthy((List.of("!", "~", "not").contains(v)))) {
            t = Type.UNARY_OPER;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "="))) {
            t = Type.ASSIGN;
        }
        else if (LangUtil.isTruthy(v.endsWith("="))) {
            t = Type.COMP_ASSIGN;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "."))) {
            t = Type.DOT;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, ","))) {
            t = Type.COMMA;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, ".."))) {
            t = Type.RANGE;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "=>"))) {
            t = Type.LAMBDA;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "->"))) {
            t = Type.ARROW;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "++"))) {
            t = Type.INCREMENT;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "--"))) {
            t = Type.DECREMENT;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "inline"))) {
            t = Type.INLINE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "ret"))) ? (Extensions.operEq(v, "ret")) : (Extensions.operEq(v, "return")))) {
            t = Type.RETURN;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "break"))) {
            t = Type.BREAK;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "continue"))) ? (Extensions.operEq(v, "continue")) : (Extensions.operEq(v, "next")))) {
            t = Type.CONTINUE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "pass"))) ? (Extensions.operEq(v, "pass")) : (Extensions.operEq(v, "...")))) {
            t = Type.PASS;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "if"))) {
            t = Type.IF;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "elif"))) {
            t = Type.ELIF;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "else"))) {
            t = Type.ELSE;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "while"))) {
            t = Type.WHILE;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "until"))) {
            t = Type.UNTIL;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "for"))) {
            t = Type.FOR;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "in"))) {
            t = Type.IN;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "try"))) {
            t = Type.TRY;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "catch"))) ? (Extensions.operEq(v, "catch")) : (Extensions.operEq(v, "except")))) {
            t = Type.CATCH;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "default"))) {
            t = Type.DEFAULT;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "public"))) ? (Extensions.operEq(v, "public")) : ((LangUtil.isTruthy(Extensions.operEq(v, "private"))) ? (Extensions.operEq(v, "private")) : ((LangUtil.isTruthy(Extensions.operEq(v, "protected"))) ? (Extensions.operEq(v, "protected")) : (Extensions.operEq(v, "internal")))))) {
            t = Type.ACCESS_MOD;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "static"))) {
            t = Type.STATIC;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "instance"))) {
            t = Type.INSTANCE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "import"))) ? (Extensions.operEq(v, "import")) : ((LangUtil.isTruthy(Extensions.operEq(v, "include"))) ? (Extensions.operEq(v, "include")) : ((LangUtil.isTruthy(Extensions.operEq(v, "require"))) ? (Extensions.operEq(v, "require")) : (Extensions.operEq(v, "use")))))) {
            t = Type.IMPORT;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "class"))) {
            t = Type.CLASS;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "enum"))) {
            t = Type.ENUM;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(v, "alias"))) {
            t = Type.ALIAS;
        }
        else if (LangUtil.isTruthy(isAlpha(v))) {
            t = Type.ID;
        }
        else if (LangUtil.isTruthy(isNum(v))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(v.contains("."))) ? (!LangUtil.isTruthy(((LangUtil.isTruthy(v.endsWith("f"))) ? (v.endsWith("f")) : (v.endsWith("d"))))) : (v.contains(".")))) {
                v += Options.defaultDecimal;
            }
            t = Type.NUM;
        }
        else {
            t = Type.SYMBOL;
        }
        return new Token(t, v);
    }
    public static static boolean isAlpha(String s) {
        return s.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
    public static static boolean isNum(String s) {
        return s.matches("^[0-9]*\\.?[0-9]*(f|d)?$");
    }
    public String toString() {
        return type + ":" + value;
    }
}

