import java.io.*;
import java.util.*;

public class Compiler {
    public static String compile(String className, String code) {
        ArrayList<String> lines = Tokeniser.splitFile(code);

        String startTemplate = "import java.io.*;\nimport java.util.*;\n\n";
        String endTemplate = "";
        String out = "";
        String currentClass = "";

        int scope = 0;

        for (int i = 0; i < lines.size(); ++i) {
            if (i > 0 && currentClass.isEmpty()) {
                startTemplate += "public class " + className + " {\n";
                endTemplate = "\n}";
                currentClass = className;
            }

            ArrayList<Token> tok = Tokeniser.tokLine(lines.get(i));

            for (int j = 0; j < tok.size(); ++j)
                out += tok.get(j).value + " ";
            out += ";\n";

            System.out.println(i + ": " + LangUtil.d(tok));
        }

        return startTemplate + out + endTemplate;
    }

    public static int findToken(ArrayList<Token> tok, Token.TokenType type) {
        for (int i = 0; i < tok.size(); ++i)
            if (tok.get(i).type == type)
                return i;
        return -1;
    }
}
