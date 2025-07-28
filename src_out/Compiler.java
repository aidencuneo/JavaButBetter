import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static HashMap < String , String > outClasses = new HashMap < > ();
    public static HashMap < String , AccessMod > outClassAccess = new HashMap < > ();
    public static HashMap < String , Alias > aliases = new HashMap < > ();
    public static int nextTempVar = 0;
    public static int indent = 0;
    public static boolean defaultStatic = false;
    public static CompResult compileFile(String className , String code) {
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";
        outClasses = new HashMap < > ();
        outClassAccess = new HashMap < > ();
        outClassAccess.put(mainClassName , AccessMod.PUBLIC);
        aliases = new HashMap < > ();
        nextTempVar = 0;
        defaultStatic = false;
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
        var types = getTokenTypes(tok);
        var startTok = types.get(0);
        var endTok = Util.get(types , - 1);
        var f = - 1;
        var f2 = - 1;
        if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IMPORT))) {
            var importStr = "import ";
            for (int j = 1; j < tok.size(); ++j) {
                importStr += tok.get(j).value;
            }
            startTemplate += importStr + ";\n";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.OPTION))) {
            if (LangUtil.isTruthy(tok.size() < 2)) {
                return "";
            }
            if (LangUtil.isTruthy(tok.size() < 3)) {
                return "";
            }
            var optionName = tok.get(1).value;
            var optionValue = tok.get(2).value;
            Options.setOption(optionName , optionValue);
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ALIAS))) {
            if (LangUtil.isTruthy(tok.size() < 2)) {
                return "";
            }
            if (LangUtil.isTruthy(Extensions.operEq(((f = findTokenType(tok , Token.Type.ASSIGN))), - 1))) {
                return "";
            }
            if (LangUtil.isTruthy(f < 2)) {
                return "";
            }
            var name = tok.get(1).value;
            var args = new ArrayList < String > ();
            if (LangUtil.isTruthy(f > 2)) {
                args = Tokeniser.extractArgsFromExpr(tok.get(2).value);
            }
            var tokens = Util.select(tok , f + 1);
            aliases.put(name , new Alias(name , tokens , args));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok , Token.Type.CLASS))), - 1))) ? (f + 1 < tok.size()) : (!Extensions.operEq(((f = findTokenType(tok , Token.Type.CLASS))), - 1)))) {
            outClasses.put(currentClass , out);
            currentClass = tok.get(f + 1).value;
            out = outClasses.getOrDefault(currentClass , "");
            defaultStatic = false;
            var access = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            outClassAccess.put(currentClass , access.accessMod);
            defaultStatic = access.isStatic;
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IF))) ? (Extensions.operEq(startTok, Token.Type.IF)) : ((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELIF))) ? (Extensions.operEq(startTok, Token.Type.ELIF)) : ((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELSE))) ? (Extensions.operEq(startTok, Token.Type.ELSE)) : ((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.WHILE))) ? (Extensions.operEq(startTok, Token.Type.WHILE)) : (Extensions.operEq(startTok, Token.Type.UNTIL)))))))) {
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IF))) { out += "if ("; }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELIF))) { out += "else if ("; }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELSE))) { out += "else "; }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.WHILE))) { out += "while ("; }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.UNTIL))) { out += "while (!("; }
            if (LangUtil.isTruthy(!Extensions.operEq(startTok, Token.Type.ELSE))) {
                out += "LangUtil.isTruthy(";
                out += compileExpr(Util.select(tok , 1));
                out += "))";
                if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.UNTIL))) { out += ")"; }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.FOR))) ? (!Extensions.operEq(((f = findTokenType(tok , Token.Type.IN))), - 1)) : (Extensions.operEq(startTok, Token.Type.FOR)))) {
            out += "for (";
            String varname = tok.get(1).value;
            out += "var " + varname + " : LangUtil.asIterable(";
            out += compileExpr(Util.select(tok , f + 1));
            out += ")) ";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok , Token.Type.IF))), - 1))) ? (Extensions.operEq(findTokenTypeRev(tok , Token.Type.ELSE), - 1)) : (!Extensions.operEq(((f = findTokenTypeRev(tok , Token.Type.IF))), - 1)))) {
            out += "if (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok , Token.Type.WHILE))), - 1))) {
            out += "while (LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok , Token.Type.UNTIL))), - 1))) {
            out += "while (!LangUtil.isTruthy(";
            String cond = compileExpr(Util.select(tok , f + 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("false") : (cond);
            out += ")) { ";
            out = compileStatement(Util.select (tok , 0 , f), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok , Token.Type.FOR))), - 1))) {
            int f_in = findTokenTypeRev(tok , Token.Type.IN);
            if (LangUtil.isTruthy(!Extensions.operEq(f_in, - 1))) {
                out += "for (";
                String varname = tok.get(f + 1).value;
                out += "var " + varname + " : LangUtil.asIterable(";
                out += compileExpr(Util.select(tok , f_in + 1));
                out += ")) { ";
                out = compileStatement(Util.select (tok , 0 , f), out);
                out += " }";
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.TRY))) {
            out += "try ";
            String resources = compileExpr(Util.select (tok , 1), false);
            if (LangUtil.isTruthy(resources)) { out += "(" + resources + ") "; }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.CATCH))) {
            out += "catch ";
            if (LangUtil.isTruthy(tok.size() > 2)) {
                return "";
            }
            else if (LangUtil.isTruthy(tok.size() > 1)) {
                Token t = tok.get(1);
                if (LangUtil.isTruthy(!Extensions.operEq(t.type, Token.Type.EXPR))) {
                    return "";
                }
                String args = compileMethodArgs(t.value);
                if (LangUtil.isTruthy(args)) { out += args + " "; }
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(tok.size() < 2)) {
                return "";
            }
            String code = tok.get(1).value;
            code = code.substring(1 , code.length() - 1);
            out += code;
        }
        else if (LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.SCOPE))) {
            if (LangUtil.isTruthy(false)) {
                
            }
            else {
                var methodAccess = getMethodAccess(tok);
                tok = stripMethodAccess(tok);
                if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                    methodAccess . accessMod = AccessMod.PUBLIC;
                }
                var typeArgs = "";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(tok.get(0).type, Token.Type.EXPR)) : (tok))) {
                    var v = tok.get(0).value;
                    typeArgs = "<" + v.substring(1 , v.length() - 1) + "> ";
                    tok.remove(0);
                }
                tok.remove(tok.size() - 1);
                var args = "()";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.EXPR)) : (tok))) {
                    args = compileMethodArgs(tok.get(tok.size() - 1).value);
                    tok.remove(tok.size() - 1);
                }
                var methodName = "";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.ID)) : (tok))) {
                    methodName = tok.get(tok.size() - 1).value;
                    tok.remove(tok.size() - 1);
                }
                else {
                    
                }
                String returnType = "";
                for (var t : LangUtil.asIterable(tok)) { returnType += t.value + " "; }
                if (LangUtil.isTruthy(!LangUtil.isTruthy(returnType))) { returnType = "void "; }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, "main"))) {
                    args = "(String[] args)";
                }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, currentClass))) {
                    out += methodAccess + " " + typeArgs + methodName + args;
                }
                else {
                    out += methodAccess + " " + typeArgs + returnType + methodName + args;
                }
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.RETURN))) {
            out += "return ";
            if (LangUtil.isTruthy(tok.size() > 1)) {
                out += compileExpr(Util.select(tok , 1));
            }
            else {
                out += "null";
            }
            out += ";";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.BREAK))) {
            out += "break;";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.CONTINUE))) {
            out += "continue;";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.PASS))) {
            
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ID))) ? (((LangUtil.isTruthy(Extensions.operEq(tok.get(0).value, "print"))) ? (Extensions.operEq(tok.get(0).value, "print")) : (Extensions.operEq(tok.get(0).value, "println")))) : (Extensions.operEq(startTok, Token.Type.ID))))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(tok.get(0).value, "print"))) ? (Extensions.operEq(tok.get(0).value, "print")) : (Extensions.operEq(tok.get(0).value, "println")))) {
                out += "LangUtil." + tok.get(0).value + "(";
                out += compileExpr(Util.select(tok , 1));
                out += ");";
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(tok.size() >= 2)) ? (!LangUtil.isTruthy(indent)) : (tok.size() >= 2))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            var value = "";
            if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok , Token.Type.ASSIGN))), - 1))) {
                value = " = " + compileExpr(Util.select(tok , f + 1));
                tok = Util.select(tok , 0 , f);
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.ID)) : (tok))) {
                name = tok.get(tok.size() - 1).value;
                tok.remove(tok.size() - 1);
            }
            else {
                
            }
            var vartype = "";
            for (var t : LangUtil.asIterable(tok)) { vartype += t.value + " "; }
            if (LangUtil.isTruthy(!LangUtil.isTruthy(vartype))) {
                
            }
            out += methodAccess + " " + vartype + name + value + ";";
        }
        else {
            var ending = LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.COMMA)) ? ("") : (";");
            out += compileExpr(tok , false) + ending;
        }
        return out;
    }
    public static String compileExpr(ArrayList < Token > tok , boolean nested) {
        String out = "";
        int f = - 1;
        int f2 = - 1;
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return out; }
        if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok , Token.Type.ASSIGN))), - 1))) {
            String vartype = "";
            String varname = "";
            if (LangUtil.isTruthy(Extensions.operEq(f, 2))) {
                vartype = tok.get(0).value;
                varname = tok.get(1).value;
            }
            else if (LangUtil.isTruthy(Extensions.operEq(f, 1))) {
                varname = tok.get(0).value;
            }
            else {
                for (int j = 0; j < f; ++j) {
                    varname += tok.get(j).value + " ";
                }
                varname = varname.trim();
            }
            if (LangUtil.isTruthy(Extensions.operEq(vartype, "let"))) { vartype = "var"; }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            if (LangUtil.isTruthy(vartype)) {
                out += vartype + " " + varname + " = ";
            }
            else {
                out += varname + " = ";
            }
            out += compileExpr(Util.select(tok , f + 1));
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok , Token.Type.COMP_ASSIGN))), - 1))) {
            String varname = "";
            String oper = tok.get(f).value.substring(0 , tok.get(f).value.length() - 1);
            if (LangUtil.isTruthy(Extensions.operEq(f, 1))) {
                varname = tok.get(0).value;
            }
            else {
                
            }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            ArrayList < Token > exprTok = Util.select(tok , f + 1);
            if (LangUtil.isTruthy(List.of("." , "**" , "??").contains(oper))) {
                exprTok.add(0 , Token.fromString(varname));
                exprTok.add(1 , Token.fromString(oper));
                out += varname + " = ";
            }
            else {
                out += varname + " " + tok.get(f).value + " ";
            }
            out += compileExpr(exprTok);
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "?"))), - 1))) ? (!Extensions.operEq(((f2 = findToken(tok , ":"))), - 1)) : (!Extensions.operEq(((f = findToken(tok , "?"))), - 1)))) {
            String cond = compileExpr(Util.select(tok , 0 , f));
            String lhs = compileExpr(Util.select(tok , f + 1 , f2));
            String rhs = compileExpr(Util.select(tok , f2 + 1));
            out += "LangUtil.isTruthy(" + cond + ") ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "if"))), - 1))) ? (!Extensions.operEq(((f2 = findToken(tok , "else"))), - 1)) : (!Extensions.operEq(((f = findToken(tok , "if"))), - 1)))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String cond = compileExpr(Util.select(tok , f + 1 , f2));
            String rhs = compileExpr(Util.select(tok , f2 + 1));
            out += "LangUtil.isTruthy(" + cond + ") ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "??"))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "((" + lhs + ") != null) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("||" , "or")))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("&&" , "and")))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + rhs + ") : (" + lhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "|"))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " | " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "^"))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " ^ " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "&"))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " & " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("==" , "!=" , "===" , "!==")))), - 1))) {
            String oper = tok.get(f).value;
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "=="))) {
                out += "Extensions.operEq(" + lhs + ", " + rhs + ")";
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!="))) {
                out += "!Extensions.operEq(" + lhs + ", " + rhs + ")";
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "==="))) {
                out += lhs + " == " + rhs;
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!=="))) {
                out += lhs + " != " + rhs;
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("<" , ">" , "<=" , ">=" , "is")))), - 1))) {
            String oper = tok.get(f).value;
            if (LangUtil.isTruthy(Extensions.operEq(oper, "is"))) {
                oper = "==";
            }
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + oper + " " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("+" , "-")))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok , List.of("*" , "/" , "%")))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += lhs + " " + tok.get(f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(tok.size() > 1)) ? (Extensions.operEq(tok.get(0).type, Token.Type.UNARY_OPER)) : (tok.size() > 1))) {
            String oper = tok.get(0).value;
            String expr = compileExpr(Util.select(tok , 1));
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "not"))) ? (Extensions.operEq(oper, "not")) : (Extensions.operEq(oper, "!")))) {
                out += "!LangUtil.isTruthy(" + expr + ")";
            }
            else {
                out += tok.get(0).value + expr;
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok , "**"))), - 1))) {
            String lhs = compileExpr(Util.select(tok , 0 , f));
            String rhs = compileExpr(Util.select(tok , f + 1));
            out += "Math.pow(" + lhs + ", " + rhs + ")";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(tok.size() > 1)) ? (Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.INCREMENT)) : (tok.size() > 1))) {
            String name = compileExpr(Util.select(tok , 0 , tok.size() - 1));
            out += name + "++";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(tok.size() > 1)) ? (Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.DECREMENT)) : (tok.size() > 1))) {
            String name = compileExpr(Util.select(tok , 0 , tok.size() - 1));
            out += name + "--";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(tok.get(tok.size() - 1).type, Token.Type.EXPR))) {
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
        else if (LangUtil.isTruthy(Extensions.operEq(tok.get(0).type, Token.Type.EXPR))) {
            String expr = tok.get(0).value;
            expr = expr.substring(1 , expr.length() - 1);
            out += "(" + compileExpr(Tokeniser.tokLine(expr)) + ")";
        }
        else {
            for (var j : LangUtil.asIterable(tok.size())) {
                var t = tok.get(j);
                if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.EXPR))) {
                    String raw = t.value.substring(1 , t.value.length() - 1);
                    ArrayList < Token > tokens = Tokeniser.tokLine(raw);
                    out += "(" + compileExpr(tokens) + ")";
                }
                else {
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
        if (LangUtil.isTruthy((LangUtil.isTruthy(expr.startsWith("("))) ? (expr.endsWith(")")) : (expr.startsWith("(")))) {
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
        for (var i : LangUtil.asIterable(tok.size())) {
            if (LangUtil.isTruthy(Extensions.operEq(tok.get(i).value, value))) { return i; }
        }
        return - 1;
    }
    public static int findTokenRev(ArrayList < Token > tok , String value) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(tok.get(i).value, value))) { return i; }
        }
        return - 1;
    }
    public static int findAnyToken(ArrayList < Token > tok , List < String > values) {
        for (var i : LangUtil.asIterable(tok.size())) {
            if (LangUtil.isTruthy(values.contains(tok.get(i).value))) { return i; }
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
        for (var i : LangUtil.asIterable(tok.size())) {
            if (LangUtil.isTruthy(Extensions.operEq(tok.get(i).type, type))) { return i; }
        }
        return - 1;
    }
    public static int findTokenTypeRev(ArrayList < Token > tok , Token . Type type) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(tok.get(i).type, type))) { return i; }
        }
        return - 1;
    }
    public static ArrayList < Token . Type > getTokenTypes(ArrayList < Token > tok) {
        var types = new ArrayList < Token.Type > ();
        for (var t : LangUtil.asIterable(tok)) { types.add(t.type); }
        return types;
    }
    public static MethodAccess getMethodAccess(ArrayList < Token > tok , int end) {
        var accessMod = AccessMod.NONE;
        var isStatic = defaultStatic;
        for (var j : LangUtil.asIterable(end)) {
            var t = tok.get(j).type;
            var v = tok.get(j).value;
            if (LangUtil.isTruthy(Extensions.operEq(accessMod, AccessMod.NONE))) {
                accessMod = MethodAccess.accessModFromToken(tok.get(j));
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "static"))) ? (Extensions.operEq(v, "static")) : (Extensions.operEq(v, "#")))) { isStatic = true; }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "instance"))) ? (Extensions.operEq(v, "instance")) : (Extensions.operEq(v, "^")))) { isStatic = false; }
        }
        return new MethodAccess(accessMod , isStatic);
    }
    public static MethodAccess getMethodAccess(ArrayList < Token > tok) {
        return getMethodAccess(tok , tok.size());
    }
    public static ArrayList < Token > stripMethodAccess(ArrayList < Token > tok , int end) {
        for (var i : LangUtil.asIterable(end)) {
            var t = tok.get(i);
            var v = t.value;
            if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "static"))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "#"))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "instance"))) ? (!Extensions.operEq(v, "^")) : (!Extensions.operEq(v, "instance"))) : (!Extensions.operEq(v, "#"))) : (!Extensions.operEq(v, "static"))) : (Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))))) {
                return new ArrayList < Token > (tok.subList(i , tok.size()));
            }
        }
        return tok;
    }
    public static ArrayList < Token > stripMethodAccess(ArrayList < Token > tok) {
        return stripMethodAccess(tok , tok.size());
    }
}

