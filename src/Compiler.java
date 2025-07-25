import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Compiler {
    public static String compileFile(String className, String code) {
        ArrayList<String> lines = Tokeniser.splitFile(code);

        String startTemplate = "import java.io.*;\nimport java.util.*;\n";
        String endTemplate = "";

        String currentClass = className;
        HashMap<String, String> outClasses = new HashMap<>();
        HashMap<String, AccessMod> outClassAccess = new HashMap<>();
        outClassAccess.put(className, AccessMod.PUBLIC);

        int lastIndent = 0;

        for (int i = 0; i < lines.size(); ++i) {
            String out = outClasses.getOrDefault(currentClass, "");

            ArrayList<Token> tok = Tokeniser.tokLine(lines.get(i));
            int indent = tok.get(0).value.length();
            tok.remove(0);

            ArrayList<Token.Type> types = getTokenTypes(tok);

            if (tok.isEmpty())
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
            else if ((f = findTokenType(tok, Token.Type.CLASS)) != -1 && f + 1 < tok.size()) {
                // Set code for previous class before moving to new one
                outClasses.put(currentClass, out);

                // Change current class
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

            // Control flow
            else if (startTok == Token.Type.IF
                || startTok == Token.Type.ELIF
                || startTok == Token.Type.ELSE
                || startTok == Token.Type.WHILE
                || startTok == Token.Type.UNTIL
            ) {
                if (startTok == Token.Type.IF)
                    out += "if (";
                else if (startTok == Token.Type.ELIF)
                    out += "else if (";
                else if (startTok == Token.Type.ELSE)
                    out += "else (";
                else if (startTok == Token.Type.WHILE)
                    out += "while (";
                else if (startTok == Token.Type.UNTIL)
                    out += "while (!(";

                out += compileExpr(Util.select(tok, 1)) + ")";

                if (startTok == Token.Type.UNTIL)
                    out += ")";
            }

            // For loops
            else if (startTok == Token.Type.FOR) {
            }

            // Scoped statements - loops, methods, classes, etc.
            else if (endTok == Token.Type.SCOPE) {
                if (false) {}

                // Methods
                else {
                    // access?, static?, returntype|void, name, expr?, scope
                    // This points to the next token to process from the end
                    int end = tok.size() - 2;

                    // Check for an expression before scope token
                    String args = "()";

                    if (end >= 0 && tok.get(end).type == Token.Type.EXPR) {
                        // Compile method arguments
                        args = compileMethodArgs(tok.get(end).value);

                        // Look to the next token for the name
                        --end;
                    }

                    // Get method name
                    String methodName = "";

                    if (end >= 0 && tok.get(end).type == Token.Type.ID) {
                        methodName = tok.get(end).value;
                        --end;
                    } else {
                        // Error! Method name not given
                    }

                    // Get return type
                    String returnType = "void";

                    if (end >= 0 && tok.get(end).type == Token.Type.ID) {
                        returnType = tok.get(end).value;
                        --end;
                    }

                    // Get method access
                    MethodAccess methodAccess = getMethodAccess(tok, end + 1);

                    // Default access modifier for methods is public
                    if (methodAccess.accessMod == AccessMod.DEFAULT)
                        methodAccess.accessMod = AccessMod.PUBLIC;

                    System.out.println(methodName + " -> " + currentClass);

                    // If method name is the same as the class name,
                    // it's a constructor, so don't include the return type
                    if (methodName.equals(currentClass))
                        out += methodAccess + " " + methodName + args;
                    else
                        out += methodAccess + " " + returnType + " " + methodName + args;
                }
                // tok.remove(tok.size() - 1);
            }

            // Keywords
            else if (startTok == Token.Type.RETURN) {
                out += "return ";
                out += compileExpr(Util.select(tok, 1));
                out += ";";
            }

            // ID keywords
            else if (startTok == Token.Type.ID && (
                tok.get(0).value.equals("print") || tok.get(0).value.equals("println")
            )) {
                // print, println
                if (tok.get(0).value.equals("print") || tok.get(0).value.equals("println")) {
                    out += "System.out." + tok.get(0).value + "(";
                    out += compileExpr(Util.select(tok, 1));
                    out += ");";
                }
            }

            // Expressions
            else
                out += compileExpr(tok, false) + ";";

            out += "\n";
            outClasses.put(currentClass, out);
            lastIndent = indent;

            System.out.println(Util.d(tok));
        }

        // After the final line of a class, reset indentation
        while (lastIndent > 0) {
            String curOut = outClasses.getOrDefault(currentClass, "");
            outClasses.put(currentClass, curOut + " ".repeat(lastIndent) + "}\n");
            lastIndent -= 4;
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

    public static String compileExpr(ArrayList<Token> tok, boolean nested) {
        String out = "";
        int f = -1;

        // Empty expression
        if (tok.isEmpty())
            return out;

        // Assignments
        if ((f = findTokenType(tok, Token.Type.ASSIGN)) != -1) {
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

            // Error!
            else {}

            if (nested)
                out += "(" + varname + " = ";
            else
                out += vartype + " " + varname + " = ";

            out += compileExpr(Util.select(tok, f + 1));

            if (nested)
                out += ")";
        }

        // Compound assignments
        else if ((f = findTokenType(tok, Token.Type.COMP_ASSIGN)) != -1) {
            String varname = "";
            String oper = tok.get(f).value.substring(0, tok.get(f).value.length() - 1);

            // name = value
            if (f == 1)
                varname = tok.get(0).value;

            // Error!
            else {}

            if (nested)
                out += "(";

            ArrayList<Token> exprTok = Util.select(tok, f + 1);

            // For custom operators, convert to 'name = (name oper value)'
            if (List.of(".", "**", "??").contains(oper)) {
                exprTok.add(0, Token.fromString(varname));
                exprTok.add(1, Token.fromString(oper));
                out += varname + " = ";
            }

            // Otherwise, use 'name oper= (value)'
            else {
                out += varname + " " + tok.get(f).value + " ";
            }

            out += compileExpr(exprTok);

            if (nested)
                out += ")";
        }

        // Null coalescing
        else if ((f = findToken(tok, "??")) != -1) {
            // name ?? '' => ((name) != null) ? (name) : ("");
            String lhs = compileExpr(Util.select(tok, 0, f));
            String rhs = compileExpr(Util.select(tok, f + 1));
            System.out.println("LHS: " + lhs + ", RHS: " + rhs);
            out += "((" + lhs + ") != null) ? (" + lhs + ") : (" + rhs + ")";
        }

        // Multiplicative operators
        else if ((f = findAnyToken(tok, List.of("*", "/", "%"))) != -1) {
            String lhs = compileExpr(Util.select(tok, 0, f));
            String rhs = compileExpr(Util.select(tok, f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }

        // Exponentiation
        else if ((f = findToken(tok, "**")) != -1) {
            String lhs = compileExpr(Util.select(tok, 0, f));
            String rhs = compileExpr(Util.select(tok, f + 1));
            out += "Math.pow(" + lhs + ", " + rhs + ")";
        }

        // Expressions (parentheses)
        else if (tok.get(0).type == Token.Type.EXPR) {
            String expr = tok.get(0).value;
            expr = expr.substring(1, expr.length() - 1);
            out += "(" + compileExpr(Tokeniser.tokLine(expr)) + ")";
        }

        // Fallback - add tokens as they are
        else {
            for (int j = 0; j < tok.size(); ++j)
                out += tok.get(j).value + " ";
        }

        return out.trim();
    }

    public static String compileExpr(ArrayList<Token> tok) {
        return compileExpr(tok, true);
    }

    public static String compileMethodArgs(String expr) {
        if (expr.startsWith("(") && expr.endsWith(")"))
            expr = expr.substring(1, expr.length() - 1);

        String out = "";
        ArrayList<Token> tok = Tokeniser.tokLine(expr);

        for (int j = 0; j < tok.size(); ++j)
            out += tok.get(j).value + " ";

        return "(" + out.trim() + ")";
    }

    public static String constructClassString(String className, String code, AccessMod accessMod) {
        String accessModStr = MethodAccess.accessModToString(accessMod);
        String separator = (accessModStr.length() > 0 ? " " : "");

        String out = accessModStr + separator + "class " + className + " {\n";
        out += "    " + code.trim();
        out += "\n}\n";

        return out;
    }

    public static int findToken(ArrayList<Token> tok, String value) {
        for (int i = 0; i < tok.size(); ++i)
            if (tok.get(i).value.equals(value))
                return i;
        return -1;
    }

    public static int findAnyToken(ArrayList<Token> tok, List<String> values) {
        for (int i = 0; i < tok.size(); ++i)
            if (values.contains(tok.get(i).value))
                return i;
        return -1;
    }

    public static int findTokenType(ArrayList<Token> tok, Token.Type type) {
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

    public static MethodAccess getMethodAccess(ArrayList<Token> tok, int end) {
        AccessMod accessMod = AccessMod.DEFAULT;
        boolean isStatic = false;

        // Fetch access modifier and static from line
        for (int j = 0; j < end; ++j) {
            Token.Type t = tok.get(j).type;
            String v = tok.get(j).value;

            // Attempt to get access modifier from every token (if not found already)
            if (accessMod == AccessMod.DEFAULT)
                accessMod = MethodAccess.accessModFromToken(tok.get(j));

            if (v.equals("+"))
                accessMod = AccessMod.PUBLIC;
            else if (v.equals("-"))
                accessMod = AccessMod.PRIVATE;
            else if (v.equals("*"))
                accessMod = AccessMod.PROTECTED;
            else if (v.equals("static") || v.equals("#"))
                isStatic = true;
        }

        return new MethodAccess(accessMod, isStatic);
    }
}
