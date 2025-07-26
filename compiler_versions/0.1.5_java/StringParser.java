import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class StringParser {
    // public static String unescapeString(String str) {
    //     try {
    //         Properties prop = new Properties();
    //         prop.load(new StringReader("x=" + str));
    //         return prop.getProperty("x");
    //     } catch (IOException e) {
    //         return null;
    //     }
    // }

    public static String unescapeString(String str) {
        String out = "";

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            if (ch == '\\' && i + 1 < str.length()) {
                char next = str.charAt(i + 1);
                ++i;

                if (next == 'u' && i + 4 < str.length()) {
                    try {
                        int code = Integer.parseInt(str.substring(i + 1, i + 5), 16);
                        out += (char) code;
                        i += 4;
                    } catch (NumberFormatException e) {
                        out += "\\u";
                    }
                } else {
                    switch (next) {
                        case 'b' -> out += '\b';
                        case 'f' -> out += '\f';
                        case 'n' -> out += '\n';
                        case 'r' -> out += '\r';
                        case 't' -> out += '\t';
                        case '\"' -> out += '\"';
                        case '\'' -> out += '\'';
                        case '\\' -> out += '\\';
                        default -> out += next;
                    }
                }
            } else
                out += ch;
        }

        return out;
    }

    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }
}
