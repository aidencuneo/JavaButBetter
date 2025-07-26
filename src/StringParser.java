import java.io.*;
import java.util.*;

public class StringParser {
    public static String unescapeString(String str) {
        try {
            var prop = new Properties ();
            prop.load (new StringReader ("x=" + str));
            return prop.getProperty ("x");
        }
        catch (IOException e) {
            return null;
        }
    }
    public static String escapeDoubleQuotes(String str) {
        return str.replace ("\"", " \ \ \ "");
    }
}

