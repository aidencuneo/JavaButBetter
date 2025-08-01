import java.util.List;

public class Token {
    public enum Type {
        BLANK,
        INDENT,
        ID,
        SCOPE,
        SYMBOL,
        EXPR,
        OPTION,
        NULL,
        TRUE,
        FALSE,
        NUM,
        STRING,
        CHAR,
        TEMPLATE_STRING,
        ASSIGN,
        COMP_ASSIGN,
        UNARY_OPER,
        BIN_OPER,
        DOT,
        COMMA,
        HASH,
        LAMBDA, // =>
        ARROW,  // ->
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
        ACCESS_MOD,
        STATIC,
        IMPORT,
        CLASS,
        ENUM,
        ALIAS,
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
        // System.out.println(f + ", " + l);

        if (v.length() == 0)
            t = Type.BLANK;
        else if (v.length() > 0 && v.isBlank())
            t = Type.INDENT;
        else if (v.equals(":"))
            t = Type.SCOPE;
        else if (v.equals("option"))
            t = Type.OPTION;
        else if (v.equals("null"))
            t = Type.NULL;
        else if (v.equals("true"))
            t = Type.TRUE;
        else if (v.equals("false"))
            t = Type.FALSE;
        else if (f == '"' && l == '"')
            t = Type.STRING;
        else if (v.startsWith("'''") && v.endsWith("'''")) {
            String vNoQuotes = v.substring(3, v.length() - 3);
            String realStr = StringParser.unescapeString(vNoQuotes);

            if (realStr.length() != 1) {
                t = Type.STRING;
                v = "\"\"\"" + vNoQuotes + "\"\"\"";
            } else
                t = Type.CHAR;
        } else if (f == '\'' && l == '\'') {
            String vNoQuotes = v.substring(1, v.length() - 1);
            String realStr = StringParser.unescapeString(vNoQuotes);

            if (realStr.length() != 1) {
                t = Type.STRING;
                v = '"' + StringParser.escapeDoubleQuotes(vNoQuotes) + '"';
            } else
                t = Type.CHAR;
        } else if (f == '`' && l == '`') {
            t = Type.TEMPLATE_STRING;
        } else if (f == '(' && l == ')')
            t = Type.EXPR;
        else if (v.equals("="))
            t = Type.ASSIGN;
        else if (v.endsWith("="))
            t = Type.COMP_ASSIGN;
        else if (v.equals("."))
            t = Type.DOT;
        else if (v.equals(","))
            t = Type.COMMA;
        else if (v.equals("#"))
            t = Type.HASH;
        else if (v.equals("=>"))
            t = Type.LAMBDA;
        else if (v.equals("->"))
            t = Type.ARROW;
        else if (v.equals("++"))
            t = Type.INCREMENT;
        else if (v.equals("--"))
            t = Type.DECREMENT;
        else if (v.equals("inline"))
            t = Type.INLINE;
        else if (v.equals("ret") || v.equals("return"))
            t = Type.RETURN;
        else if (v.equals("break"))
            t = Type.BREAK;
        else if (v.equals("continue") || v.equals("next"))
            t = Type.CONTINUE;
        else if (v.equals("pass") || v.equals("..."))
            t = Type.PASS;
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
        else if (v.equals("catch") || v.equals("except"))
            t = Type.CATCH;
        else if (v.equals("public") || v.equals("private") || v.equals("protected") || v.equals("internal"))
            t = Type.ACCESS_MOD;
        else if (v.equals("static"))
            t = Type.STATIC;
        else if (v.equals("import") || v.equals("include") || v.equals("require") || v.equals("use"))
            t = Type.IMPORT;
        else if (v.equals("class"))
            t = Type.CLASS;
        else if (v.equals("enum"))
            t = Type.ENUM;
        else if (v.equals("alias"))
            t = Type.ALIAS;
        else if (v.equals("string")) {
            // string transforms into String
            v = "String";
            t = Type.ID;
        } else if (v.equals("bool")) {
            // bool transforms into boolean
            v = "boolean";
            t = Type.ID;
        } else if (List.of(
            "+", "-", "*", "/", "%", "&", "|",
            "and", "or", "xor", "nor",
            "&&", "||", "^",
            ">", "<", ">=", "<=",
            "==", "!=", "is").contains(v)
        )
            t = Type.BIN_OPER;
        else if (List.of(
            "!", "~", "not").contains(v)
        )
            t = Type.UNARY_OPER;
        else if (isAlpha(v))
            t = Type.ID;
        else if (isNum(v)) {
            if (v.contains(".") && !(v.endsWith("f") || v.endsWith("d")))
                v += Options.defaultDecimal;
            t = Type.NUM;
        }
        else
            t = Type.SYMBOL;

        return new Token(t, v);
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
