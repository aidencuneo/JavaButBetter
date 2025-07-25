
import java.lang.reflect.Array;
import java.util.List;

public class Token {
    public enum Type {
        BLANK,
        INDENT,
        ID,
        SCOPE,
        SYMBOL,
        EXPR,
        NUM,
        STRING,
        CHAR,
        ASSIGN,
        COMP_ASSIGN,
        UNARY_OPER,
        BIN_OPER,
        DOT,
        INCREMENT,
        DECREMENT,
        RETURN,
        BREAK,
        CONTINUE,
        IF,
        ELIF,
        ELSE,
        WHILE,
        UNTIL,
        FOR,
        IN,
        TRY,
        CATCH,
        ACCESS_MOD,
        STATIC,
        IMPORT,
        CLASS,
    }

    public Type type;
    public String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Token fromString(String v) {
        if (v.isEmpty())
            return new Token(Type.BLANK, v);

        Type t;
        char f = v.charAt(0);
        char l = v.charAt(v.length() - 1);

        if (v.length() == 0)
            t = Type.BLANK;
        else if (v.length() > 0 && v.isBlank())
            t = Type.INDENT;
        else if (v.equals(":"))
            t = Type.SCOPE;
        else if (f == '"' && l == '"')
            t = Type.STRING;
        else if (f == '\'' && l == '\'')
            t = Type.CHAR; // TODO: Unless length of literal is not one
        else if (f == '(' && l == ')')
            t = Type.EXPR;
        else if (v.equals("="))
            t = Type.ASSIGN;
        else if (v.endsWith("="))
            t = Type.COMP_ASSIGN;
        else if (v.equals("."))
            t = Type.DOT;
        else if (v.equals("++"))
            t = Type.INCREMENT;
        else if (v.equals("--"))
            t = Type.DECREMENT;
        else if (v.equals("ret") || v.equals("return"))
            t = Type.RETURN;
        else if (v.equals("break"))
            t = Type.BREAK;
        else if (v.equals("continue") || v.equals("next"))
            t = Type.CONTINUE;
        else if (v.equals("if"))
            t = Type.IF;
        else if (v.equals("elif"))
            t = Type.ELIF;
        else if (v.equals("else"))
            t = Type.ELSE;
        else if (v.equals("while"))
            t = Type.WHILE;
        else if (v.equals("until"))
            t = Type.UNTIL;
        else if (v.equals("for"))
            t = Type.FOR;
        else if (v.equals("in"))
            t = Type.IN;
        else if (v.equals("try"))
            t = Type.TRY;
        else if (v.equals("catch"))
            t = Type.CATCH;
        else if (v.equals("public") || v.equals("private") || v.equals("protected") || v.equals("internal"))
            t = Type.ACCESS_MOD;
        else if (v.equals("static"))
            t = Type.STATIC;
        else if (v.equals("import") || v.equals("include") || v.equals("require") || v.equals("use"))
            t = Type.IMPORT;
        else if (v.equals("class"))
            t = Type.CLASS;
        else if (v.equals("string")) {
            // string transforms into String
            v = "String";
            t = Type.ID;
        } else if (isAlpha(v))
            t = Type.ID;
        else if (isNum(v))
            t = Type.NUM;
        else if (List.of(
            "+", "-", "*", "/", "%",
            "not", "and", "or", "xor", "&", "|", "&&", "||", ">", "<", ">=", "<=", "==", "!=").contains(v)
        )
            t = Type.BIN_OPER;
        else if (List.of(
            "!", "~", "not").contains(v)
        )
            t = Type.UNARY_OPER;
        else
            t = Type.SYMBOL;

        return new Token(t, v);
    }

    public static boolean isAlpha(String s) {
        return s.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    public static boolean isNum(String s) {
        return s.matches("^[0-9]*\\.?[0-9]*$");
    }

    public String toString() {
        return type + ":" + value;
    }
}
