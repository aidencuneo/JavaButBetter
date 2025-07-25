import java.io.*;
import java.util.*;

public class Compiler {
    public static String compile(String className, String code) {
        ArrayList<String> lines = Tokeniser.splitFile(code);

        String startTemplate = "import java.io.*;\nimport java.util.*;\n";
        String endTemplate = "";

        String currentClass = className;
        HashMap<String, String> outClasses = new HashMap<>();
        HashMap<String, AccessMod> outClassAccess = new HashMap<>();
        outClassAccess.put(className, AccessMod.PUBLIC);

        int scope = 0;
        int lastIndent = 0;

        for (int i = 0; i < lines.size(); ++i) {
            String out = outClasses.getOrDefault(currentClass, "");

            ArrayList<Token> tok = Tokeniser.tokLine(lines.get(i));
            int indent = tok.get(0).value.length();
            tok.remove(0);

            ArrayList<Token.Type> types = getTokenTypes(tok);

            if (tok.size() == 0)
                continue;

            Token.Type startTok = types.get(0);
            Token.Type endTok = Util.get(types, -1);
            int f = -1;

            // Indentation controls braces
            while (indent > lastIndent) {
                lastIndent += 4;
                out = out.trim();
                out += " {\n";
            }

            while (indent < lastIndent) {
                out += " ".repeat(lastIndent - indent) + "}\n";
                lastIndent -= 4;
            }

            // Add indentation for every line
            out += " ".repeat(indent + 4);

            // Imports
            if (startTok == Token.Type.IMPORT) {
                String importStr = "import ";
                for (int j = 1; j < tok.size(); ++j)
                    importStr += tok.get(j).value;
                startTemplate += importStr + ";\n";
            }

            // Classes
            else if ((f = findToken(tok, Token.Type.CLASS)) != -1 && f + 1 < tok.size()) {
                // Set current class
                currentClass = tok.get(f + 1).value;
                out = outClasses.getOrDefault(currentClass, "");
                AccessMod accessMod = AccessMod.DEFAULT;

                // out = out.trim();
                // out += "\n}\n\n";

                // Get access modifier from tokens before 'class'
                for (int j = 0; j < f && accessMod == AccessMod.DEFAULT; ++j)
                    accessMod = MethodAccess.accessModFromToken(tok.get(j));

                // Set access modifier for this class
                outClassAccess.put(currentClass, accessMod);

                // out += "{";
            }

            // Scoped statements - loops, methods, classes, etc.
            else if (endTok == Token.Type.SCOPE) {
                if (false) {}

                // Methods
                else {
                    // access?, static?, returntype|void, name, expr?, scope
                    MethodAccess methodAccess = getMethodAccess(tok);

                    for (int j = 0; j < tok.size() - 1; ++j)
                        out += tok.get(j).value + " ";
                }
                // tok.remove(tok.size() - 1);
            }

            // Assignments
            else if ((f = findToken(tok, Token.Type.ASSIGN)) != -1) {
                String vartype = "var";
                String varname = "";

                // type name = value
                if (f == 2) {
                    vartype = tok.get(0).value;
                    varname = tok.get(1).value;
                }

                // name = value
                else if (f == 1)
                    varname = tok.get(0).value;

                out += vartype + " " + varname + " = ";

                // Add compiled value
                for (int j = f + 1; j < tok.size(); ++j)
                    out += tok.get(j).value + " ";

                // Line ending
                out += ";";
            }

            // Keywords
            else if (startTok == Token.Type.ID && (
                tok.get(0).value.equals("print") || tok.get(0).value.equals("println")
            )) {
                // print, println
                if (tok.get(0).value.equals("print") || tok.get(0).value.equals("println")) {
                    out += "System.out." + tok.get(0).value + "(";

                    for (int j = 1; j < tok.size(); ++j)
                        out += tok.get(j).value + " ";

                    out += ");";
                }
            }

            else {
                for (int j = 0; j < tok.size(); ++j)
                    out += tok.get(j).value + " ";
            }

            out += "\n";
            outClasses.put(currentClass, out);
            lastIndent = indent;

            System.out.println(Util.d(tok));
        }

        // Construct classes
        String out = "";

        // File class goes first
        if (outClasses.containsKey(className))
            out += constructClassString(className, outClasses.get(className), outClassAccess.get(className));

        // Construct other classes
        for (String c : outClasses.keySet())
            if (!c.equals(className))
                out += constructClassString(c, outClasses.get(c), outClassAccess.get(c));

        return startTemplate + "\n" + out + endTemplate;
    }

    public static String constructClassString(String className, String code, AccessMod accessMod) {
        String accessModStr = MethodAccess.accessModToString(accessMod);
        String separator = (accessModStr.length() > 0 ? " " : "");

        String out = accessModStr + separator + "class " + className + " {\n";
        out += code;
        out += "\n}\n";

        return out;
    }

    public static int findToken(ArrayList<Token> tok, Token.Type type) {
        for (int i = 0; i < tok.size(); ++i)
            if (tok.get(i).type == type)
                return i;
        return -1;
    }

    public static ArrayList<Token.Type> getTokenTypes(ArrayList<Token> tok) {
        ArrayList<Token.Type> types = new ArrayList<>();

        for (int i = 0; i < tok.size(); ++i)
            types.add(tok.get(i).type);

        return types;
    }

    public static MethodAccess getMethodAccess(ArrayList<Token> tok) {
        AccessMod accessMod = AccessMod.DEFAULT;
        boolean isStatic = false;

        // Fetch access modifier and static from line
        for (int j = 0; j < tok.size(); ++j) {
            Token.Type t = tok.get(j).type;
            String v = tok.get(j).value;

            // Attempt to get access modifier from every token
            accessMod = MethodAccess.accessModFromToken(tok.get(j));
            if (accessMod != AccessMod.DEFAULT)
                break;

            if (v.equals("+")) {
                accessMod = AccessMod.PUBLIC;
                break;
            } else if (v.equals("-")) {
                accessMod = AccessMod.PRIVATE;
                break;
            } else if (v.equals("*")) {
                accessMod = AccessMod.PROTECTED;
                break;
            } else if (v.equals("static") || v.equals("#"))
                isStatic = true;
        }

        return new MethodAccess(accessMod, isStatic);
    }
}
