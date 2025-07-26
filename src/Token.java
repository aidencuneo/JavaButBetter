import java.io.*;
import java.util.*;
import java.lang.reflect.Array;

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
        ASSIGN ,
        COMP_ASSIGN ,
        UNARY_OPER ,
        BIN_OPER ,
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
    }
    public Type type;
    public String value;
    public Token(Type type , String value) {
        this . type = type;
        this . value = value;
    }
    public static Token fromString(String v) {
        if (LangUtil.isTruthy(v.isEmpty ())) {
            return new Token (Type.BLANK , v);
        }
        Type t;
        var f = v.charAt (0);
        var l = v.charAt (v.length () - 1);
        if (LangUtil.isTruthy(( == 0))) {
            t = Type.BLANK;
        }
        else if (LangUtil.isTruthy(v.length ()> 0 && v.isBlank ())) {
            t = Type.INDENT;
        }
        else if (LangUtil.isTruthy(v.equals (":"))) {
            t = Type.SCOPE;
        }
        else if (LangUtil.isTruthy(v.equals ("option"))) {
            t = Type.OPTION;
        }
        else if (LangUtil.isTruthy(v.equals ("null"))) {
            t = Type.NULL;
        }
        else if (LangUtil.isTruthy(v.equals ("true"))) {
            t = Type.TRUE;
        }
        else if (LangUtil.isTruthy(v.equals ("false"))) {
            t = Type.FALSE;
        }
        else if (LangUtil.isTruthy((f == ( == '"')))) {
            t = Type.STRING;
        }
        else if (LangUtil.isTruthy(v.startsWith ("'''")&& v.endsWith ("'''"))) {
            String vNoQuotes = v.substring (3 , v.length () - 3);
            String realStr = StringParser.unescapeString (vNoQuotes);
            if (LangUtil.isTruthy(( != 1))) {
                t = Type.STRING;
                v = "\" \ "\"" + StringParser.escapeDoubleQuotes(vNoQuotes) + " \ "\" \ "";
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((f == "\'' && l == " \ ""))) {
            String vNoQuotes = v.substring (1 , v.length () - 1);
            String realStr = StringParser.unescapeString (vNoQuotes);
            if (LangUtil.isTruthy(( != 1))) {
                t = Type.STRING;
                v = '"' + StringParser.escapeDoubleQuotes (vNoQuotes) + '"';
            }
            else {
                t = Type.CHAR;
            }
        }
        else if (LangUtil.isTruthy((f == ( == ')')))) {
            t = Type.EXPR;
        }
        else if (LangUtil.isTruthy(v.equals ("="))) {
            t = Type.ASSIGN;
        }
        else if (LangUtil.isTruthy(v.endsWith ("="))) {
            t = Type.COMP_ASSIGN;
        }
        else if (LangUtil.isTruthy(v.equals ("."))) {
            t = Type.DOT;
        }
        else if (LangUtil.isTruthy(v.equals (","))) {
            t = Type.COMMA;
        }
        else if (LangUtil.isTruthy(v.equals ("#"))) {
            t = Type.HASH;
        }
        else if (LangUtil.isTruthy(v.equals ("=>"))) {
            t = Type.LAMBDA;
        }
        else if (LangUtil.isTruthy(v.equals ("->"))) {
            t = Type.ARROW;
        }
        else if (LangUtil.isTruthy(v.equals ("++"))) {
            t = Type.INCREMENT;
        }
        else if (LangUtil.isTruthy(v.equals ("--"))) {
            t = Type.DECREMENT;
        }
        else if (LangUtil.isTruthy(v.equals ("inline"))) {
            t = Type.INLINE;
        }
        else if (LangUtil.isTruthy(v.equals ("ret")|| v.equals ("return"))) {
            t = Type.RETURN;
        }
        else if (LangUtil.isTruthy(v.equals ("break"))) {
            t = Type.BREAK;
        }
        else if (LangUtil.isTruthy(v.equals ("continue")|| v.equals ("next"))) {
            t = Type.CONTINUE;
        }
        else if (LangUtil.isTruthy(v.equals ("pass")|| v.equals ("..."))) {
            t = Type.PASS;
        }
        else if (LangUtil.isTruthy(v.equals ("if"))) {
            t = Type.IF;
        }
        else if (LangUtil.isTruthy(v.equals ("elif"))) {
            t = Type.ELIF;
        }
        else if (LangUtil.isTruthy(v.equals ("else"))) {
            t = Type.ELSE;
        }
        else if (LangUtil.isTruthy(v.equals ("while"))) {
            t = Type.WHILE;
        }
        else if (LangUtil.isTruthy(v.equals ("until"))) {
            t = Type.UNTIL;
        }
        else if (LangUtil.isTruthy(v.equals ("for"))) {
            t = Type.FOR;
        }
        else if (LangUtil.isTruthy(v.equals ("in"))) {
            t = Type.IN;
        }
        else if (LangUtil.isTruthy(v.equals ("try"))) {
            t = Type.TRY;
        }
        else if (LangUtil.isTruthy(v.equals ("catch")|| v.equals ("except"))) {
            t = Type.CATCH;
        }
        else if (LangUtil.isTruthy(v.equals ("public")|| v.equals ("private")|| v.equals ("protected")|| v.equals ("internal"))) {
            t = Type.ACCESS_MOD;
        }
        else if (LangUtil.isTruthy(v.equals ("static"))) {
            t = Type.STATIC;
        }
        else if (LangUtil.isTruthy(v.equals ("import")|| v.equals ("include")|| v.equals ("require")|| v.equals ("use"))) {
            t = Type.IMPORT;
        }
        else if (LangUtil.isTruthy(v.equals ("class"))) {
            t = Type.CLASS;
        }
        else if (LangUtil.isTruthy(v.equals ("string"))) {
            v = "String";
            t = Type.ID;
        }
        else if (LangUtil.isTruthy(v.equals ("bool"))) {
            v = "boolean";
            t = Type.ID;
        }
        else if (LangUtil.isTruthy(isAlpha (v))) {
            t = Type.ID;
        }
        else if (LangUtil.isTruthy(isNum (v))) {
            if (LangUtil.isTruthy(v.contains (".")&& ! (v.endsWith ("f")|| v.endsWith ("d")))) {
                v += Options.defaultDecimal;
            }
            t = Type.NUM;
        }
        else if (LangUtil.isTruthy(List.of ("+" , "-" , "*" , "/" , "%" , "not" , "and" , "or" , "xor" , "&" , "|" , "&&" , "||" , ">" , "<" , ">=" , "<=" , "==" , "!=").contains (v))) {
            t = Type.BIN_OPER;
        }
        else if (LangUtil.isTruthy(List.of ("!" , "~" , "not").contains (v))) {
            t = Type.UNARY_OPER;
        }
        else {
            t = Type.SYMBOL;
        }
        return new Token (t , v);
    }
    public static boolean isAlpha(String s) {
        return s.matches ("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
    public static boolean isNum(String s) {
        return s.matches ("^[0-9]*\\.?[0-9]*(f|d)?$");
    }
    public String toString() {
        return type + ":" + value;
    }
}

