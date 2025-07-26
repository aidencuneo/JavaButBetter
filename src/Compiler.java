import java.io.*;
import java.util.*;

public class Compiler {
    static String mainClassName = "";
    static String currentClass = "";
    static String startTemplate = "";
    static String endTemplate = "";
    static HashMap<String, String> outClasses = new HashMap<>();
    static HashMap<String, AccessMod> outClassAccess = new HashMap<>();

    public static CompResult compileFile(String className, String code) {
        // Allow all methods to use these variables
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";

        // Reset class data
        outClasses = new HashMap<>();
        outClassAccess = new HashMap<>();
        outClassAccess.put(mainClassName, AccessMod.PUBLIC);
        
        ArrayList<String> lines = Tokeniser.splitFile(code);
        int lastIndent = 0;

        for (int i = 0; i < lines.size(); ++i) {
            // System.out.println(i + "/" + (lines.size() - 1) + ": " + lines.get(i));
            String out = outClasses.getOrDefault(currentClass, "");

            ArrayList<Token> tok = Tokeniser.tokLine(lines.get(i));

            // Nothing in the line?
            if (tok.isEmpty())
                continue;

            int indent = tok.get(0).value.length();
            tok.remove(0);

            // Nothing but indentation in the line?
            if (tok.isEmpty())
                continue;

            // Indentation controls braces
            while (indent > lastIndent) {
                lastIndent += 4;
                out = out.trim();
                out += " {\n";
            }

            while (indent < lastIndent) {
                out += " ".repeat(lastIndent) + "}\n";
                lastIndent -= 4;
            }

            // Add indentation for every line
            out += " ".repeat(indent + 4);

            // Add compiled statement
            out = compileStatement(tok, out);
            out += "\n";

            // Push to current class
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

        // Return compilation result
        return new CompResult(outClasses, outClassAccess, startTemplate, endTemplate);
    }

    public static String compileStatement(ArrayList<Token> tok, String out) {
        if (tok.isEmpty())
            return "";

        ArrayList<Token.Type> types = getTokenTypes(tok);
        Token.Type startTok = types.get(0);
        Token.Type endTok = Util.get(types, -1);
        int f = -1;

        // Imports
        if (startTok == Token.Type.IMPORT) {
            String importStr = "import ";
            for (int j = 1; j < tok.size(); ++j)
                importStr += tok.get(j).value;
            startTemplate += importStr + ";\n";
        }

        // Options
        else if (startTok == Token.Type.OPTION) {
            if (tok.size() < 2) {
                // Error!
                return "";
            }

            if (tok.size() < 3) {
                // Error!
                return "";
            }

            String optionName = tok.get(1).value;
            String optionValue = tok.get(2).value;
            Options.setOption(optionName, optionValue);
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
            switch (startTok) {
                case IF -> out += "if (";
                case ELIF -> out += "else if (";
                case ELSE -> out += "else (";
                case WHILE -> out += "while (";
                case UNTIL -> out += "while (!(";
                default -> {}
            }

            out += "LangUtil.isTruthy(";
            out += compileExpr(Util.select(tok, 1));
            out += "))";

            if (startTok == Token.Type.UNTIL)
                out += ")";
        }

        // For-in loops
        else if (startTok == Token.Type.FOR && (f = findTokenType(tok, Token.Type.IN)) != -1) {
            out += "for (";
            String varname = tok.get(1).value;
            out += "var " + varname + " : LangUtil.asIterable(";
            out += compileExpr(Util.select(tok, f + 1));
            out += ")) ";
        }

        // Statement control flow
        else if ((f = findTokenTypeRev(tok, Token.Type.IF)) != -1) {
            out += "if (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok, f + 1));
            out += cond.isEmpty() ? "true" : cond;
            out += ")) { ";
            out = compileStatement(Util.select(tok, 0, f), out);
            out += " }";
        }

        else if ((f = findTokenTypeRev(tok, Token.Type.WHILE)) != -1) {
            out += "while (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok, f + 1));
            out += cond.isEmpty() ? "true" : cond;
            out += ")) { ";
            out = compileStatement(Util.select(tok, 0, f), out);
            out += " }";
        }

        else if ((f = findTokenTypeRev(tok, Token.Type.UNTIL)) != -1) {
            out += "while (!LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok, f + 1));
            out += cond.isEmpty() ? "false" : cond;
            out += ")) { ";
            out = compileStatement(Util.select(tok, 0, f), out);
            out += " }";
        }

        else if ((f = findTokenTypeRev(tok, Token.Type.FOR)) != -1) {
            // For-in
            int f_in = findTokenTypeRev(tok, Token.Type.IN);
            if (f_in != -1) {
                out += "for (";
                String varname = tok.get(f + 1).value;
                out += "var " + varname + " : LangUtil.asIterable(";
                out += compileExpr(Util.select(tok, f_in + 1));
                out += ")) { ";
                out = compileStatement(Util.select(tok, 0, f), out);
                out += " }";
            }
        }

        // Try
        else if (startTok == Token.Type.TRY) {
            out += "try ";

            String resources = compileExpr(Util.select(tok, 1), false);
            if (!resources.isEmpty())
                out += "(" + resources + ") ";
        }

        // Catch
        else if (startTok == Token.Type.CATCH) {
            out += "catch ";

            if (tok.size() > 2) {
                // Error!
                return "";
            } else if (tok.size() > 1) {
                Token t = tok.get(1);

                if (t.type != Token.Type.EXPR) {
                    // Error!
                    return "";
                }

                String args = compileMethodArgs(t.value);
                if (!args.isEmpty())
                    out += args + " ";
            }
        }

        // Inline code
        else if (startTok == Token.Type.INLINE) {
            if (tok.size() < 2) {
                // Error!
                return "";
            }

            String code = tok.get(1).value;
            code = code.substring(1, code.length() - 1); // Strip backticks away
            out += code;
        }

        // Scoped statements - methods only (?)
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

                // Get type arguments
                String typeArgs = "";

                if (end >= 0 && tok.get(end).type == Token.Type.EXPR) {
                    String v = tok.get(end).value;
                    typeArgs = "<" + v.substring(1, v.length() - 1) + "> ";
                    --end;
                }

                // Get return type
                String returnType = "";

                for (int j = 0; j <= end; ++j) {
                    Token t = tok.get(j);

                    if (t.type == Token.Type.ID || t.type == Token.Type.SYMBOL)
                        returnType += t.value + " ";
                }

                returnType = returnType.trim();

                if (returnType.isEmpty())
                    returnType = "void";

                // Get method access
                MethodAccess methodAccess = getMethodAccess(tok, end + 1);

                // Default access modifier for methods is public
                if (methodAccess.accessMod == AccessMod.DEFAULT)
                    methodAccess.accessMod = AccessMod.PUBLIC;

                // If method name is 'main', args must be (String[] args)
                if (methodName.equals("main"))
                    args = "(String[] args)";

                // If method name is the same as the class name,
                // it's a constructor, so don't include the return type
                if (methodName.equals(currentClass))
                    out += methodAccess + " " + methodName + args;
                else
                    out += methodAccess + " " + typeArgs + returnType + " " + methodName + args;
            }
        }

