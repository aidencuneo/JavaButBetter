import java.io.*;
import java.util.*;

public class Token {
    public enum Type {
        BLANK ,
        INDENT ,
        ID ,
        SCOPE ,
        SYMBOL ,
        EXPR ,
        OPTION ,
        NULL ,
        TRUE ,
        FALSE ,
        NUM ,
        STRING ,
        CHAR ,
        TEMPLATE_STRING ,
        UNARY_OPER ,
        BIN_OPER ,
        ASSIGN ,
        COMP_ASSIGN ,
        DOT ,
        COMMA ,
        HASH ,
        LAMBDA ,
        ARROW ,
        INCREMENT ,
        DECREMENT ,
        INLINE ,
        RETURN ,
        BREAK ,
        CONTINUE ,
        PASS ,
        IF ,
        ELIF ,
        ELSE ,
        WHILE ,
        UNTIL ,
        FOR ,
        IN ,
        TRY ,
        CATCH ,
        ACCESS_MOD ,
        STATIC ,
        IMPORT ,
        CLASS ,
        ENUM ,
        ALIAS ,
    }
    public Type type;
    public String value;
    public Token(Type type , String value) {
        this . type = type;
        this . value = value;
    }
    public static Token fromString(String v) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(v))) { return new Token(Type.BLANK , v); }
        Type t;
        char f = v.charAt(0);
        char l = v.charAt(v.length() - 1);
        if (LangUtil.isTruthy(v.length() == 0)) {
            t = Type.BLANK;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.length() > 0)) ? (v.isBlank()) : (v.length() > 0))) {
            t = Type.INDENT;
        }
        else if (LangUtil.isTruthy(v.equals(":"))) {
            t = Type.SCOPE;
        }
        else if (LangUtil.isTruthy(v.equals("option"))) {
            t = Type.OPTION;
        }
        else if (LangUtil.isTruthy(v.equals("null"))) {
            t = Type.NULL;
        }
        else if (LangUtil.isTruthy(v.equals("true"))) {
            t = Type.TRUE;
        }
        else if (LangUtil.isTruthy(v.equals("false"))) {
            t = Type.FALSE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(f == '"')) ? (l == '"') : (f == '"'))) {
            t = Type.STRING;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.startsWith("'''"))) ? (v.endsWith("'''")) : (v.startsWith("'''")))) {
            String vNoQuotes = v.substring(3 , v.length() - 3);
            String realStr = StringParser.unescapeString(vNoQuotes);
            if (LangUtil.isTruthy(realStr.length() != 1)) {
                t = Type.STRING;
                v = "\"\"\"" + vNoQuotes + "\"\"\"";
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(f == '\'')) ? (l == '\'') : (f == '\''))) {
            String vNoQuotes = v.substring(1 , v.length() - 1);
            String realStr = StringParser.unescapeString(vNoQuotes);
            if (LangUtil.isTruthy(realStr.length() != 1)) {
                t = Type.STRING;
                v = '"' + StringParser.escapeDoubleQuotes(vNoQuotes) + '"';
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(f == '`')) ? (l == '`') : (f == '`'))) {
            t = Type.TEMPLATE_STRING;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(f == '(')) ? (l == ')') : (f == '('))) {
            t = Type.EXPR;
        }
        else if (LangUtil.isTruthy((List.of("+" , "-" , "*" , "/" , "%" , "&" , "|" , "and" , "or" , "xor" , "nor" , "&&" , "||" , "^" , ">" , "<" , ">=" , "<=" , "==" , "!=" , "is").contains(v)))) {
            t = Type.BIN_OPER;
        }
        else if (LangUtil.isTruthy((List.of("!" , "~" , "not").contains(v)))) {
            t = Type.UNARY_OPER;
        }
        else if (LangUtil.isTruthy(v.equals("="))) {
            t = Type.ASSIGN;
        }
        else if (LangUtil.isTruthy(v.endsWith("="))) {
            t = Type.COMP_ASSIGN;
        }
        else if (LangUtil.isTruthy(v.equals("."))) {
            t = Type.DOT;
        }
        else if (LangUtil.isTruthy(v.equals(","))) {
            t = Type.COMMA;
        }
        else if (LangUtil.isTruthy(v.equals("#"))) {
            t = Type.HASH;
        }
        else if (LangUtil.isTruthy(v.equals("=>"))) {
            t = Type.LAMBDA;
        }
        else if (LangUtil.isTruthy(v.equals("->"))) {
            t = Type.ARROW;
        }
        else if (LangUtil.isTruthy(v.equals("++"))) {
            t = Type.INCREMENT;
        }
        else if (LangUtil.isTruthy(v.equals("--"))) {
            t = Type.DECREMENT;
        }
        else if (LangUtil.isTruthy(v.equals("inline"))) {
            t = Type.INLINE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("ret"))) ? (v.equals("ret")) : (v.equals("return")))) {
            t = Type.RETURN;
        }
        else if (LangUtil.isTruthy(v.equals("break"))) {
            t = Type.BREAK;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("continue"))) ? (v.equals("continue")) : (v.equals("next")))) {
            t = Type.CONTINUE;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("pass"))) ? (v.equals("pass")) : (v.equals("...")))) {
            t = Type.PASS;
        }
        else if (LangUtil.isTruthy(v.equals("if"))) {
            t = Type.IF;
        }
        else if (LangUtil.isTruthy(v.equals("elif"))) {
            t = Type.ELIF;
        }
        else if (LangUtil.isTruthy(v.equals("else"))) {
            t = Type.ELSE;
        }
        else if (LangUtil.isTruthy(v.equals("while"))) {
            t = Type.WHILE;
        }
        else if (LangUtil.isTruthy(v.equals("until"))) {
            t = Type.UNTIL;
        }
        else if (LangUtil.isTruthy(v.equals("for"))) {
            t = Type.FOR;
        }
        else if (LangUtil.isTruthy(v.equals("in"))) {
            t = Type.IN;
        }
        else if (LangUtil.isTruthy(v.equals("try"))) {
            t = Type.TRY;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("catch"))) ? (v.equals("catch")) : (v.equals("except")))) {
            t = Type.CATCH;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("public"))) ? (v.equals("public")) : ((LangUtil.isTruthy(v.equals("private"))) ? (v.equals("private")) : ((LangUtil.isTruthy(v.equals("protected"))) ? (v.equals("protected")) : (v.equals("internal")))))) {
            t = Type.ACCESS_MOD;
        }
        else if (LangUtil.isTruthy(v.equals("static"))) {
            t = Type.STATIC;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("import"))) ? (v.equals("import")) : ((LangUtil.isTruthy(v.equals("include"))) ? (v.equals("include")) : ((LangUtil.isTruthy(v.equals("require"))) ? (v.equals("require")) : (v.equals("use")))))) {
            t = Type.IMPORT;
        }
        else if (LangUtil.isTruthy(v.equals("class"))) {
            t = Type.CLASS;
        }
        else if (LangUtil.isTruthy(v.equals("enum"))) {
            t = Type.ENUM;
        }
        else if (LangUtil.isTruthy(v.equals("alias"))) {
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
        return new Token(t , v);
    }
    public static boolean isAlpha(String s) {
        return s.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
    public static boolean isNum(String s) {
        return s.matches("^[0-9]*\\.?[0-9]*(f|d)?$");
    }
    public String toString() {
        return type + ":" + value;
    }
}

