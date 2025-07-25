public class Token {
    public enum TokenType {
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
    }

    public TokenType type;
    public String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Token fromString(String v) {
        if (v.isEmpty())
            return new Token(TokenType.BLANK, v);

        TokenType t;
        char f = v.charAt(0);
        char l = v.charAt(v.length() - 1);

        if (v.length() == 0)
            t = TokenType.BLANK;
        else if (v.length() > 0 && v.isBlank())
            t = TokenType.INDENT;
        else if (v.equals(":"))
            t = TokenType.SCOPE;
        else if (f == '"' && l == '"')
            t = TokenType.STRING;
        else if (f == '\'' && l == '\'')
            t = TokenType.CHAR; // TODO: Unless length of literal is not one
        else if (f == '(' && l == ')')
            t = TokenType.EXPR;
        else if (v.equals("="))
            t = TokenType.ASSIGN;
        else if (v.endsWith("="))
            t = TokenType.COMP_ASSIGN;
        else if (v.equals("."))
            t = TokenType.DOT;
        else if (v.equals("++"))
            t = TokenType.INCREMENT;
        else if (v.equals("--"))
            t = TokenType.DECREMENT;
        else if (v.equals("ret") || v.equals("return"))
            t = TokenType.RETURN;
        else if (v.equals("break"))
            t = TokenType.BREAK;
        else if (v.equals("continue") || v.equals("next"))
            t = TokenType.CONTINUE;
        else if (v.equals("if"))
            t = TokenType.IF;
        else if (v.equals("elif"))
            t = TokenType.ELIF;
        else if (v.equals("else"))
            t = TokenType.ELSE;
        else if (v.equals("while"))
            t = TokenType.WHILE;
        else if (v.equals("until"))
            t = TokenType.UNTIL;
        else if (v.equals("for"))
            t = TokenType.FOR;
        else if (v.equals("in"))
            t = TokenType.IN;
        else if (v.equals("try"))
            t = TokenType.TRY;
        else if (v.equals("catch"))
            t = TokenType.CATCH;
        else if (v.equals("public") || v.equals("private") || v.equals("protected") || v.equals("internal"))
            t = TokenType.ACCESS_MOD;
        else if (v.equals("static"))
            t = TokenType.STATIC;
        else if (v.equals("string")) {
            // string transforms into String
            v = "String";
            t = TokenType.ID;
        } else if (isAlpha(v))
            t = TokenType.ID;
        else if (isNum(v))
            t = TokenType.NUM;
        else
            t = TokenType.SYMBOL;

        return new Token(t, v);
    }

    public static boolean isAlpha(String s) {
        return s.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    public static boolean isNum(String s) {
        return s.matches("^[0-9]*$");
    }

    public String toString() {
        return type + ":" + value;
    }
}
