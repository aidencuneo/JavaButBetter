public class Token {
    public enum TokenType {
        BLANK,
        INDENT,
        ID,
    }

    public TokenType type;
    public String value;

    public Token(TokenType type, String value) {

    }
}
