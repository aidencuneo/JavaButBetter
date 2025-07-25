import java.io.*;
import java.util.*;

public class Tokeniser {
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

        for (int i = 0; i < lines.size(); ++i) {
            System.out.println(i + ": " + lines.get(i));
        }

        return lines;
    }

    public static void tokLine(String file) {
        
    }
}
