import java.io.*;
import java.util.*;

public class Compiler {
    public static String compile(String code) {
        ArrayList<String> lines = Tokeniser.splitFile(code);

        for (int i = 0; i < lines.size(); ++i) {
            ArrayList<String> tok = Tokeniser.tokLine(lines.get(i));
            System.out.println(i + ": " + LangUtil.d(tok));
        }

        return code;
    }
}
