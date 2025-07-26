import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static HashMap<String, String> outClasses = new HashMap<>();
    public static HashMap<String, AccessMod> outClassAccess = new HashMap<>();
    public static int nextTempVar = 0;
    public static int indent = 0;
    public static CompResult compileFile(String className , String code) {
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";
        outClasses = new HashMap < > ();
        outClassAccess = new HashMap < > ();
        outClassAccess.put(mainClassName , AccessMod.PUBLIC);
        nextTempVar = 0;
        ArrayList < String > lines = Tokeniser.splitFile(code);
        int lastIndent = 0;
        for (var i : LangUtil.asIterable(lines.size())) {
            String out = outClasses.getOrDefault(currentClass , "");
            ArrayList < Token > tok = Tokeniser.tokLine(lines.get(i));
            if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { continue; }
            indent = tok.get(0).value.length();
            tok.remove(0);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { continue; }
            while (LangUtil.isTruthy(indent > lastIndent)) {
                lastIndent += 4;
                out = out.trim();
                out += " {\n";
            }
            while (LangUtil.isTruthy(indent < lastIndent)) {
                out += " ".repeat(lastIndent) + "}\n";
                lastIndent -= 4;
            }
            out += " ".repeat(indent + 4);
            out = compileStatement(tok , out);
            out += "\n";
            outClasses.put(currentClass , out);
            lastIndent = indent;
            System.out.println(Util.d(tok));
        }
        while (LangUtil.isTruthy(lastIndent > 0)) {
            String curOut = outClasses.getOrDefault(currentClass , "");
            outClasses.put(currentClass , curOut + " ".repeat(lastIndent) + "}\n");
            lastIndent -= 4;
        }
        return new CompResult(outClasses , outClassAccess , startTemplate , endTemplate);
    }
    public static String compileStatement(ArrayList < Token > tok , String out) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return ""; }
        ArrayList < Token . Type > types = getTokenTypes(tok);
        Token . Type startTok = types.get(0);
        Token . Type endTok = Util.get(types , - 1);
        int f = - 1;
        if (LangUtil.isTruthy((startTok == Token.Type.IMPORT))) {
            String importStr = "import ";
            for (int j = 1; j < tok.size(); ++j) {
                importStr += tok.get(j).value;
            }
            startTemplate += importStr + ";\n";
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.OPTION))) {
            if (LangUtil.isTruthy(tok.size() < 2)) {
                return "";
            }
            if (LangUtil.isTruthy(tok.size() < 3)) {
                return "";
            }
            String optionName = tok.get(1).value;
            String optionValue = tok.get(2).value;
            Options.setOption(optionName , optionValue);
        }
        else if (LangUtil.isTruthy(((f = findTokenType(tok, Token.Type.CLASS)) != - 1 & & f + 1 < tok.size()))) {
            outClasses.put(currentClass , out);
            currentClass = tok.get(f + 1).value;
            out = outClasses.getOrDefault(currentClass , "");
            AccessMod accessMod = AccessMod.DEFAULT;
            for (int j = 0; j < f && accessMod == AccessMod.DEFAULT; ++j) {
                accessMod = MethodAccess.accessModFromToken(tok.get(j));
            }
            outClassAccess.put(currentClass , accessMod);
        }
        else if (LangUtil.isTruthy((( == ( == ( == ( == ( == Token.Type.UNTIL)))))))) {
            switch (startTok) {
                += "if (";
                += "else if (";
                += "else (";
                += "while (";
                += "while (!(";
                default -> { };
            }
            out += "LangUtil.isTruthy(";
            out += compileExpr(Util.select(tok , 1));
            out += "))";
            if (LangUtil.isTruthy((startTok == Token.Type.UNTIL))) { out += ")"; }
        }
        else if (LangUtil.isTruthy((startTok == ( != - 1)))) {
            out += "for (";
            String varname = tok.get(1).value;
            out += "var " + varname + " : LangUtil.asIterable(";
            out += compileExpr(Util.select(tok , f + 1));
            out += ")) ";
        }
        else if (LangUtil.isTruthy(((f = findTokenTypeRev(tok, Token.Type.IF)) != - 1))) {
            out += "if (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += cond.isEmpty ()? "true" : cond;
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(((f = findTokenTypeRev(tok, Token.Type.WHILE)) != - 1))) {
            out += "while (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += cond.isEmpty ()? "true" : cond;
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(((f = findTokenTypeRev(tok, Token.Type.UNTIL)) != - 1))) {
            out += "while (!LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += cond.isEmpty ()? "false" : cond;
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(((f = findTokenTypeRev(tok, Token.Type.FOR)) != - 1))) {
            int f_in = findTokenTypeRev(tok , Token.Type.IN);
            if (LangUtil.isTruthy((f_in != - 1))) {
                out += "for (";
                String varname = tok.get(f + 1).value;
                out += "var " + varname + " : LangUtil.asIterable(";
                out += compileExpr(Util.select(tok , f_in + 1));
                out += ")) { ";
                out = compileStatement(Util.select (tok , 0 , f), out);
                out += " }";
            }
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.TRY))) {
            out += "try ";
            String resources = compileExpr(Util.select (tok , 1), false);
            if (LangUtil.isTruthy(resources)) { out += "(" + resources + ") "; }
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.CATCH))) {
            out += "catch ";
            if (LangUtil.isTruthy(tok.size() > 2)) {
                return "";
            }
            else if (LangUtil.isTruthy(tok.size() > 1)) {
                Token t = tok.get(1);
                if (LangUtil.isTruthy(( != Token.Type.EXPR))) {
                    return "";
                }
                String args = compileMethodArgs(t.value);
                if (LangUtil.isTruthy(args)) { out += args + " "; }
            }
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.INLINE))) {
            if (LangUtil.isTruthy(tok.size() < 2)) {
                return "";
            }
            String code = tok.get(1).value;
            code = code.substring(1 , code.length() - 1);
            out += code;
        }
        else if (LangUtil.isTruthy((endTok == Token.Type.SCOPE))) {
            if (LangUtil.isTruthy(false)) {
                
            }
            else (LangUtil.isTruthy()) {
                int end = tok.size() - 2;
                String args = "()";
                if (LangUtil.isTruthy((end >= ( == Token.Type.EXPR)))) {
                    args = compileMethodArgs(tok.get(end).value);
                    -- end;
                }
                String methodName = "";
                if (LangUtil.isTruthy((end >= ( == Token.Type.ID)))) {
                    methodName = tok.get(end).value;
                    -- end;
                }
                else (LangUtil.isTruthy()) {
                    
                }
                String typeArgs = "";
                if (LangUtil.isTruthy((end >= ( == Token.Type.EXPR)))) {
                    String v = tok.get(end).value;
                    typeArgs = "<" + v.substring(1 , v.length() - 1) + "> ";
                    -- end;
                }
                String returnType = "";
                for (int j = 0; j <= end; ++j) {
                    Token t = tok.get(j);
                    if (LangUtil.isTruthy((( == ( == Token.Type.SYMBOL | | t.value.equals("<") | | t.value.equals(">") | | t.value.equals(".")))))) {
                        returnType += t.value + " ";
                    }
                }
                returnType = returnType.trim();
                if (LangUtil.isTruthy(!LangUtil.isTruthy(returnType))) { returnType = "void"; }
                MethodAccess methodAccess = getMethodAccess(tok , end + 1);
                if (LangUtil.isTruthy(( == AccessMod.DEFAULT))) {
                    methodAccess . accessMod = AccessMod.PUBLIC;
                }
                if (LangUtil.isTruthy(methodName.equals("main"))) {
                    args = "(String[] args)";
                }
                if (LangUtil.isTruthy(methodName.equals(currentClass))) {
                    out += methodAccess + " " + methodName + args;
                }
                else (LangUtil.isTruthy()) {
                    out += methodAccess + " " + typeArgs + returnType + " " + methodName + args;
                }
            }
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.RETURN))) {
            out += "return ";
            if (LangUtil.isTruthy(tok.size() > 1)) {
                out += compileExpr(Util.select(tok , 1));
            }
            else (LangUtil.isTruthy()) {
                out += "null";
            }
            out += ";";
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.BREAK))) {
            out += "break;";
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.CONTINUE))) {
            out += "continue;";
        }
        else if (LangUtil.isTruthy((startTok == Token.Type.PASS))) {
            
        }
        else if (LangUtil.isTruthy((( == Token.Type.ID & & (tok.get(0).value.equals("print") | | tok.get(0).value.equals("println")))))) {
            if (LangUtil.isTruthy(tok.get(0).value.equals("print") | | tok.get(0).value.equals("println"))) {
                out += "System.out." + tok.get(0).value + "(";
                out += compileExpr(Util.select(tok , 1));
                out += ");";
            }
        }
        else if (LangUtil.isTruthy(( >= 2 & & !LangUtil.isTruthy(indent)))) {
            int end = tok.size() - 1;
            String value = "";
            if (LangUtil.isTruthy(((f = findTokenType(tok, Token.Type.ASSIGN)) != - 1))) {
                value = " = " + compileExpr(Util.select(tok , f + 1));
                end = f - 1;
            }
            String varname = tok.get(end).value;
            -- end;
            String vartype = tok.get(end).value;
            MethodAccess methodAccess = getMethodAccess(tok , end);
            if (LangUtil.isTruthy(( == AccessMod.DEFAULT))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            out += methodAccess + " " + vartype + " " + varname + value + ";";
        }
        else (LangUtil.isTruthy()) {
            String ending = (endTok == Token.Type.COMMA ? "" : ";");
            out += compileExpr(tok , false) + ending;
        }
        return out;
    }
    public static String compileExpr(ArrayList < Token > tok , boolean nested) {
        String out = "";
        int f = - 1;
        int f2 = - 1;
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return out; }
        if (LangUtil.isTruthy(((f = findTokenType(tok, Token.Type.ASSIGN)) != - 1))) {
            String vartype = "";
            String varname = "";
            if (LangUtil.isTruthy((f == 2))) {
                vartype = tok.get(0).value;
                varname = tok.get(1).value;
            }
            else if (LangUtil.isTruthy((f == 1))) {
                varname = tok.get(0).value;
            }
            else (LangUtil.isTruthy()) { {
                    varname += tok.get(j).value + " ";
                }
                varname = varname.trim();
            }
            if (LangUtil.isTruthy(vartype.equals("let"))) { vartype = "var"; }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            if (LangUtil.isTruthy(vartype)) {
                out += vartype + " " + varname + " = ";
            }
            else (LangUtil.isTruthy()) {
                out += varname + " = ";
            }
            out += compileExpr(Util.select(tok , f + 1));
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(((f = findTokenType(tok, Token.Type.COMP_ASSIGN)) != - 1))) {
            String varname = "";
            String oper = tok.get(f).value.substring(0 , tok.get(f).value.length() - 1);
            if (LangUtil.isTruthy((f == 1))) {
                varname = tok.get(0).value;
            }
            else (LangUtil.isTruthy()) {
                
            }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            ArrayList < Token > exprTok = Util.select(tok , f + 1);
            if (LangUtil.isTruthy(List.of("." , "**" , "??").contains(oper))) {
                exprTok.add(0 , Token.fromString(varname));
                exprTok.add(1 , Token.fromString(oper));
                out += varname + " = ";
            }
            else (LangUtil.isTruthy()) {
                out += varname + " " + tok.get(f).value + " ";
            }
            out += compileExpr(exprTok);
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(((f = findToken(tok, "??")) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "((" + lhs + ") != null) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("||", "or"))) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("&&", "and"))) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + rhs + ") : (" + lhs + ")";
        }
        else if (LangUtil.isTruthy(((f = findToken(tok, "|")) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " | " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findToken(tok, "^")) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " ^ " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findToken(tok, "&")) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " & " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("==", "!=", "===", "!=="))) != - 1))) {
            String oper = tok.get(f).value;
            if (LangUtil.isTruthy(oper.equals("==="))) {
                oper = "==";
            }
            else if (LangUtil.isTruthy(oper.equals("!=="))) {
                oper = "!=";
            }
            else if (LangUtil.isTruthy(oper.equals ("=="){ }))
            else if (LangUtil.isTruthy(oper.equals ("!="){ }))
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("<", ">", "<=", ">=", "is"))) != - 1))) {
            String oper = tok.get(f).value;
            if (LangUtil.isTruthy(oper.equals("is"))) {
                oper = "==";
            }
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + oper + " " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("+", "-"))) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy(((f = findAnyToken(tok, List.of("*", "/", "%"))) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy(( == Token.Type.UNARY_OPER))) {
            String oper = tok.get(0).value;
            String expr = compileExpr(Util.select(tok , 1));
            if (LangUtil.isTruthy(oper.equals("not") | | oper.equals("!"))) {
                out += "!LangUtil.isTruthy(" + expr + ")";
            }
            else (LangUtil.isTruthy()) {
                out += tok.get(0).value + expr;
            }
        }
        else if (LangUtil.isTruthy(((f = findToken(tok, "**")) != - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "Math.pow(" + lhs + ", " + rhs + ")";
        }
        else if (LangUtil.isTruthy(( == Token.Type.INCREMENT))) {
            String name = compileExpr(Util.select(tok , 0 , tok.size() - 1));
            out += name + "++";
        }
        else if (LangUtil.isTruthy(( == Token.Type.DECREMENT))) {
            String name = compileExpr(Util.select(tok , 0 , tok.size() - 1));
            out += name + "--";
        }
        else if (LangUtil.isTruthy(( == Token.Type.EXPR))) {
            String name = compileExpr(Util.select(tok , 0 , tok.size() - 1));
            Token t = tok.get(tok.size() - 1);
            String args = t.value.substring(1 , t.value.length() - 1);
            out += name + "(" + compileExpr(Tokeniser.tokLine(args)) + ")";
        }
        else if (LangUtil.isTruthy(((f = findTokenRev(tok , "."))) > 0)) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + "." + rhs;
        }
        else if (LangUtil.isTruthy(( == Token.Type.EXPR))) {
            String expr = tok.get(0).value;
            expr = expr.substring(1 , expr.length() - 1);
            out += "(" + compileExpr(Tokeniser.tokLine(expr)) + ")";
        }
        else (LangUtil.isTruthy()) {
            for (var t : LangUtil.asIterable(tok)) {
                if (LangUtil.isTruthy(( == Token.Type.EXPR))) {
                    String raw = t.value.substring(1 , t.value.length() - 1);
                    ArrayList < Token > tokens = Tokeniser.tokLine(raw);
                    out += "(" + compileExpr(tokens) + ")";
                }
                else (LangUtil.isTruthy()) {
                    out += tok.get(j).value + " ";
                }
            }
        }
        return out.trim();
    }
    public static String compileExpr(ArrayList < Token > tok) {
        return compileExpr(tok , true);
    }
    public static String compileMethodArgs(String expr) {
        if (LangUtil.isTruthy(expr.startsWith("(" ) & & expr.endsWith ( ")"))) {
            expr = expr.substring(1 , expr.length() - 1);
        }
        String out = "";
        ArrayList < Token > tok = Tokeniser.tokLine(expr);
        for (var t : LangUtil.asIterable(tok)) { out += t.value + " "; }
        return "(" + out.trim() + ")";
    }
    public static String getNextTemp() {
        return "_temp" + nextTempVar++;
    }
    public static int findToken(ArrayList < Token > tok , String value) {
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(t.value.equals(value))) { return i; }
        }
        return - 1;
    }
    public static int findTokenRev(ArrayList < Token > tok , String value) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(tok.get(i).value.equals(value))) { return i; }
        }
        return - 1;
    }
    public static int findAnyToken(ArrayList < Token > tok , List < String > values) {
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(values.contains(t.value))) { return i; }
        }
        return - 1;
    }
    public static int findAnyTokenRev(ArrayList < Token > tok , List < String > values) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(values.contains(tok.get(i).value))) { return i; }
        }
        return - 1;
    }
    public static int findTokenType(ArrayList < Token > tok , Token . Type type) {
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(( == type))) { return i; }
        }
        return - 1;
    }
    public static int findTokenTypeRev(ArrayList < Token > tok , Token . Type type) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(( == type))) { return i; }
        }
        return - 1;
    }
    public static ArrayList < Token . Type > getTokenTypes(ArrayList < Token > tok) {
        var types = new ArrayList < Token.Type > ();
        for (var t : LangUtil.asIterable(tok)) { types.add(t.type); }
        return types;
    }
    public static MethodAccess getMethodAccess(ArrayList < Token > tok , int end) {
        AccessMod accessMod = AccessMod.DEFAULT;
        boolean isStatic = false;
        for (var j : LangUtil.asIterable(end)) {
            Token . Type t = tok.get(j).type;
            String v = tok.get(j).value;
            System.out.println(j + ": " + v);
            if (LangUtil.isTruthy((accessMod == AccessMod.DEFAULT))) {
                accessMod = MethodAccess.accessModFromToken(tok.get(j));
            }
            switch (v) {
                case "+" -> accessMod = AccessMod.PUBLIC;
                case "-" -> accessMod = AccessMod.PRIVATE;
                case "*" -> accessMod = AccessMod.PROTECTED;
                case "static" , "#" -> isStatic = true;
                default -> {}
            }
        }
        return new MethodAccess(accessMod , isStatic);
    }
}

