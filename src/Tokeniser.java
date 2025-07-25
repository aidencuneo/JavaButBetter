import java.io.*;
import java.util.*;

public class Tokeniser {
    public enum CharType {
        A, D, S, W,
    }

    public static CharType charType(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
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

            if (c == '\n') {
                lines.add(current);
                current = "";
            } else {
                current += c;
            }
        }

        lines.add(current);

        return lines;
    }

    public static ArrayList<String> tokLine(String line) {
        ArrayList<String> lines = new ArrayList<>();

        if (line.isEmpty())
            return lines;

        String current = "";
        CharType type = charType(line.charAt(0));
        CharType lastType;

        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            lastType = type;
            type = charType(c);

            // Break
            if (type != lastType) {
                lines.add(current);
                current = "" + c;
            } else {
                current += c;
            }
        }

        lines.add(current);

        return lines;
    }
}
