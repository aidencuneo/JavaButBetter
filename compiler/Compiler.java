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
                cl . code = Extensions.operAdd(cl.code, " {\n");
            }
            while (LangUtil.isTruthy(indent < lastIndent)) {
                cl . code = Extensions.operAdd(Extensions.operAdd(cl.code, " ".repeat(lastIndent)), "}\n");
                lastIndent -= 4;
            }
            scope = Extensions.operDiv(indent, 4);
            if (LangUtil.isTruthy(scope < lastScope)) { cleanLocals(scope); }
            cl . code = Extensions.operAdd(cl.code, " ".repeat(Extensions.operAdd(indent, 4)));
            var out = compileStatement(tok, cl.code);
            cl = getOrCreateClass(currentClass);
            cl . code = Extensions.operAdd(out, "\n");
            lastIndent = indent;
            lastScope = scope;
        }
        var cl = getOrCreateClass(currentClass);
        while (LangUtil.isTruthy(lastIndent > 0)) {
            cl . code = Extensions.operAdd(Extensions.operAdd(cl.code, " ".repeat(lastIndent)), "}\n");
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
        var types = Tokeniser.getTokenTypes(tok);
        var startTok = Extensions.operGetIndex(types, 0);
        var endTok = Extensions.operGetIndex(types, Extensions.operUnarySub(1));
        var f = Extensions.operUnarySub(1);
        var f2 = Extensions.operUnarySub(1);
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
            startTemplate += Extensions.operAdd(importStr, ";\n");
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
            if (LangUtil.isTruthy(Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), Extensions.operUnarySub(1)))) {
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
            var tokens = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
            if (LangUtil.isTruthy(tokens)) {
                aliases.put(name, new Alias(tokens, args));
            }
            else {
                if (LangUtil.isTruthy(Extensions.operIn(name, aliases))) { aliases.remove(name); }
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.REGEX))) {
            
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), Extensions.operUnarySub(1)))) ? (Extensions.operAdd(f, 1) < Extensions.len(tok)) : (!Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), Extensions.operUnarySub(1))))) {
            var cl = getOrCreateClass(currentClass);
            cl . code = out;
            classes.put(currentClass, cl);
            var access = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            tok.remove(0);
            var typeArgs = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (tok))) {
                typeArgs = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, Extensions.operUnarySub(1), 1);
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
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.FOR))) ? (!Extensions.operEq(((f = findToken(tok, "in"))), Extensions.operUnarySub(1))) : (Extensions.operEq(startTok, Token.Type.FOR)))) {
            out += "for (";
            String varname = Extensions.operGetIndex(tok, 1).value;
            out += Extensions.operAdd(Extensions.operAdd("var ", varname), " : LangUtil.asIterable(");
            out += compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += ")) ";
            declareLocal(varname, Extensions.operAdd(scope, 1));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.IF))), Extensions.operUnarySub(1)))) ? (Extensions.operEq(findTokenTypeRev(tok, Token.Type.ELSE), Extensions.operUnarySub(1))) : (!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.IF))), Extensions.operUnarySub(1))))) {
            out += "if (LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.WHILE))), Extensions.operUnarySub(1)))) {
            out += "while (LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.UNTIL))), Extensions.operUnarySub(1)))) {
            out += "while (!LangUtil.isTruthy(";
            String cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += LangUtil.isTruthy(cond.isEmpty()) ? ("false") : (cond);
            out += ")) { ";
            out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
            out += " }";
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.FOR))), Extensions.operUnarySub(1)))) {
            int f_in = findTokenRev(tok, "in");
            if (LangUtil.isTruthy(!Extensions.operEq(f_in, Extensions.operUnarySub(1)))) {
                out += "for (";
                var varname = Extensions.operGetIndex(tok, Extensions.operAdd(f, 1)).value;
                out += Extensions.operAdd(Extensions.operAdd("var ", varname), " : LangUtil.asIterable(");
                out += compileExpr(LangUtil.slice(tok, Extensions.operAdd(f_in, 1), null, 1));
                out += ")) { ";
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out += " }";
                declareLocal(varname, Extensions.operAdd(scope, 1));
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.TRY))) {
            out += "try ";
            String resources = compileExpr(LangUtil.slice(tok, 1, null, 1), false);
            if (LangUtil.isTruthy(resources)) { out += Extensions.operAdd(Extensions.operAdd("(", resources), ") "); }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.CATCH))) {
            out += "catch (";
            var s = "";
            for (var t : LangUtil.asIterable(LangUtil.slice(tok, 1, null, 1))) { s += Extensions.operAdd(t.value, " "); }
            s = s.trim();
            out += Extensions.operAdd(s, ")");
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return "";
            }
            out += LangUtil.slice(Extensions.operGetIndex(tok, 1).value, 1, Extensions.operUnarySub(1), 1);
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
                    typeArgs = Extensions.operAdd(Extensions.operAdd("<", LangUtil.slice(v, 1, Extensions.operUnarySub(1), 1)), "> ");
                    tok.remove(0);
                }
                tok.remove(Extensions.operSub(Extensions.len(tok), 1));
                var args = "()";
                if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.EXPR)) : (tok))) {
                    args = compileMethodArgs(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value, true);
                    tok.remove(Extensions.operSub(Extensions.len(tok), 1));
                }
                var methodName = "";
                if (LangUtil.isTruthy(tok)) {
                    methodName = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
                    tok.remove(Extensions.operSub(Extensions.len(tok), 1));
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
                for (var t : LangUtil.asIterable(tok)) { returnType += Extensions.operAdd(t.value, " "); }
                if (LangUtil.isTruthy(!LangUtil.isTruthy(returnType))) { returnType = "void "; }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, "main"))) {
                    args = "(String[] args)";
                    methodAccess . isStatic = true;
                }
                if (LangUtil.isTruthy(Extensions.operEq(methodName, currentClass))) {
                    out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), typeArgs), methodName), args);
                }
                else {
                    out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), typeArgs), returnType), methodName), args);
                }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.STATIC)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out += "static";
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "@")) : (Extensions.len(tok) > 1))) {
            out += Extensions.operAdd("@", compileExpr(LangUtil.slice(tok, 1, null, 1)));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.RETURN))) {
            out += "return";
            if (LangUtil.isTruthy(Extensions.len(tok) > 1)) {
                out += Extensions.operAdd(" ", compileExpr(LangUtil.slice(tok, 1, null, 1)));
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
                out += Extensions.operAdd(Extensions.operAdd("LangUtil.", Extensions.operGetIndex(tok, 0).value), "(");
                out += compileExpr(LangUtil.slice(tok, 1, null, 1));
                out += ");";
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(findTokenType(tok, Token.Type.ENUM), Extensions.operUnarySub(1)))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
            }
            else {
                
            }
            out += Extensions.operAdd(Extensions.operAdd(methodAccess, " enum "), name);
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) >= 2)) ? (!LangUtil.isTruthy(indent)) : (Extensions.len(tok) >= 2))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess . accessMod = AccessMod.PUBLIC;
            }
            var value = "";
            if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), Extensions.operUnarySub(1)))) {
                value = Extensions.operAdd(" = ", compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1)));
                tok = LangUtil.slice(tok, null, f, 1);
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
                tok.remove(Extensions.operSub(Extensions.len(tok), 1));
            }
            else {
                
            }
            var vartype = "";
            for (var t : LangUtil.asIterable(tok)) { vartype += Extensions.operAdd(t.value, " "); }
            if (LangUtil.isTruthy(!LangUtil.isTruthy(vartype))) {
                
            }
            declareLocal(name);
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), vartype), name), value), ";");
        }
        else {
            var ending = LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.COMMA)) ? ("") : (";");
            out += Extensions.operAdd(compileExpr(tok, false), ending);
        }
        return out;
    }
    public static String compileExpr(ArrayList < Token > tok , boolean nested) {
        String out = "";
        int f = Extensions.operUnarySub(1);
        int f2 = Extensions.operUnarySub(1);
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return out; }
        if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.COMMA))), Extensions.operUnarySub(1)))) {
            out += compileExpr(LangUtil.slice(tok, null, f, 1));
            out += ", ";
            out += compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return "";
            }
            out += LangUtil.slice(Extensions.operGetIndex(tok, 1).value, 1, Extensions.operUnarySub(1), 1);
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), Extensions.operUnarySub(1)))) {
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
                for (var j : LangUtil.asIterable(LangUtil.range(0, f, null))) { varname += Extensions.operAdd(Extensions.operGetIndex(tok, j).value, " "); }
                varname = varname.trim();
            }
            if (LangUtil.isTruthy(Extensions.operEq(vartype, "let"))) { vartype = "var"; }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            if (LangUtil.isTruthy((LangUtil.isTruthy(!LangUtil.isTruthy(localInScope(varname)))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(nested))) ? (!LangUtil.isTruthy(varname.contains(" "))) : (!LangUtil.isTruthy(nested))) : (!LangUtil.isTruthy(localInScope(varname))))) {
                if (LangUtil.isTruthy(!LangUtil.isTruthy(vartype))) { vartype = "var"; }
                declareLocal(varname);
            }
            if (LangUtil.isTruthy(vartype)) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(vartype, " "), varname), " = ");
            }
            else {
                out += Extensions.operAdd(varname, " = ");
            }
            out += compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.COMP_ASSIGN))), Extensions.operUnarySub(1)))) {
            var varname = "";
            var oper = LangUtil.slice(Extensions.operGetIndex(tok, f).value, null, Extensions.operUnarySub(1), 1);
            if (LangUtil.isTruthy(Extensions.operEq(f, 1))) {
                varname = Extensions.operGetIndex(tok, 0).value;
            }
            else {
                
            }
            if (LangUtil.isTruthy(nested)) { out += "("; }
            ArrayList < Token > exprTok = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
            if (LangUtil.isTruthy(List.of(".", "**", "??").contains(oper))) {
                exprTok.add(0, Token.fromString(varname));
                exprTok.add(1, Token.fromString(oper));
                out += Extensions.operAdd(varname, " = ");
            }
            else {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(varname, " "), Extensions.operGetIndex(tok, f).value), " ");
            }
            out += compileExpr(exprTok);
            if (LangUtil.isTruthy(nested)) { out += ")"; }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenType(tok, Token.Type.ARROW))), Extensions.operUnarySub(1)))) {
            var args = Extensions.operGetIndex(tok, 0).value;
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR))) {
                args = compileMethodArgs(args);
                tok.remove(0);
            }
            else {
                return "";
            }
            out += Extensions.operAdd(Extensions.operAdd(args, " -> "), compileExpr(LangUtil.slice(tok, 1, null, 1)));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "?"))), Extensions.operUnarySub(1)))) ? (!Extensions.operEq(((f2 = findToken(tok, ":"))), Extensions.operUnarySub(1))) : (!Extensions.operEq(((f = findToken(tok, "?"))), Extensions.operUnarySub(1))))) {
            String cond = compileExpr(LangUtil.slice(tok, null, f, 1));
            String lhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), f2, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f2, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.isTruthy(", cond), ") ? ("), lhs), ") : ("), rhs), ")");
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "if"))), Extensions.operUnarySub(1)))) ? (!Extensions.operEq(((f2 = findToken(tok, "else"))), Extensions.operUnarySub(1))) : (!Extensions.operEq(((f = findToken(tok, "if"))), Extensions.operUnarySub(1))))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), f2, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f2, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.isTruthy(", cond), ") ? ("), lhs), ") : ("), rhs), ")");
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findToken(tok, "??"))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("((", lhs), ") != null) ? ("), lhs), ") : ("), rhs), ")");
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("||", "or")))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("(LangUtil.isTruthy(", lhs), ")) ? ("), lhs), ") : ("), rhs), ")");
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyToken(tok, List.of("&&", "and")))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("(LangUtil.isTruthy(", lhs), ")) ? ("), rhs), ") : ("), lhs), ")");
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenRev(tok, "|"))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(lhs, " | "), rhs);
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenRev(tok, "^"))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(lhs, " ^ "), rhs);
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenRev(tok, "&"))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(lhs, " & "), rhs);
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyTokenRev(tok, List.of("==", "!=", "===", "!==")))), Extensions.operUnarySub(1)))) {
            String oper = Extensions.operGetIndex(tok, f).value;
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "=="))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operEq(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!="))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("!Extensions.operEq(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "==="))) {
                out += Extensions.operAdd(Extensions.operAdd(lhs, " == "), rhs);
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!=="))) {
                out += Extensions.operAdd(Extensions.operAdd(lhs, " != "), rhs);
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findAnyTokenRev(tok, List.of("<", ">", "<=", ">=", "is", "isnt", "isnot", "in", "inside", "notin", "outside")))), Extensions.operUnarySub(1)))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "is"))) {
                oper = "==";
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "isnt"))) ? (Extensions.operEq(oper, "isnt")) : (Extensions.operEq(oper, "isnot")))) {
                oper = "!=";
            }
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "in"))) ? (Extensions.operEq(oper, "in")) : (Extensions.operEq(oper, "inside")))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operIn(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "notin"))) ? (Extensions.operEq(oper, "notin")) : (Extensions.operEq(oper, "outside")))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("!Extensions.operIn(", lhs), ", "), rhs), ")");
            }
            else {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(lhs, " "), oper), " "), rhs);
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(((f = findAnyTokenRev(tok, List.of("+", "-")))) > 0)) ? (Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.BIN_OPER)) : (((f = findAnyTokenRev(tok, List.of("+", "-")))) > 0))) {
            String oper = Extensions.operGetIndex(tok, f).value;
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "+"))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operAdd(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "-"))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operSub(", lhs), ", "), rhs), ")");
            }
        }
        else if (LangUtil.isTruthy(((f = findAnyTokenRev(tok, List.of("*", "/", "%")))) > 0)) {
            String oper = Extensions.operGetIndex(tok, f).value;
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "*"))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operMul(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "/"))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operDiv(", lhs), ", "), rhs), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "%"))) {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operMod(", lhs), ", "), rhs), ")");
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.UNARY_OPER))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.UNARY_OPER)) : (List.of("+", "-").contains(Extensions.operGetIndex(tok, 0).value)))) : (Extensions.len(tok) > 1))) {
            String oper = Extensions.operGetIndex(tok, 0).value;
            String expr = compileExpr(LangUtil.slice(tok, 1, null, 1));
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "not"))) ? (Extensions.operEq(oper, "not")) : (Extensions.operEq(oper, "!")))) {
                out += Extensions.operAdd(Extensions.operAdd("!LangUtil.isTruthy(", expr), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "+"))) {
                out += Extensions.operAdd(Extensions.operAdd("Extensions.operUnaryAdd(", expr), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "-"))) {
                out += Extensions.operAdd(Extensions.operAdd("Extensions.operUnarySub(", expr), ")");
            }
            else {
                out += Extensions.operAdd(Extensions.operGetIndex(tok, 0).value, expr);
            }
        }
        else if (LangUtil.isTruthy(!Extensions.operEq(((f = findTokenRev(tok, "**"))), Extensions.operUnarySub(1)))) {
            String lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            String rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Math.pow(", lhs), ", "), rhs), ")");
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.INCREMENT)) : (Extensions.len(tok) > 1))) {
            String name = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
            out += Extensions.operAdd(name, "++");
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.DECREMENT)) : (Extensions.len(tok) > 1))) {
            String name = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
            out += Extensions.operAdd(name, "--");
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (!Extensions.operEq(((f = findTokenType(tok, Token.Type.NULL_CHECK))), Extensions.operUnarySub(1))) : (Extensions.len(tok) > 1))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            var tempVar = getNextTemp();
            if (LangUtil.isTruthy(rhs.startsWith("LangUtil.nullCheck"))) {
                rhs = LangUtil.slice(rhs, Extensions.len("LangUtil.nullCheck("), Extensions.operUnarySub(1), 1);
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.nullCheck(", lhs), ", "), tempVar), " -> LangUtil.nullCheck("), tempVar), "."), rhs), "))");
            }
            else {
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.nullCheck(", lhs), ", "), tempVar), " -> "), tempVar), "."), rhs), ")");
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.len(tok) > 1)) ? (((f = findAnyTokenTypeRev(tok, List.of(Token.Type.EXPR, Token.Type.DOT, Token.Type.SQUARE_EXPR)))) > 0) : (Extensions.len(tok) > 1))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.EXPR))) {
                var name = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
                var isNew = Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "new");
                if (LangUtil.isTruthy(Extensions.operIn(name, List.of("len")))) {
                    name = Extensions.operAdd("Extensions.", name);
                }
                else if (LangUtil.isTruthy(Extensions.operIn(name, List.of("round", "roundstr")))) {
                    name = Extensions.operAdd("LangUtil.", name);
                }
                if (LangUtil.isTruthy((LangUtil.isTruthy(StringParser.isPascalCase(Extensions.operGetIndex(tok, Extensions.operUnarySub(2)).value))) ? (!LangUtil.isTruthy(isNew)) : (StringParser.isPascalCase(Extensions.operGetIndex(tok, Extensions.operUnarySub(2)).value)))) {
                    name = Extensions.operAdd("new ", name);
                }
                var t = Extensions.operGetIndex(tok, Extensions.operUnarySub(1));
                var args = LangUtil.slice(t.value, 1, Extensions.operUnarySub(1), 1);
                out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(name, "("), compileExpr(Tokeniser.tokLine(args))), ")");
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.DOT))) {
                var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
                var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out += Extensions.operAdd(Extensions.operAdd(lhs, "."), rhs);
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.SQUARE_EXPR))) {
                var iterable = compileExpr(LangUtil.slice(tok, null, f, 1));
                var expr = Extensions.operGetIndex(tok, f).value;
                expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
                tok = Tokeniser.tokLine(expr);
                if (LangUtil.isTruthy(!Extensions.operEq(findToken(tok, ":"), Extensions.operUnarySub(1)))) {
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
                    out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.slice(", iterable), ", "), start), ", "), end), ", "), step), ")");
                }
                else {
                    out += Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operGetIndex(", iterable), ", "), compileExpr(tok)), ")");
                }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
            out += Extensions.operAdd(Extensions.operAdd("(", compileExpr(Tokeniser.tokLine(expr))), ")");
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.SQUARE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
            tok = Tokeniser.tokLine(expr);
            if (LangUtil.isTruthy(!Extensions.operEq(findTokenType(tok, Token.Type.RANGE), Extensions.operUnarySub(1)))) {
                out += compileRange(tok);
            }
            else {
                out += Extensions.operAdd(Extensions.operAdd("List.of(", StringParser.trimComma(compileExpr(tok))), ")");
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.BRACE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            String expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
            out += Extensions.operAdd(Extensions.operAdd("{", compileExpr(Tokeniser.tokLine(expr))), "}");
        }
        else {
            for (var j : LangUtil.asIterable(Extensions.len(tok))) {
                var t = Extensions.operGetIndex(tok, j);
                if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.EXPR))) {
                    String raw = LangUtil.slice(t.value, 1, Extensions.operUnarySub(1), 1);
                    ArrayList < Token > tokens = Tokeniser.tokLine(raw);
                    out += Extensions.operAdd(Extensions.operAdd("(", compileExpr(tokens)), ")");
                }
                else {
                    out += Extensions.operAdd(t.value, " ");
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
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
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
                    out += Extensions.operAdd(Extensions.operAdd("Object ", Extensions.operGetIndex(buffer, 0).value), ", ");
                }
                else if (LangUtil.isTruthy(Extensions.len(buffer) > 1)) {
                    if (LangUtil.isTruthy(declareLocals)) { declareLocal(Extensions.operGetIndex(buffer, Extensions.operUnarySub(1)).value); }
                    for (var j : LangUtil.asIterable(LangUtil.range(0, Extensions.len(buffer), null))) { out += Extensions.operAdd(Extensions.operGetIndex(buffer, j).value, " "); }
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
            out = LangUtil.slice(out, null, Extensions.operUnarySub(1), 1).trim();
        }
        return Extensions.operAdd(Extensions.operAdd("(", out.trim()), ")");
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
        return Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.range(", start), ", "), end), ", "), step), ")");
    }
    public static String getNextTemp() {
        return Extensions.operAdd("_t", nextTempVar++);
    }
    public static int findToken(ArrayList < Token > tok , String value) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenRev(ArrayList < Token > tok , String value) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyToken(ArrayList < Token > tok , List < String > values) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenRev(ArrayList < Token > tok , List < String > values) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenType(ArrayList < Token > tok , Token . Type type) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenTypeRev(ArrayList < Token > tok , Token . Type type) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenType(ArrayList < Token > tok , List < Token . Type > types) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenTypeRev(ArrayList < Token > tok , List < Token . Type > types) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
        }
        return Extensions.operUnarySub(1);
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
        return scope >= locals.getOrDefault(name, Extensions.operAdd(scope, 1));
    }
    public static void cleanLocals(int scope) {
        var toRemove = new ArrayList < String > ();
        for (var name : LangUtil.asIterable(locals.keySet())) {
            if (LangUtil.isTruthy(locals.get(name) > scope)) { toRemove.add(name); }
        }
        for (var name : LangUtil.asIterable(toRemove)) { locals.remove(name); }
    }
}

