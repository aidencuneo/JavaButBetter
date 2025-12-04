import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static String packagePath = "";
    public static HashMap < String , Class > classes = null;
    public static HashMap < String , Alias > aliases = null;
    public static HashMap < String , Integer > locals = null;
    public static int nextTempVar = 0;
    public static int indent = 0;
    public static int scope = 0;
    public static boolean defaultStatic = false;
    public static CompResult compileFile(String className , String code) {
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";
        classes = new HashMap < > ();
        aliases = new HashMap < > ();
        locals = new HashMap < > ();
        nextTempVar = 0;
        defaultStatic = false;
        var lines = Tokeniser.splitFile(code);
        var lastIndent = 0;
        var lastScope = 0;
        for (var i : LangUtil.asIterable(Extensions.len(lines))) {
            var cl = getOrCreateClass(currentClass);
            var tok = Tokeniser.tokLine(Extensions.operGetIndex(lines, i), true);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { continue; }
            indent = Extensions.len(Extensions.operGetIndex(tok, 0).value);
            tok.remove(0);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { continue; }
            while (LangUtil.isTruthy(indent > lastIndent)) {
                lastIndent += 4;
                cl . code = cl.code.trim();
                cl . code = cl.code + " {\n";
            }
            while (LangUtil.isTruthy(indent < lastIndent)) {
                cl . code = cl.code + " ".repeat(lastIndent) + "}\n";
                lastIndent -= 4;
            }
            scope = indent / 4;
            if (LangUtil.isTruthy(scope < lastScope)) { cleanLocals(scope); }
            cl . code = cl.code + " ".repeat(indent + 4);
            var out = compileStatement(tok, cl.code);
            cl = getOrCreateClass(currentClass);
            cl . code = out + "\n";
            lastIndent = indent;
            lastScope = scope;
        }
        var cl = getOrCreateClass(currentClass);
        while (LangUtil.isTruthy(lastIndent > 0)) {
            cl . code = cl.code + " ".repeat(lastIndent) + "}\n";
            lastIndent -= 4;
        }
        return new CompResult(classes, startTemplate, endTemplate);
    }
    public static Class getOrCreateClass(String name) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(classes.containsKey(name)))) {
            classes.put(name, new Class(name));
        }
        return classes.get(currentClass);
    }
    public static String compileStatement(ArrayList < Token > tok , String out) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return ""; }
        var types = getTokenTypes(tok);
        var startTok = Extensions.operGetIndex(types, 0);
        var endTok = Extensions.operGetIndex(types, - 1);
        var f = - 1;
        var f2 = - 1;
        if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.PACKAGE))) {
            packagePath = "";
            for (var j : LangUtil.asIterable(LangUtil.range(1, Extensions.len(tok), null))) {
                packagePath += Extensions.operGetIndex(tok, j).value;
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IMPORT))) {
            var importStr = "import ";
            for (var j : LangUtil.asIterable(LangUtil.range(1, Extensions.len(tok), null))) {
                importStr += Extensions.operGetIndex(tok, j).value;
            }
            startTemplate += importStr + ";\n";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.OPTION))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return "";
            }
            if (LangUtil.isTruthy(Extensions.len(tok) < 3)) {
                return "";
            }
            var optionName = Extensions.operGetIndex(tok, 1).value;
            var optionValue = Extensions.operGetIndex(tok, 2).value;
            Options.setOption(optionName, optionValue);
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ALIAS))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return "";
            }
            if (LangUtil.isTruthy(Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), - 1))) {
                return "";
            }
            if (LangUtil.isTruthy(f < 2)) {
                return "";
            }
            var name = Extensions.operGetIndex(tok, 1).value;
            var args = new ArrayList < String > ();
            if (LangUtil.isTruthy(f > 2)) {
                args = Tokeniser.extractArgsFromExpr(Extensions.operGetIndex(tok, 2).value);
            }
            var tokens = LangUtil.slice(tok, f + 1, null, 1);
            aliases.put(name, new Alias(tokens, args));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), - 1))) ? (f + 1 < Extensions.len(tok)) : (!Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), - 1)))) {
            var cl = getOrCreateClass(currentClass);
            cl . code = out;
            classes.put(currentClass, cl);
            var access = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            tok.remove(0);
            var typeArgs = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (tok))) {
                typeArgs = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, - 1, 1);
                tok.remove(0);
            }
            currentClass = Extensions.operGetIndex(tok, 0).value;
            cl = getOrCreateClass(currentClass);
            classes.put(currentClass, cl);
            out = cl.code;
            tok.remove(0);
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "extends")) : (tok))) {
                cl . extendsType = compileExpr(LangUtil.slice(tok, 1, null, 1));
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "implements")) : (tok))) {
                cl . implementsType = compileExpr(LangUtil.slice(tok, 1, null, 1));
            }
            cl . access = access.accessMod;
            cl . typeArgs = typeArgs;
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
                out += compileExpr(LangUtil.slice(tok, 1, null, 1));
                out += "))";
                if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.UNTIL))) { out += ")"; }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.FOR))) ? (!Extensions.operEq(((f = findTokenType(tok, Token.Type.IN))), - 1)) : (Extensions.operEq(startTok, Token.Type.FOR)))) {
            out += "for (";
            String varname = Extensions.operGetIndex(tok, 1).value;
            out += "var " + varname + " : LangUtil.asIterable(";
            out += compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += ")) ";
            declareLocal(varname, scope + 1);
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.IF))), - 1))) ? (Extensions.operEq(findTokenTypeRev(tok, Token.Type.ELSE), - 1)) : (!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.IF))), - 1)))) {
            out += "if (LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.WHILE))), - 1))) {
            out += "while (LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.UNTIL))), - 1))) {
            out += "while (!LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("false") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.FOR))), - 1))) {
            int f_in = findTokenTypeRev(tok, Token.Type.IN);
            if (LangUtil.isTruthy(!Extensions.operEq(f_in, - 1))) {
                out += "for (";
                String varname = Extensions.operGetIndex(tok, f + 1).value;
                out += "var " + varname + " : LangUtil.asIterable(";
                out += compileExpr(LangUtil.slice(tok, f_in + 1, null, 1));
                out += ")) { ";
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out += " }";
                declareLocal(varname, scope + 1);
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.TRY))) {
            out += "try ";
            String resources = compileExpr(LangUtil.slice(tok, 1, null, 1), false);
            if (LangUtil.isTruthy(resources)) { out += "(" + resources + ") "; }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.CATCH))) {
            out += "catch (";
            var s = "";
            for (var t : LangUtil.asIterable(LangUtil.slice(tok, 1, null, 1))) { s += t.value + " "; }
            s = s.trim();
            out += s + ")";
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return "";
            }
            out += LangUtil.slice(Extensions.operGetIndex(tok, 1).value, 1, - 1, 1);
        }
        else if (LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.SCOPE))) {
            if (LangUtil.isTruthy(false)) {
                
            }
            else {
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "override"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "override")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "!")))) : (tok))) {
                    out += "@Override ";
                    tok.remove(0);
                }
                var methodAccess = getMethodAccess(tok);
                tok = stripMethodAccess(tok);
                if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                    methodAccess . accessMod = AccessMod.PUBLIC;
                }
                var typeArgs = "";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (tok))) {
                    var v = Extensions.operGetIndex(tok, 0).value;
                    typeArgs = "<" + LangUtil.slice(v, 1, - 1, 1) + "> ";
                    tok.remove(0);
                }
                tok.remove(Extensions.len(tok) - 1);
                var args = "()";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, - 1).type, Token.Type.EXPR)) : (tok))) {
                    args = compileMethodArgs(Extensions.operGetIndex(tok, - 1).value, true);
                    tok.remove(Extensions.len(tok) - 1);
                }
                var methodName = "";
                if (LangUtil.isTruthy(tok)) {
                    methodName = Extensions.operGetIndex(tok, - 1).value;
                    tok.remove(Extensions.len(tok) - 1);
                    if (LangUtil.isTruthy(Extensions.operEq(methodName, "+"))) {
                        methodName = "operAdd";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "-"))) {
                        methodName = "operSub";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "*"))) {
                        methodName = "operMul";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "/"))) {
                        methodName = "operDiv";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "%"))) {
                        methodName = "operMod";
                    }
                    else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(methodName, "++"))) ? (Extensions.operEq(methodName, "++")) : (Extensions.operEq(methodName, "_++")))) {
                        methodName = "operPostInc";
                    }
                    else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(methodName, "--"))) ? (Extensions.operEq(methodName, "--")) : (Extensions.operEq(methodName, "_--")))) {
                        methodName = "operPostDec";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "++_"))) {
                        methodName = "operPreInc";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "--_"))) {
                        methodName = "operPreDec";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "!"))) {
                        methodName = "operNot";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "~"))) {
                        methodName = "operBitNot";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "=="))) {
                        methodName = "operEq";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "!="))) {
                        methodName = "operEq";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, ">"))) {
                        methodName = "operGt";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "<"))) {
                        methodName = "operLt";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, ">="))) {
                        methodName = "operGte";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "<="))) {
                        methodName = "operLte";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "&&"))) {
                        methodName = "operAnd";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(methodName, "||"))) {
                        methodName = "operOr";
                    }
                }
                else {
                    
                }
                var returnType = "";
                for (var t : LangUtil.asIterable(tok)) { returnType += t.value + " "; }
                if (LangUtil.isTruthy(!LangUtil.isTruthy(returnType))) { returnType = "void "; }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, "main"))) {
                    args = "(String[] args)";
                    methodAccess . isStatic = true;
                }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, currentClass))) {
                    out += methodAccess + " " + typeArgs + methodName + args;
                }
                else {
                    out += methodAccess + " " + typeArgs + returnType + methodName + args;
                }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.STATIC)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out += "static";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "@")) : (Extensions.len(tok) > 1))) {
            out += "@" + compileExpr(LangUtil.slice(tok, 1, null, 1));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.RETURN))) {
            out += "return";
            if (LangUtil.isTruthy(Extensions.len(tok) > 1)) {
                out += " " + compileExpr(LangUtil.slice(tok, 1, null, 1));
            }
            out += ";";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.BREAK)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out += "break;";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.CONTINUE)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out += "continue;";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.PASS)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ID))) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "println")))) : (Extensions.operEq(startTok, Token.Type.ID))))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "println")))) {
                out += "LangUtil." + Extensions.operGetIndex(tok, 0).value + "(";
                out += compileExpr(LangUtil.slice(tok, 1, null, 1));
                out += ");";
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(findTokenType(tok, Token.Type.ENUM), - 1))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, - 1).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, - 1).value;
            }
            else {
                
            }
            out += methodAccess + " enum " + name;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) >= 2)) ? (!LangUtil.isTruthy(indent)) : (Extensions.len(tok) >= 2))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            var value = "";
            if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), - 1))) {
                value = " = " + compileExpr(LangUtil.slice(tok, f + 1, null, 1));
                tok = LangUtil.slice(tok, null, f, 1);
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, - 1).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, - 1).value;
                tok.remove(Extensions.len(tok) - 1);
            }
            else {
                
            }
            var vartype = "";
            for (var t : LangUtil.asIterable(tok)) { vartype += t.value + " "; }
            if (LangUtil.isTruthy(!LangUtil.isTruthy(vartype))) {
                
            }
            LangUtil.println("Declaring " + name);
            declareLocal(name);
            out += methodAccess + " " + vartype + name + value + ";";
        }
        else {
            var ending = LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.COMMA)) ? ("") : (";");
            out += compileExpr(tok, false) + ending;
        }
        return out;
    }
    public static String compileExpr(ArrayList < Token > tok , boolean nested) {
        String out = "";
        int f = - 1;
        int f2 = - 1;
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return out; }
        if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.COMMA))), - 1))) {
            out += compileExpr(LangUtil.slice(tok, null, f, 1));
            out += ", ";
            out += compileExpr(LangUtil.slice(tok, f + 1, null, 1));
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), - 1))) {
            var vartype = "";
            var varname = "";
            if (LangUtil.isTruthy(Extensions.operEq(f, 2))) {
                vartype = Extensions.operGetIndex(tok, 0).value;
                varname = Extensions.operGetIndex(tok, 1).value;
            }
            else if (LangUtil.isTruthy(Extensions.operEq(f, 1))) {
                varname = Extensions.operGetIndex(tok, 0).value;
            }
            else {
                for (var j : LangUtil.asIterable(LangUtil.range(0, f, null))) { varname += Extensions.operGetIndex(tok, j).value + " "; }
                varname = varname.trim();
            }
            if (LangUtil.isTruthy(Extensions.operEq(vartype, "let"))) { vartype = "var"; }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            if (LangUtil.isTruthy((LangUtil.isTruthy(!LangUtil.isTruthy(localInScope(varname)))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(nested))) ? (!LangUtil.isTruthy(varname.contains(" "))) : (!LangUtil.isTruthy(nested))) : (!LangUtil.isTruthy(localInScope(varname))))) {
                if (LangUtil.isTruthy(!LangUtil.isTruthy(vartype))) { vartype = "var"; }
                declareLocal(varname);
            }
            if (LangUtil.isTruthy(vartype)) {
                out += vartype + " " + varname + " = ";
            }
            else {
                out += varname + " = ";
            }
            out += compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.COMP_ASSIGN))), - 1))) {
            var varname = "";
            var oper = LangUtil.slice(Extensions.operGetIndex(tok, f).value, null, - 1, 1);
            if (LangUtil.isTruthy(Extensions.operEq(f, 1))) {
                varname = Extensions.operGetIndex(tok, 0).value;
            }
            else {
                
            }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            ArrayList < Token > exprTok = LangUtil.slice(tok, f + 1, null, 1);
            if (LangUtil.isTruthy(List.of(".", "**", "??").contains(oper))) {
                exprTok.add(0, Token.fromString(varname));
                exprTok.add(1, Token.fromString(oper));
                out += varname + " = ";
            }
            else {
                out += varname + " " + Extensions.operGetIndex(tok, f).value + " ";
            }
            out += compileExpr(exprTok);
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ARROW))), - 1))) {
            var args = Extensions.operGetIndex(tok, 0).value;
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR))) {
                args = compileMethodArgs(args);
                tok.remove(0);
            }
            else {
                return "";
            }
            out += args + " -> " + compileExpr(LangUtil.slice(tok, 1, null, 1));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "?"))), - 1))) ? (!Extensions.operEq(((f2 = findToken(tok, ":"))), - 1)) : (!Extensions.operEq(((f = findToken(tok, "?"))), - 1)))) {
            String cond = compileExpr(LangUtil.slice(tok, null, f, 1));
            String lhs = compileExpr(LangUtil.slice(tok, f + 1, f2, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f2 + 1, null, 1));
            out += "LangUtil.isTruthy(" + cond + ") ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "if"))), - 1))) ? (!Extensions.operEq(((f2 = findToken(tok, "else"))), - 1)) : (!Extensions.operEq(((f = findToken(tok, "if"))), - 1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String cond = compileExpr(LangUtil.slice(tok, f + 1, f2, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f2 + 1, null, 1));
            out += "LangUtil.isTruthy(" + cond + ") ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "??"))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += "((" + lhs + ") != null) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("||", "or")))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + lhs + ") : (" + rhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("&&", "and")))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += "(LangUtil.isTruthy(" + lhs + ")) ? (" + rhs + ") : (" + lhs + ")";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "|"))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " | " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "^"))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " ^ " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "&"))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " & " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("==", "!=", "===", "!==")))), - 1))) {
            String oper = Extensions.operGetIndex(tok, f).value;
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
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
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("<", ">", "<=", ">=", "is")))), - 1))) {
            String oper = Extensions.operGetIndex(tok, f).value;
            if (LangUtil.isTruthy(Extensions.operEq(oper, "is"))) {
                oper = "==";
            }
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " " + oper + " " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("+", "-")))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " " + Extensions.operGetIndex(tok, f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("*", "/", "%")))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += lhs + " " + Extensions.operGetIndex(tok, f).value + " " + rhs;
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.UNARY_OPER)) : (Extensions.len(tok) > 1))) {
            String oper = Extensions.operGetIndex(tok, 0).value;
            String expr = compileExpr(LangUtil.slice(tok, 1, null, 1));
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "not"))) ? (Extensions.operEq(oper, "not")) : (Extensions.operEq(oper, "!")))) {
                out += "!LangUtil.isTruthy(" + expr + ")";
            }
            else {
                out += Extensions.operGetIndex(tok, 0).value + expr;
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "**"))), - 1))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            out += "Math.pow(" + lhs + ", " + rhs + ")";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, - 1).type, Token.Type.INCREMENT)) : (Extensions.len(tok) > 1))) {
            String name = compileExpr(LangUtil.slice(tok, null, - 1, 1));
            out += name + "++";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, - 1).type, Token.Type.DECREMENT)) : (Extensions.len(tok) > 1))) {
            String name = compileExpr(LangUtil.slice(tok, null, - 1, 1));
            out += name + "--";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (!Extensions.operEq(((f = findTokenType(tok, Token.Type.NULL_CHECK))), - 1)) : (Extensions.len(tok) > 1))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
            var tempVar = getNextTemp();
            if (LangUtil.isTruthy(rhs.startsWith("LangUtil.nullCheck"))) {
                rhs = LangUtil.slice(rhs, Extensions.len("LangUtil.nullCheck("), - 1, 1);
                out += "LangUtil.nullCheck(" + lhs + ", " + tempVar + " -> LangUtil.nullCheck(" + tempVar + "." + rhs + "))";
            }
            else {
                out += "LangUtil.nullCheck(" + lhs + ", " + tempVar + " -> " + tempVar + "." + rhs + ")";
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (((f = findAnyTokenTypeRev(tok, List.of(Token.Type.EXPR, Token.Type.DOT, Token.Type.SQUARE_EXPR)))) > 0) : (Extensions.len(tok) > 1))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.EXPR))) {
                var name = compileExpr(LangUtil.slice(tok, null, - 1, 1));
                var isNew = Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "new");
                if (LangUtil.isTruthy(Extensions.operEq(name, "len"))) {
                    name = "Extensions.len";
                }
                if (LangUtil.isTruthy((LangUtil.isTruthy(StringParser.isPascalCase(Extensions.operGetIndex(tok, - 2).value))) ? (!LangUtil.isTruthy(isNew)) : (StringParser.isPascalCase(Extensions.operGetIndex(tok, - 2).value)))) {
                    name = "new " + name;
                    LangUtil.println(LangUtil.slice(tok, null, - 1, 1) + ", " + name);
                }
                var t = Extensions.operGetIndex(tok, - 1);
                var args = LangUtil.slice(t.value, 1, - 1, 1);
                out += name + "(" + compileExpr(Tokeniser.tokLine(args)) + ")";
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.DOT))) {
                var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
                var rhs = compileExpr(LangUtil.slice(tok, f + 1, null, 1));
                out += lhs + "." + rhs;
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.SQUARE_EXPR))) {
                var iterable = compileExpr(LangUtil.slice(tok, null, f, 1));
                var expr = Extensions.operGetIndex(tok, f).value;
                expr = LangUtil.slice(expr, 1, - 1, 1);
                tok = Tokeniser.tokLine(expr);
                if (LangUtil.isTruthy(!Extensions.operEq(findToken(tok, ":"), - 1))) {
                    var startTokens = new ArrayList < Token > ();
                    var endTokens = new ArrayList < Token > ();
                    var stepTokens = new ArrayList < Token > ();
                    while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ":")) : (tok))) {
                        startTokens.add(Extensions.operGetIndex(tok, 0));
                        tok.remove(0);
                    }
                    tok.remove(0);
                    while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ":")) : (tok))) {
                        endTokens.add(Extensions.operGetIndex(tok, 0));
                        tok.remove(0);
                    }
                    if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ":")) : (tok))) {
                        stepTokens = tok;
                        tok.remove(0);
                    }
                    var start = compileExpr(startTokens);
                    var end = compileExpr(endTokens);
                    var step = compileExpr(stepTokens);
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(start))) { start = "null"; }
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(end))) { end = "null"; }
                    if (LangUtil.isTruthy(!LangUtil.isTruthy(step))) { step = "1"; }
                    out += "LangUtil.slice(" + iterable + ", " + start + ", " + end + ", " + step + ")";
                }
                else {
                    out += "Extensions.operGetIndex(" + iterable + ", " + compileExpr(tok) + ")";
                }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, - 1, 1);
            out += "(" + compileExpr(Tokeniser.tokLine(expr)) + ")";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.SQUARE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, - 1, 1);
            tok = Tokeniser.tokLine(expr);
            if (LangUtil.isTruthy(!Extensions.operEq(findTokenType(tok, Token.Type.RANGE), - 1))) {
                out += compileRange(tok);
            }
            else {
                out += "List.of(" + compileExpr(tok) + ")";
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.BRACE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            String expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, - 1, 1);
            out += "{" + compileExpr(Tokeniser.tokLine(expr)) + "}";
        }
        else {
            for (var j : LangUtil.asIterable(Extensions.len(tok))) {
                var t = Extensions.operGetIndex(tok, j);
                if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.EXPR))) {
                    String raw = LangUtil.slice(t.value, 1, - 1, 1);
                    ArrayList < Token > tokens = Tokeniser.tokLine(raw);
                    out += "(" + compileExpr(tokens) + ")";
                }
                else {
                    out += t.value + " ";
                }
            }
        }
        return out.trim();
    }
    public static String compileExpr(ArrayList < Token > tok) {
        return compileExpr(tok, true);
    }
    public static String compileMethodArgs(String expr , boolean declareLocals) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(expr.startsWith("("))) ? (expr.endsWith(")")) : (expr.startsWith("(")))) {
            expr = LangUtil.slice(expr, 1, - 1, 1);
        }
        var out = "";
        var tok = Tokeniser.tokLine(expr);
        var buffer = new ArrayList < Token > ();
        tok.add(Token.fromString(","));
        ++ scope;
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.COMMA))) {
                if (LangUtil.isTruthy(Extensions.operEq(Extensions.len(buffer), 1))) {
                    if (LangUtil.isTruthy(declareLocals)) { declareLocal(Extensions.operGetIndex(buffer, 0).value); }
                    out += "Object " + Extensions.operGetIndex(buffer, 0).value + ", ";
                }
                else if (LangUtil.isTruthy(Extensions.len(buffer) > 1)) {
                    if (LangUtil.isTruthy(declareLocals)) { declareLocal(Extensions.operGetIndex(buffer, - 1).value); }
                    for (var j : LangUtil.asIterable(LangUtil.range(0, Extensions.len(buffer), null))) { out += Extensions.operGetIndex(buffer, j).value + " "; }
                    out += ", ";
                }
                buffer.clear();
            }
            else {
                buffer.add(t);
            }
        }
        -- scope;
        out = out.trim();
        if (LangUtil.isTruthy(out.endsWith(","))) {
            out = LangUtil.slice(out, null, - 1, 1).trim();
        }
        return "(" + out.trim() + ")";
    }
    public static String compileMethodArgs(String expr) {
        return compileMethodArgs(expr, false);
    }
    public static String compileRange(ArrayList < Token > tok) {
        var startTokens = new ArrayList < Token > ();
        var endTokens = new ArrayList < Token > ();
        var stepTokens = new ArrayList < Token > ();
        while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.RANGE)) : (tok))) {
            startTokens.add(Extensions.operGetIndex(tok, 0));
            tok.remove(0);
        }
        tok.remove(0);
        while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.RANGE)) : (tok))) {
            endTokens.add(Extensions.operGetIndex(tok, 0));
            tok.remove(0);
        }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.RANGE)) : (tok))) {
            stepTokens = tok;
            tok.remove(0);
        }
        var start = compileExpr(startTokens);
        var end = compileExpr(endTokens);
        var step = compileExpr(stepTokens);
        if (LangUtil.isTruthy(!LangUtil.isTruthy(start))) { start = "0"; }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(end))) { end = "null"; }
        if (LangUtil.isTruthy(!LangUtil.isTruthy(step))) { step = "null"; }
        return "LangUtil.range(" + start + ", " + end + ", " + step + ")";
    }
    public static String getNextTemp() {
        return "_t" + nextTempVar++;
    }
    public static int findToken(ArrayList < Token > tok , String value) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return - 1;
    }
    public static int findTokenRev(ArrayList < Token > tok , String value) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return - 1;
    }
    public static int findAnyToken(ArrayList < Token > tok , List < String > values) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return - 1;
    }
    public static int findAnyTokenRev(ArrayList < Token > tok , List < String > values) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return - 1;
    }
    public static int findTokenType(ArrayList < Token > tok , Token . Type type) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return - 1;
    }
    public static int findTokenTypeRev(ArrayList < Token > tok , Token . Type type) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return - 1;
    }
    public static int findAnyTokenType(ArrayList < Token > tok , List < Token . Type > types) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
        }
        return - 1;
    }
    public static int findAnyTokenTypeRev(ArrayList < Token > tok , List < Token . Type > types) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
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
            var t = Extensions.operGetIndex(tok, j).type;
            var v = Extensions.operGetIndex(tok, j).value;
            if (LangUtil.isTruthy(Extensions.operEq(accessMod, AccessMod.NONE))) {
                accessMod = MethodAccess.accessModFromToken(Extensions.operGetIndex(tok, j));
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "static"))) ? (Extensions.operEq(v, "static")) : (Extensions.operEq(v, "#")))) { isStatic = true; }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(v, "instance"))) ? (Extensions.operEq(v, "instance")) : (Extensions.operEq(v, "^")))) { isStatic = false; }
        }
        return new MethodAccess(accessMod, isStatic);
    }
    public static MethodAccess getMethodAccess(ArrayList < Token > tok) {
        return getMethodAccess(tok, Extensions.len(tok));
    }
    public static ArrayList < Token > stripMethodAccess(ArrayList < Token > tok , int end) {
        for (var i : LangUtil.asIterable(end)) {
            var t = Extensions.operGetIndex(tok, i);
            var v = t.value;
            if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "static"))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "#"))) ? ((LangUtil.isTruthy(!Extensions.operEq(v, "instance"))) ? (!Extensions.operEq(v, "^")) : (!Extensions.operEq(v, "instance"))) : (!Extensions.operEq(v, "#"))) : (!Extensions.operEq(v, "static"))) : (Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))))) {
                return LangUtil.slice(tok, i, null, 1);
            }
        }
        return tok;
    }
    public static ArrayList < Token > stripMethodAccess(ArrayList < Token > tok) {
        return stripMethodAccess(tok, Extensions.len(tok));
    }
    public static void declareLocal(String name , int scope) {
        locals.put(name, scope);
    }
    public static void declareLocal(String name) {
        declareLocal(name, scope);
    }
    public static boolean localInScope(String name) {
        return scope >= locals.getOrDefault(name, scope + 1);
    }
    public static void cleanLocals(int scope) {
        var toRemove = new ArrayList < String > ();
        for (var name : LangUtil.asIterable(locals.keySet())) {
            if (LangUtil.isTruthy(locals.get(name) > scope)) { toRemove.add(name); }
        }
        for (var name : LangUtil.asIterable(toRemove)) { locals.remove(name); }
    }
}

