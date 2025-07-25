import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class StringParser {
    public static String unescapeString(String str) {
        try {
            Properties prop = new Properties();
            prop.load(new StringReader("x=" + str));
            return prop.getProperty("x");
        } catch (IOException e) {
            return null;
        }
    }

    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }
}
