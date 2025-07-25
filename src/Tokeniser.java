import java.io.*;
import java.util.*;

public class Tokeniser {
    public enum CharType {
        A, D, S, W,
    }

    public static CharType charType(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_')
            return CharType.A;
        else if (c >= '0' && c <= '9')
            return CharType.D;
        else if (c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f')
            return CharType.W;
        else
            return CharType.S;
    }

    public static ArrayList<String> splitFile(String file) {
        ArrayList<String> lines = new ArrayList<>();
        String current = "";

        for (int i = 0; i < file.length(); ++i) {
            char c = file.charAt(i);

            if (c == '\n' || c == ';') {
                if (current.length() > 0)
                    lines.add(current);

                current = "";
            } else if (c != '\r') {
                current += c;
            }
        }

        lines.add(current);

        return lines;
    }

    public static ArrayList<Token> tokLine(String line) {
        ArrayList<Token> tok = new ArrayList<>();

        if (line.isEmpty())
            return tok;

        // Gather indent for this line
        String indent = "";
        int i = 0;

        for (; i < line.length(); ++i) {
            char c = line.charAt(i);

            if (c == ' ')
                indent += c;
            else if (c == '\t')
                indent += "    ";
            else
                break;
        }

        // State
        String current = "";
        CharType type = charType(line.charAt(i));
        CharType lastType;
        char lastChar = 0;
        int comment = 0;

        // Context
        boolean sq = false;
        boolean dq = false;
        boolean bt = false;
        int rb = 0;
        int sb = 0;
        int cb = 0;

        // Add indent as the first token
        tok.add(new Token(Token.Type.INDENT, indent));

        // Start tokenising
        for (; i < line.length(); ++i) {
            char c = line.charAt(i);
            lastType = type;
            type = charType(c);
            // System.out.println(c + ", " + type);

            // Break
            if ((type != lastType && type != CharType.W) && !(
                sq || dq || bt || rb > 0 || sb > 0 || cb > 0
            // ) && !(
            //     // Join together operators: -= += *= /= == >= <= .=
            //     ("+-*/=><!.".contains("" + lastChar)) && c == '='
            // ) && !(
            //     lastType == CharType.A && c == '.' // Join together names like `word.upper` (second part is below)
            // ) && !(
            //     type == CharType.A && lastChar == '.' // Second part to the line above
            ) && !(
                lastChar == '_' && type == CharType.A // Join together names like `string_one` (second part is below)
            ) && !(
                lastType == CharType.A && c == '_' // Second part to the line above
            ) && !(
                lastType == CharType.A && type == CharType.D // Join alphabetical and numerical characters
            ) && !(
                type == CharType.A && lastType == CharType.D // Second part to the line above
            ) && !(
                lastType == CharType.D && c == '.' // Join decimals (first part)
            ) && !(
                lastChar == '.' && type == CharType.D // Join decimals (second part)
            ) && !(
                lastChar == '_' && c == '_' // Join together all `_` tokens
            ) && !current.isEmpty()) {
                tok.add(Token.fromString(current));
                current = "";
            }

            if (c == '\'' && !(dq || rb > 0 || sb > 0 || cb > 0))
                sq = !sq;
            else if (c == '"' && !(sq || bt || rb > 0 || sb > 0 || cb > 0))
                dq = !dq;
            else if (c == '`' && !(sq || dq || rb > 0 || sb > 0 || cb > 0))
                bt = !bt;
            else if (c == '(' && !(sq || dq || bt || sb > 0 || cb > 0))
                ++rb;
            else if (c == ')' && !(sq || dq || bt || sb > 0 || cb > 0))
                --rb;
            else if (c == '[' && !(sq || dq || bt || rb > 0 || cb > 0))
                ++sb;
            else if (c == ']' && !(sq || dq || bt || rb > 0 || cb > 0))
                --sb;
            else if (c == '/' && !(sq || dq || bt)) {
                if (++comment >= 2) {
                    current = "";
                    // current = current.substring(0, current.length() - 2);
                    break;
                }
            }

            if (type != CharType.W && comment <= 1) {
                if (comment > 0 && c != '/')
                    comment = 0;

                current += c;
            }

            lastChar = c;
        }

        if (!current.isEmpty())
            tok.add(Token.fromString(current));

        return tok;
    }
}