        // Keywords
        else if (startTok == Token.Type.RETURN) {
            out += "return ";
            out += compileExpr(Util.select(tok, 1));
            out += ";";
        }

        else if (startTok == Token.Type.BREAK)
            out += "break;";

        else if (startTok == Token.Type.CONTINUE)
            out += "continue;";

        else if (startTok == Token.Type.PASS) {
            // Do nothing
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
            out += compileExpr(tok, false) + (endTok == Token.Type.COMMA ? "" : ";");

        return out;
    }

    public static String compileExpr(ArrayList<Token> tok, boolean nested) {
        String out = "";
        int f = -1;

        // Empty expression
        if (tok.isEmpty())
            return out;

        // Assignments
        if ((f = findTokenType(tok, Token.Type.ASSIGN)) != -1) {
            String vartype = "";
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

            if (vartype.equals("let"))
                vartype = "var";

            if (nested)
                out += "(";

            if (vartype.isEmpty())
                out += varname + " = ";
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
            // System.out.println("LHS: " + lhs + ", RHS: " + rhs);
            out += "((" + lhs + ") != null) ? (" + lhs + ") : (" + rhs + ")";
        }

        // Binary operators (Java handles precedence)
        else if ((f = findAnyToken(tok, List.of("+", "-", "*", "/", "%"))) != -1) {
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

        // Dot operator
        else if ((f = findTokenRev(tok, ".")) != -1) {
            String lhs = compileExpr(Util.select(tok, 0, f));
            String rhs = compileExpr(Util.select(tok, f + 1));
            out += lhs + "." + rhs;
        }

        // Expressions (parentheses)
        else if (tok.get(0).type == Token.Type.EXPR) {
            String expr = tok.get(0).value;
            expr = expr.substring(1, expr.length() - 1);
            out += "(" + compileExpr(Tokeniser.tokLine(expr)) + ")";
        }

        // Fallback - add tokens as they are
        else {
            for (int j = 0; j < tok.size(); ++j) {
                Token t = tok.get(j);

                // Perform conversions for some tokens

                // Compile expressions
                if (t.type == Token.Type.EXPR) {
                    String raw = t.value.substring(1, t.value.length() - 1);
                    ArrayList<Token> tokens = Tokeniser.tokLine(raw);
                    out += "(" + compileExpr(tokens) + ")";
                }

                else
                    out += tok.get(j).value + " ";
            }
        }

        // System.out.println("Just did expr: " + Util.d(tok));

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

    public static int findToken(ArrayList<Token> tok, String value) {
        for (int i = 0; i < tok.size(); ++i)
            if (tok.get(i).value.equals(value))
                return i;
        return -1;
    }

    public static int findTokenRev(ArrayList<Token> tok, String value) {
        for (int i = tok.size() - 1; i >= 0; --i)
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

    public static int findAnyTokenRev(ArrayList<Token> tok, List<String> values) {
        for (int i = tok.size() - 1; i >= 0; --i)
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

    public static int findTokenTypeRev(ArrayList<Token> tok, Token.Type type) {
        for (int i = tok.size() - 1; i >= 0; --i)
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

            switch (v) {
                case "+" -> accessMod = AccessMod.PUBLIC;
                case "-" -> accessMod = AccessMod.PRIVATE;
                case "*" -> accessMod = AccessMod.PROTECTED;
                case "static", "#" -> isStatic = true;
                default -> {}
            }
        }

        return new MethodAccess(accessMod, isStatic);
    }
}
