import java.io.*;
import java.util.*;

public class Compiler {
    public static String mainClassName = "";
    public static String currentClass = "";
    public static String startTemplate = "";
    public static String endTemplate = "";
    public static String packagePath = "";
    public static HashMap<String, Class> classes = null;
    public static HashMap<String, Alias> aliases = null;
    public static HashMap<String, Integer> locals = null;
    public static int nextTempVar = 0;
    public static int indent = 0;
    public static int scope = 0;
    public static boolean defaultStatic = false;
    public static CompResult compileFile(String className, String code) {
        mainClassName = className;
        currentClass = className;
        startTemplate = "import java.io.*;\nimport java.util.*;\n";
        endTemplate = "";
        classes = new HashMap<String, Class>();
        aliases = new HashMap<String, Alias>();
        locals = new HashMap<String, Integer>();
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
            while (LangUtil.isTruthy(Extensions.operGt(indent, lastIndent))) {
                lastIndent = Extensions.operAdd(lastIndent, (4));
                cl.code = cl.code.trim();
                cl.code = Extensions.operAdd(cl.code, " {\n");
            }
            while (LangUtil.isTruthy(Extensions.operLt(indent, lastIndent))) {
                cl.code = Extensions.operAdd(Extensions.operAdd(cl.code, " ".repeat(lastIndent)), "}\n");
                lastIndent = Extensions.operSub(lastIndent, (4));
            }
            scope = Extensions.operDiv(indent, 4);
            if (LangUtil.isTruthy(Extensions.operLt(scope, lastScope))) { cleanLocals(scope); }
            cl.code = Extensions.operAdd(cl.code, " ".repeat(Extensions.operAdd(indent, 4)));
            var out = compileStatement(tok, cl.code);
            cl = getOrCreateClass(currentClass);
            cl.code = Extensions.operAdd(out, "\n");
            lastIndent = indent;
            lastScope = scope;
        }
        var cl = getOrCreateClass(currentClass);
        while (LangUtil.isTruthy(Extensions.operGt(lastIndent, 0))) {
            cl.code = Extensions.operAdd(Extensions.operAdd(cl.code, " ".repeat(lastIndent)), "}\n");
            lastIndent = Extensions.operSub(lastIndent, (4));
        }
        return new CompResult(classes, startTemplate, endTemplate);
    }
    public static Class getOrCreateClass(String name) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(classes.containsKey(name)))) {
            classes.put(name, new Class(name));
        }
        return classes.get(currentClass);
    }
    public static String compileStatement(ArrayList<Token> tok, String out) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return ""; }
        var types = Tokeniser.getTokenTypes(tok);
        var startTok = Extensions.operGetIndex(types, 0);
        var endTok = Extensions.operGetIndex(types, Extensions.operUnarySub(1));
        var f = Extensions.operUnarySub(1);
        var f2 = Extensions.operUnarySub(1);
        if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.PACKAGE))) {
            packagePath = "";
            for (var t : LangUtil.asIterable(LangUtil.slice(tok, 1, null, 1))) { packagePath = Extensions.operAdd(packagePath, (t.value)); }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IMPORT))) {
            var importStr = "import ";
            for (var t : LangUtil.asIterable(LangUtil.slice(tok, 1, null, 1))) { importStr = Extensions.operAdd(importStr, (t.value)); }
            startTemplate = Extensions.operAdd(startTemplate, (Extensions.operAdd(importStr, ";\n")));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.OPTION))) {
            if (LangUtil.isTruthy(Extensions.operLt(Extensions.len(tok), 2))) {
                return "";
            }
            if (LangUtil.isTruthy(Extensions.operLt(Extensions.len(tok), 3))) {
                return "";
            }
            var optionName = Extensions.operGetIndex(tok, 1).value;
            var optionValue = Extensions.operGetIndex(tok, 2).value;
            Options.setOption(optionName, optionValue);
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ALIAS))) ? (Extensions.operGe(((f = findTokenType(tok, Token.Type.ASSIGN))), 2)) : (Extensions.operEq(startTok, Token.Type.ALIAS)))) {
            var name = Extensions.operGetIndex(tok, 1).value;
            var args = new ArrayList<String>();
            if (LangUtil.isTruthy(Extensions.operGt(f, 2))) {
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
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), Extensions.operUnarySub(1))))) ? (Extensions.operLt(Extensions.operAdd(f, 1), Extensions.len(tok))) : (!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.CLASS))), Extensions.operUnarySub(1)))))) {
            var cl = getOrCreateClass(currentClass);
            cl.code = out;
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
                cl.extendsType = compileExpr(LangUtil.slice(tok, 1, null, 1));
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "implements")) : (tok))) {
                cl.implementsType = compileExpr(LangUtil.slice(tok, 1, null, 1));
            }
            cl.access = access.accessMod;
            cl.typeArgs = typeArgs;
            defaultStatic = access.isStatic;
        }
        else if (LangUtil.isTruthy(Extensions.operIn(startTok, LangUtil.listOf(Token.Type.IF, Token.Type.ELIF, Token.Type.ELSE, Token.Type.UNLESS, Token.Type.WHILE, Token.Type.UNTIL)))) {
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.IF))) { out = Extensions.operAdd(out, ("if (")); }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELIF))) { out = Extensions.operAdd(out, ("else if (")); }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ELSE))) { out = Extensions.operAdd(out, ("else ")); }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.UNLESS))) { out = Extensions.operAdd(out, ("if (!(")); }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.WHILE))) { out = Extensions.operAdd(out, ("while (")); }
            if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.UNTIL))) { out = Extensions.operAdd(out, ("while (!(")); }
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(startTok, Token.Type.ELSE)))) {
                out = Extensions.operAdd(out, ("LangUtil.isTruthy("));
                out = Extensions.operAdd(out, (compileExpr(LangUtil.slice(tok, 1, null, 1))));
                out = Extensions.operAdd(out, ("))"));
                if (LangUtil.isTruthy(Extensions.operIn(startTok, LangUtil.listOf(Token.Type.UNTIL, Token.Type.UNLESS)))) { out = Extensions.operAdd(out, (")")); }
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.FOR))) {
            var f_in = findTokenRev(tok, "in");
            var varname = "__";
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(f_in, Extensions.operUnarySub(1))))) {
                varname = Extensions.operGetIndex(tok, 1).value;
            }
            else {
                f_in = 0;
            }
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("for (var ", varname), " : LangUtil.asIterable(")));
            out = Extensions.operAdd(out, (compileExpr(LangUtil.slice(tok, Extensions.operAdd(f_in, 1), null, 1))));
            out = Extensions.operAdd(out, (")) "));
            declareLocal(varname, Extensions.operAdd(scope, 1));
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy((Extensions.operLt(findTokenTypeRev(tok, Token.Type.ELSE), findTokenTypeRev(tok, Token.Type.IF))))) ? ((Extensions.operLt(findTokenTypeRev(tok, Token.Type.ELSE), findTokenTypeRev(tok, Token.Type.IF)))) : (!((boolean) Extensions.operEq(((f = findAnyTokenTypeRev(tok, LangUtil.listOf(Token.Type.UNLESS, Token.Type.WHILE, Token.Type.UNTIL, Token.Type.FOR)))), Extensions.operUnarySub(1))))))) {
            f = findAnyTokenTypeRev(tok, LangUtil.listOf(Token.Type.IF, Token.Type.UNLESS, Token.Type.WHILE, Token.Type.UNTIL, Token.Type.FOR));
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.IF))) {
                out = Extensions.operAdd(out, ("if (LangUtil.isTruthy("));
                var cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out = Extensions.operAdd(out, (LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond)));
                out = Extensions.operAdd(out, (")) { "));
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out = Extensions.operAdd(out, (" }"));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.UNLESS))) {
                out = Extensions.operAdd(out, ("if (!LangUtil.isTruthy("));
                var cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out = Extensions.operAdd(out, (LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond)));
                out = Extensions.operAdd(out, (")) { "));
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out = Extensions.operAdd(out, (" }"));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.WHILE))) {
                out = Extensions.operAdd(out, ("while (LangUtil.isTruthy("));
                var cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out = Extensions.operAdd(out, (LangUtil.isTruthy(cond.isEmpty()) ? ("true") : (cond)));
                out = Extensions.operAdd(out, (")) { "));
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out = Extensions.operAdd(out, (" }"));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.UNTIL))) {
                out = Extensions.operAdd(out, ("while (!LangUtil.isTruthy("));
                var cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out = Extensions.operAdd(out, (LangUtil.isTruthy(cond.isEmpty()) ? ("false") : (cond)));
                out = Extensions.operAdd(out, (")) { "));
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out = Extensions.operAdd(out, (" }"));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.FOR))) {
                var f_in = findTokenRev(tok, "in");
                var varname = "__";
                if (LangUtil.isTruthy(!((boolean) Extensions.operEq(f_in, Extensions.operUnarySub(1))))) {
                    varname = Extensions.operGetIndex(tok, Extensions.operAdd(f, 1)).value;
                }
                else {
                    f_in = f;
                }
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("for (var ", varname), " : LangUtil.asIterable(")));
                out = Extensions.operAdd(out, (compileExpr(LangUtil.slice(tok, Extensions.operAdd(f_in, 1), null, 1))));
                out = Extensions.operAdd(out, (")) { "));
                out = compileStatement(LangUtil.slice(tok, null, f, 1), out);
                out = Extensions.operAdd(out, (" }"));
                declareLocal(varname, Extensions.operAdd(scope, 1));
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.TRY))) {
            out = Extensions.operAdd(out, ("try "));
            var resources = compileExpr(LangUtil.slice(tok, 1, null, 1), false);
            if (LangUtil.isTruthy(resources)) { out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("(", resources), ") "))); }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.CATCH))) {
            out = Extensions.operAdd(out, ("catch ("));
            var s = "";
            for (var t : LangUtil.asIterable(LangUtil.slice(tok, 1, null, 1))) { s = Extensions.operAdd(s, (Extensions.operAdd(t.value, " "))); }
            s = s.trim();
            out = Extensions.operAdd(out, (Extensions.operAdd(s, ")")));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.THROW))) {
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("throw (", compileExpr(LangUtil.slice(tok, 1, null, 1))), ");")));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(Extensions.operLt(Extensions.len(tok), 2))) {
                return "";
            }
            out = Extensions.operAdd(out, (LangUtil.slice(Extensions.operGetIndex(tok, 1).value, 1, Extensions.operUnarySub(1), 1)));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGe(findTokenType(tok, Token.Type.ASSIGN), 2))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(indent))) ? (!LangUtil.isTruthy(scope)) : (!LangUtil.isTruthy(indent))) : (Extensions.operGe(findTokenType(tok, Token.Type.ASSIGN), 2)))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess.accessMod = AccessMod.PUBLIC;
            }
            var value = "";
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.ASSIGN))), Extensions.operUnarySub(1))))) {
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
            var varType = compileType(tok);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(varType))) {
                
            }
            declareLocal(name);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), varType), " "), name), value), ";")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(scope, 0))) ? (!((boolean) Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.SCOPE))), Extensions.operUnarySub(1)))) : (Extensions.operEq(scope, 0)))) {
            if (LangUtil.isTruthy(false)) {
                
            }
            else {
                if (LangUtil.isTruthy(Extensions.operEq(f, Extensions.operSub(Extensions.len(tok), 1)))) {
                    out = Extensions.operAdd(out, (compileMethodDef(tok)));
                }
                else {
                    out = Extensions.operAdd(out, (Extensions.operAdd(compileMethodDef(LangUtil.slice(tok, null, f, 1), false), " { ")));
                    ++ scope;
                    out = compileStatement(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1), out);
                    -- scope;
                    out = Extensions.operAdd(out, (" }"));
                }
            }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.LAMBDA))), Extensions.operUnarySub(1))))) {
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(compileMethodDef(LangUtil.slice(tok, null, f, 1), false), " { return ("), compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1))), "); }")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(scope, 0))) ? (Extensions.operEq(endTok, Token.Type.EXPR)) : (Extensions.operEq(scope, 0)))) {
            out = Extensions.operAdd(out, (compileMethodDef(tok)));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.STATIC)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out = Extensions.operAdd(out, ("static"));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "@")) : (Extensions.operGt(Extensions.len(tok), 1)))) {
            out = Extensions.operAdd(out, (Extensions.operAdd("@", compileExpr(LangUtil.slice(tok, 1, null, 1)))));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.RETURN))) {
            out = Extensions.operAdd(out, ("return"));
            if (LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(" ", compileExpr(LangUtil.slice(tok, 1, null, 1)))));
            }
            out = Extensions.operAdd(out, (";"));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.BREAK)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out = Extensions.operAdd(out, ("break;"));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.CONTINUE)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            out = Extensions.operAdd(out, ("continue;"));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(startTok, Token.Type.PASS)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.ID))) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "println")))) : (Extensions.operEq(startTok, Token.Type.ID))))) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "print")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "println")))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("LangUtil.", Extensions.operGetIndex(tok, 0).value), "(")));
                out = Extensions.operAdd(out, (compileCommaExpr(LangUtil.slice(tok, 1, null, 1))));
                out = Extensions.operAdd(out, (");"));
            }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(findTokenType(tok, Token.Type.ENUM), Extensions.operUnarySub(1))))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess.accessMod = AccessMod.PUBLIC;
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
            }
            else {
                
            }
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(methodAccess, " enum "), name)));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGe(Extensions.len(tok), 2))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(indent))) ? (!LangUtil.isTruthy(scope)) : (!LangUtil.isTruthy(indent))) : (Extensions.operGe(Extensions.len(tok), 2)))) {
            var methodAccess = getMethodAccess(tok);
            tok = stripMethodAccess(tok);
            if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
                methodAccess.accessMod = AccessMod.PUBLIC;
            }
            var name = "";
            if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.ID)) : (tok))) {
                name = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
                tok.remove(Extensions.operSub(Extensions.len(tok), 1));
            }
            else {
                
            }
            var varType = compileType(tok);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(varType))) {
                
            }
            declareLocal(name);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), varType), " "), name), ";")));
        }
        else {
            var ending = LangUtil.isTruthy(Extensions.operEq(endTok, Token.Type.COMMA)) ? ("") : (";");
            out = Extensions.operAdd(out, (Extensions.operAdd(compileExpr(tok, false), ending)));
        }
        return out;
    }
    public static String compileCommaExpr(ArrayList<Token> tok) {
        var out = "";
        var buffer = new ArrayList<Token>();
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.COMMA))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(compileExpr(buffer), ", ")));
                buffer.clear();
            }
            else {
                Extensions.operShl(buffer, t);
            }
        }
        out = Extensions.operAdd(out, (compileExpr(buffer)));
        while (LangUtil.isTruthy(out.endsWith(", "))) { out = LangUtil.slice(out, null, Extensions.operUnarySub(2), 1); }
        return out;
    }
    public static String compileCommaExpr(String expr) {
        return compileCommaExpr(Tokeniser.tokLine(expr));
    }
    public static String compileExpr(ArrayList<Token> tok, boolean nested) {
        var out = "";
        var f = Extensions.operUnarySub(1);
        var f2 = Extensions.operUnarySub(1);
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return out; }
        if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.INLINE))) {
            if (LangUtil.isTruthy(Extensions.operLt(Extensions.len(tok), 2))) {
                return "";
            }
            out = Extensions.operAdd(out, (LangUtil.slice(Extensions.operGetIndex(tok, 1).value, 1, Extensions.operUnarySub(1), 1)));
        }
        else if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operGe(((f = findTokenType(tok, Token.Type.ASSIGN))), 3))) ? ((LangUtil.isTruthy(!((boolean) Extensions.operEq(((f2 = findTokenType(tok, Token.Type.DOT))), Extensions.operUnarySub(1))))) ? (Extensions.operLt(f2, f)) : (!((boolean) Extensions.operEq(((f2 = findTokenType(tok, Token.Type.DOT))), Extensions.operUnarySub(1))))) : (Extensions.operGe(((f = findTokenType(tok, Token.Type.ASSIGN))), 3))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(lhs, " = "), rhs)));
        }
        else if (LangUtil.isTruthy(Extensions.operGe(((f = findTokenType(tok, Token.Type.ASSIGN))), 1))) {
            var name = Extensions.operGetIndex(tok, Extensions.operSub(f, 1)).value;
            var value = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            var varType = compileType(LangUtil.slice(tok, null, Extensions.operSub(f, 1), 1));
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operSub(f, 1)).type, Token.Type.SQUARE_EXPR))) {
                name = "";
                for (var t : LangUtil.asIterable(LangUtil.slice(tok, null, f, 1))) { name = Extensions.operAdd(name, (Extensions.operAdd(t.value, " "))); }
                name = name.trim();
                varType = "";
            }
            if (LangUtil.isTruthy(Extensions.operEq(varType, "let"))) { varType = "var"; }
            if (LangUtil.isTruthy(nested)) { out = Extensions.operAdd(out, ("(")); }
            if (LangUtil.isTruthy((LangUtil.isTruthy(!LangUtil.isTruthy(localInScope(name)))) ? ((LangUtil.isTruthy(!LangUtil.isTruthy(nested))) ? (!LangUtil.isTruthy(name.contains(" "))) : (!LangUtil.isTruthy(nested))) : (!LangUtil.isTruthy(localInScope(name))))) {
                if (LangUtil.isTruthy(!LangUtil.isTruthy(varType))) { varType = "var"; }
                declareLocal(name);
            }
            if (LangUtil.isTruthy(varType)) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(varType, " "), name), " = "), value)));
            }
            else {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(name, " = "), value)));
            }
            if (LangUtil.isTruthy(nested)) { out = Extensions.operAdd(out, (")")); }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.COMP_ASSIGN))), Extensions.operUnarySub(1))))) {
            var id = Token.joinTokens(LangUtil.slice(tok, null, f, 1));
            var oper = LangUtil.slice(Extensions.operGetIndex(tok, f).value, null, Extensions.operUnarySub(1), 1);
            var value = Token.joinTokens(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(oper, ".")))) {
                value = Extensions.operAdd(Extensions.operAdd("(", value), ")");
            }
            var expr = Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(id, " = "), id), " "), oper), " "), value);
            tok = Tokeniser.tokLine(expr);
            out = Extensions.operAdd(out, (compileExpr(tok, nested)));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.ARROW))), Extensions.operUnarySub(1))))) {
            var compArgs = "";
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR))) {
                var args = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, Extensions.operUnarySub(1), 1);
                compArgs = compileMethodArgs(Tokeniser.tokLine(args), true, null);
            }
            else {
                compArgs = Extensions.operGetIndex(tok, 0).value;
            }
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(compArgs, " -> "), compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1)))));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findToken(tok, "?"))), Extensions.operUnarySub(1))))) ? (!((boolean) Extensions.operEq(((f2 = findToken(tok, ":"))), Extensions.operUnarySub(1)))) : (!((boolean) Extensions.operEq(((f = findToken(tok, "?"))), Extensions.operUnarySub(1)))))) {
            var cond = compileExpr(LangUtil.slice(tok, null, f, 1));
            var lhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), f2, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f2, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.isTruthy(", cond), ") ? ("), lhs), ") : ("), rhs), ")")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findToken(tok, "if"))), Extensions.operUnarySub(1))))) ? (!((boolean) Extensions.operEq(((f2 = findToken(tok, "else"))), Extensions.operUnarySub(1)))) : (!((boolean) Extensions.operEq(((f = findToken(tok, "if"))), Extensions.operUnarySub(1)))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), f2, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f2, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.isTruthy(", cond), ") ? ("), lhs), ") : ("), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findToken(tok, "??"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("((", lhs), ") != null) ? ("), lhs), ") : ("), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findAnyToken(tok, LangUtil.listOf("||", "or")))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("(LangUtil.isTruthy(", lhs), ")) ? ("), lhs), ") : ("), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findToken(tok, "nor"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("!(LangUtil.isTruthy(", lhs), ") || LangUtil.isTruthy("), rhs), "))")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findToken(tok, "xor"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("(LangUtil.isTruthy(", lhs), ") != LangUtil.isTruthy("), rhs), "))")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findAnyToken(tok, LangUtil.listOf("&&", "and")))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("(LangUtil.isTruthy(", lhs), ")) ? ("), rhs), ") : ("), lhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenRev(tok, "|"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operOr(", lhs), ", "), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenRev(tok, "^"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operXor(", lhs), ", "), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenRev(tok, "&"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operAnd(", lhs), ", "), rhs), ")")));
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findAnyTokenRev(tok, LangUtil.listOf("==", "!=", "===", "!==")))), Extensions.operUnarySub(1))))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "=="))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operEq(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!="))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("!((boolean) Extensions.operEq(", lhs), ", "), rhs), "))")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "==="))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(lhs, " == "), rhs)));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "!=="))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(lhs, " != "), rhs)));
            }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findAnyToken(tok, LangUtil.listOf("<", ">", "<=", ">=", "as", "is", "isnt", "in")))), Extensions.operUnarySub(1))))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "in"))) ? ((LangUtil.isTruthy(Extensions.operGe(Extensions.operSub(f, 1), 0))) ? (Extensions.operIn(Extensions.operGetIndex(tok, Extensions.operSub(f, 1)).value, LangUtil.listOf("not", "!"))) : (Extensions.operGe(Extensions.operSub(f, 1), 0))) : (Extensions.operEq(oper, "in")))) {
                oper = "not in";
                tok.remove(f--);
            }
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "is"))) {
                oper = "==";
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "isnt"))) {
                oper = "!=";
            }
            if (LangUtil.isTruthy(Extensions.operIn(oper, LangUtil.listOf(">", ">=", "<", "<=")))) {
                var stack = new ArrayList<String>();
                while (LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) {
                    f = findAnyToken(tok, LangUtil.listOf(">", ">=", "<", "<="));
                    f2 = findAnyToken(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1), LangUtil.listOf(">", ">=", "<", "<="));
                    f2 = LangUtil.isTruthy(Extensions.operEq(f2, Extensions.operUnarySub(1))) ? (Extensions.len(tok)) : (Extensions.operAdd(Extensions.operAdd(f2, f), 1));
                    if (LangUtil.isTruthy(Extensions.operEq(f, Extensions.operUnarySub(1)))) { break; }
                    lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
                    oper = Extensions.operGetIndex(tok, f).value;
                    rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), f2, 1));
                    tok = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
                    var operName = "";
                    if (LangUtil.isTruthy(Extensions.operEq(oper, ">"))) {
                        operName = "Gt";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(oper, ">="))) {
                        operName = "Ge";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(oper, "<"))) {
                        operName = "Lt";
                    }
                    else if (LangUtil.isTruthy(Extensions.operEq(oper, "<="))) {
                        operName = "Le";
                    }
                    stack = Extensions.operAdd(stack, LangUtil.listOf(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.oper", operName), "("), lhs), ", "), rhs), ")")));
                }
                for (var elem : LangUtil.asIterable(stack)) { out = Extensions.operAdd(out, (Extensions.operAdd(elem, " && "))); }
                if (LangUtil.isTruthy(out.endsWith(" && "))) { out = LangUtil.slice(out, null, Extensions.operUnarySub(4), 1); }
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "in"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operIn(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "not in"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("!((boolean) Extensions.operIn(", lhs), ", "), rhs), "))")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "as"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("((", rhs), ") "), lhs), ")")));
            }
            else {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(lhs, " "), oper), " "), rhs)));
            }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findAnyTokenRev(tok, LangUtil.listOf("<<", ">>", "<<<", ">>>")))), Extensions.operUnarySub(1))))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "<<"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operShl(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, ">>"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operShr(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "<<<"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operUshl(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, ">>>"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operUshr(", lhs), ", "), rhs), ")")));
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(((f = findAnyTokenRev(tok, LangUtil.listOf("+", "-")))), 0))) ? (Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.BIN_OPER)) : (Extensions.operGt(((f = findAnyTokenRev(tok, LangUtil.listOf("+", "-")))), 0)))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "+"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operAdd(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "-"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operSub(", lhs), ", "), rhs), ")")));
            }
        }
        else if (LangUtil.isTruthy(Extensions.operGt(((f = findAnyTokenRev(tok, LangUtil.listOf("*", "/", "%")))), 0))) {
            var oper = Extensions.operGetIndex(tok, f).value;
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            if (LangUtil.isTruthy(Extensions.operEq(oper, "*"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operMul(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "/"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operDiv(", lhs), ", "), rhs), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "%"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operMod(", lhs), ", "), rhs), ")")));
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.UNARY_OPER))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.UNARY_OPER)) : (Extensions.operIn(Extensions.operGetIndex(tok, 0).value, LangUtil.listOf("+", "-"))))) : (Extensions.operGt(Extensions.len(tok), 1)))) {
            var oper = Extensions.operGetIndex(tok, 0).value;
            var expr = compileExpr(LangUtil.slice(tok, 1, null, 1));
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(oper, "not"))) ? (Extensions.operEq(oper, "not")) : (Extensions.operEq(oper, "!")))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("!LangUtil.isTruthy(", expr), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "~"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("Extensions.operBitNot(", expr), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "+"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("Extensions.operUnaryAdd(", expr), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(oper, "-"))) {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("Extensions.operUnarySub(", expr), ")")));
            }
            else {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operGetIndex(tok, 0).value, expr)));
            }
        }
        else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenRev(tok, "**"))), Extensions.operUnarySub(1))))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Math.pow(", lhs), ", "), rhs), ")")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.INCREMENT)) : (Extensions.operGt(Extensions.len(tok), 1)))) {
            var name = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(name, "++")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.DECREMENT)) : (Extensions.operGt(Extensions.len(tok), 1)))) {
            var name = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(name, "--")));
        }
        else if (LangUtil.isTruthy(Extensions.operGe(((f = findToken(tok, "extends"))), 1))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(lhs, " extends "), rhs)));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.NULL_CHECK))), Extensions.operUnarySub(1)))) : (Extensions.operGt(Extensions.len(tok), 1)))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            var tempVar = getNextTemp();
            if (LangUtil.isTruthy(rhs.startsWith("LangUtil.nullCheck"))) {
                rhs = LangUtil.slice(rhs, Extensions.len("LangUtil.nullCheck("), Extensions.operUnarySub(1), 1);
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.nullCheck(", lhs), ", "), tempVar), " -> LangUtil.nullCheck("), tempVar), "."), rhs), "))")));
            }
            else {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.nullCheck(", lhs), ", "), tempVar), " -> "), tempVar), "."), rhs), ")")));
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (((LangUtil.isTruthy(Extensions.operIn(Extensions.operGetIndex(tok, (f = Extensions.operSub(Extensions.len(tok), 1))).type, LangUtil.listOf(Token.Type.EXPR, Token.Type.SQUARE_EXPR)))) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operSub(f, 1)).type, Token.Type.COMMA))) : (Extensions.operIn(Extensions.operGetIndex(tok, (f = Extensions.operSub(Extensions.len(tok), 1))).type, LangUtil.listOf(Token.Type.EXPR, Token.Type.SQUARE_EXPR))))) : (Extensions.operGt(Extensions.len(tok), 1)))) ? ((LangUtil.isTruthy(Extensions.operGt(Extensions.len(tok), 1))) ? (((LangUtil.isTruthy(Extensions.operIn(Extensions.operGetIndex(tok, (f = Extensions.operSub(Extensions.len(tok), 1))).type, LangUtil.listOf(Token.Type.EXPR, Token.Type.SQUARE_EXPR)))) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operSub(f, 1)).type, Token.Type.COMMA))) : (Extensions.operIn(Extensions.operGetIndex(tok, (f = Extensions.operSub(Extensions.len(tok), 1))).type, LangUtil.listOf(Token.Type.EXPR, Token.Type.SQUARE_EXPR))))) : (Extensions.operGt(Extensions.len(tok), 1))) : (((LangUtil.isTruthy(Extensions.operGt(((f = findTokenTypeRev(tok, Token.Type.DOT))), 0))) ? (Extensions.operLt(f, Extensions.operSub(Extensions.len(tok), 1))) : (Extensions.operGt(((f = findTokenTypeRev(tok, Token.Type.DOT))), 0)))))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.EXPR))) {
                if (LangUtil.isTruthy(!((boolean) Extensions.operEq(f, Extensions.operSub(Extensions.len(tok), 1))))) {
                    return "";
                }
                var args = LangUtil.slice(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value, 1, Extensions.operUnarySub(1), 1);
                var compArgs = compileCommaExpr(args);
                tok = LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1);
                var typeArgs = "";
                if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.SQUARE_EXPR))) {
                    typeArgs = compileTypeArgs(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value);
                    tok = LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1);
                }
                var isNew = false;
                if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "new"))) {
                    isNew = true;
                    tok.remove(0);
                }
                var name = compileExpr(tok);
                var isClassName = StringParser.isPascalCase(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value);
                if (LangUtil.isTruthy(Extensions.operIn(name, LangUtil.listOf("len")))) {
                    name = Extensions.operAdd("Extensions.", name);
                }
                else if (LangUtil.isTruthy(Extensions.operIn(name, LangUtil.listOf("round", "roundstr", "sum", "min", "max", "exit")))) {
                    name = Extensions.operAdd("LangUtil.", name);
                }
                if (LangUtil.isTruthy((LangUtil.isTruthy(isClassName)) ? (isClassName) : (isNew))) { name = Extensions.operAdd("new ", name); }
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(name, typeArgs), "("), StringParser.trimComma(compArgs)), ")")));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.DOT))) {
                var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
                var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(lhs, "."), rhs)));
            }
            else if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, f).type, Token.Type.SQUARE_EXPR))) {
                var iterable = compileExpr(LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1));
                var expr = LangUtil.slice(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value, 1, Extensions.operUnarySub(1), 1);
                tok = Tokeniser.tokLine(expr);
                if (LangUtil.isTruthy(!((boolean) Extensions.operEq(findToken(tok, ":"), Extensions.operUnarySub(1))))) {
                    var startTokens = new ArrayList<Token>();
                    var endTokens = new ArrayList<Token>();
                    var stepTokens = new ArrayList<Token>();
                    while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ":"))) : (tok))) {
                        startTokens.add(Extensions.operGetIndex(tok, 0));
                        tok.remove(0);
                    }
                    tok.remove(0);
                    while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ":"))) : (tok))) {
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
                    out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("LangUtil.slice(", iterable), ", "), start), ", "), end), ", "), step), ")")));
                }
                else {
                    out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.operGetIndex(", iterable), ", "), compileExpr(tok)), ")")));
                }
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = Extensions.operGetIndex(tok, 0).value;
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("(", compileCommaExpr(Tokeniser.tokLine(expr))), ")")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.SQUARE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, Extensions.operUnarySub(1), 1);
            tok = Tokeniser.tokLine(expr);
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenType(tok, Token.Type.FOR))), Extensions.operUnarySub(1))))) {
                expr = compileExpr(LangUtil.slice(tok, null, f, 1));
                tok = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
                var cond = "";
                if (LangUtil.isTruthy(!((boolean) Extensions.operEq(((f = findTokenTypeRev(tok, Token.Type.IF))), Extensions.operUnarySub(1))))) {
                    cond = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
                    tok = LangUtil.slice(tok, null, f, 1);
                }
                var varname = "__";
                if (LangUtil.isTruthy(Extensions.operGe(((f = findTokenRev(tok, "in"))), 1))) {
                    varname = compileExpr(LangUtil.slice(tok, null, f, 1));
                    tok = LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1);
                }
                var iterable = compileExpr(tok);
                if (LangUtil.isTruthy(!LangUtil.isTruthy(expr))) { expr = varname; }
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("LangUtil.arrstream(LangUtil.asIterable(", iterable), "))")));
                if (LangUtil.isTruthy(cond)) { out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(".filter(", varname), " -> LangUtil.isTruthy("), cond), "))"))); }
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(".map(", varname), " -> ("), expr), "))")));
                out = Extensions.operAdd(out, (".toList()"));
            }
            else if (LangUtil.isTruthy(!((boolean) Extensions.operEq(findTokenType(tok, Token.Type.RANGE), Extensions.operUnarySub(1))))) {
                out = Extensions.operAdd(out, (compileRange(tok)));
            }
            else {
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("LangUtil.listOf(", compileCommaExpr(tok)), ")")));
            }
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.BRACE_EXPR)) : (Extensions.operEq(Extensions.len(tok), 1)))) {
            var expr = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, Extensions.operUnarySub(1), 1);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("{", compileCommaExpr(Tokeniser.tokLine(expr))), "}")));
        }
        else if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGe(Extensions.len(tok), 3))) ? (Extensions.operGe(((f = findTokenTypeRev(tok, Token.Type.SYMBOL))), 1)) : (Extensions.operGe(Extensions.len(tok), 3)))) {
            var lhs = compileExpr(LangUtil.slice(tok, null, f, 1));
            var rhs = compileExpr(LangUtil.slice(tok, Extensions.operAdd(f, 1), null, 1));
            var funcName = Parser.convertOperToID(Extensions.operGetIndex(tok, f).value);
            out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("Extensions.", funcName), "("), lhs), ", "), rhs), ")")));
        }
        else {
            for (var j : LangUtil.asIterable(Extensions.len(tok))) {
                var t = Extensions.operGetIndex(tok, j);
                if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.EXPR))) {
                    var raw = LangUtil.slice(t.value, 1, Extensions.operUnarySub(1), 1);
                    var tokens = Tokeniser.tokLine(raw);
                    out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd("(", compileCommaExpr(tokens)), ")")));
                }
                else {
                    out = Extensions.operAdd(out, (Extensions.operAdd(t.value, " ")));
                }
            }
        }
        return out.trim();
    }
    public static String compileExpr(ArrayList<Token> tok) {
        return compileExpr(tok, true);
    }
    public static String compileType(ArrayList<Token> tok) {
        var typeArgs = "";
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.SQUARE_EXPR)) : (tok))) {
            typeArgs = compileTypeArgs(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value);
            tok = LangUtil.slice(tok, null, Extensions.operUnarySub(1), 1);
        }
        var varType = "";
        for (var t : LangUtil.asIterable(tok)) { varType = Extensions.operAdd(varType, (Extensions.operAdd(t.value, " "))); }
        return Extensions.operAdd(varType.trim(), typeArgs);
    }
    public static String compileMethodDef(ArrayList<Token> tok, boolean declareLocals) {
        var out = "";
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (((LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "override"))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "override")) : (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, "!")))) : (tok))) {
            out = Extensions.operAdd(out, ("@Override "));
            tok.remove(0);
        }
        var methodAccess = getMethodAccess(tok);
        tok = stripMethodAccess(tok);
        if (LangUtil.isTruthy(Extensions.operEq(methodAccess.accessMod, AccessMod.NONE))) {
            methodAccess.accessMod = AccessMod.PUBLIC;
        }
        var typeArgs = "";
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.SQUARE_EXPR)) : (tok))) {
            typeArgs = Extensions.operAdd(compileTypeArgs(Extensions.operGetIndex(tok, 0).value), " ");
            tok.remove(0);
        }
        var thrown = "";
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.EXPR)) : (tok))) {
            var v = LangUtil.slice(Extensions.operGetIndex(tok, 0).value, 1, Extensions.operUnarySub(1), 1);
            thrown = compileExpr(Tokeniser.tokLine(v));
            tok.remove(0);
        }
        if (LangUtil.isTruthy(thrown)) { thrown = Extensions.operAdd(" throws ", thrown); }
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.SCOPE)) : (tok))) {
            tok.remove(Extensions.operSub(Extensions.len(tok), 1));
        }
        var args = "()";
        if (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.EXPR)) : (tok))) {
            var v = LangUtil.slice(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value, 1, Extensions.operUnarySub(1), 1);
            args = compileMethodArgs(Tokeniser.tokLine(v), declareLocals);
            tok.remove(Extensions.operSub(Extensions.len(tok), 1));
        }
        var methodName = "";
        if (LangUtil.isTruthy(tok)) {
            methodName = Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).value;
            if (LangUtil.isTruthy(!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, Extensions.operUnarySub(1)).type, Token.Type.ID)))) {
                methodName = Parser.convertOperToID(methodName);
            }
            tok.remove(Extensions.operSub(Extensions.len(tok), 1));
        }
        else {
            return "";
        }
        var returnType = compileType(tok);
        if (LangUtil.isTruthy(!LangUtil.isTruthy(returnType))) { returnType = "void"; }
        if (LangUtil.isTruthy(Extensions.operEq(methodName, "main"))) {
            args = "(String[] args)";
            methodAccess.isStatic = true;
        }
        if (LangUtil.isTruthy(Extensions.operEq(methodName, currentClass))) {
            return Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), typeArgs), methodName), args), thrown);
        }
        return Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(methodAccess, " "), typeArgs), returnType), " "), methodName), args), thrown);
    }
    public static String compileMethodDef(ArrayList<Token> tok) {
        return compileMethodDef(tok, true);
    }
    public static String compileMethodArgs(ArrayList<Token> tok, boolean declareLocals, String defaultType) {
        var out = "";
        var buffer = new ArrayList<Token>();
        tok.add(Token.fromString(","));
        ++ scope;
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.COMMA))) {
                if (LangUtil.isTruthy(Extensions.operEq(Extensions.len(buffer), 1))) {
                    if (LangUtil.isTruthy(declareLocals)) { declareLocal(Extensions.operGetIndex(buffer, 0).value); }
                    if (LangUtil.isTruthy(defaultType)) { out = Extensions.operAdd(out, (Extensions.operAdd(defaultType, " "))); }
                    out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operGetIndex(buffer, 0).value, ", ")));
                }
                else if (LangUtil.isTruthy(Extensions.operGt(Extensions.len(buffer), 1))) {
                    var name = Extensions.operGetIndex(buffer, Extensions.operUnarySub(1)).value;
                    var argType = compileType(LangUtil.slice(buffer, null, Extensions.operUnarySub(1), 1));
                    if (LangUtil.isTruthy(declareLocals)) { declareLocal(name); }
                    out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(argType, " "), name), ", ")));
                }
                buffer.clear();
            }
            else {
                buffer.add(t);
            }
        }
        -- scope;
        return Extensions.operAdd(Extensions.operAdd("(", StringParser.trimComma(out)), ")");
    }
    public static String compileMethodArgs(ArrayList<Token> tok, boolean declareLocals) {
        return compileMethodArgs(tok, declareLocals, "Object");
    }
    public static String compileMethodArgs(ArrayList<Token> tok) {
        return compileMethodArgs(tok, false, "Object");
    }
    public static String compileTypeArgs(ArrayList<Token> tok) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return "[]"; }
        if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(Extensions.len(tok), 1))) ? (Extensions.operEq(Extensions.operGetIndex(tok, 0).value, ".")) : (Extensions.operEq(Extensions.len(tok), 1)))) { return "<>"; }
        var out = "";
        var buffer = new ArrayList<Token>();
        tok.add(Token.fromString(","));
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.COMMA))) ? (buffer) : (Extensions.operEq(t.type, Token.Type.COMMA)))) {
                var typeArgs = "";
                if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operGt(Extensions.len(buffer), 1))) ? (Extensions.operEq(Extensions.operGetIndex(buffer, Extensions.operUnarySub(1)).type, Token.Type.SQUARE_EXPR)) : (Extensions.operGt(Extensions.len(buffer), 1)))) {
                    typeArgs = compileTypeArgs(Extensions.operGetIndex(buffer, Extensions.operUnarySub(1)).value);
                    buffer = LangUtil.slice(buffer, null, Extensions.operUnarySub(1), 1);
                }
                out = Extensions.operAdd(out, (Extensions.operAdd(Extensions.operAdd(compileExpr(buffer), typeArgs), ", ")));
                buffer.clear();
            }
            else {
                buffer.add(t);
            }
        }
        return Extensions.operAdd(Extensions.operAdd("<", StringParser.trimComma(out)), ">");
    }
    public static String compileTypeArgs(String expr) {
        if (LangUtil.isTruthy((LangUtil.isTruthy(expr.startsWith("["))) ? (expr.endsWith("]")) : (expr.startsWith("[")))) {
            expr = LangUtil.slice(expr, 1, Extensions.operUnarySub(1), 1);
        }
        return compileTypeArgs(Tokeniser.tokLine(expr));
    }
    public static String compileRange(ArrayList<Token> tok) {
        var startTokens = new ArrayList<Token>();
        var endTokens = new ArrayList<Token>();
        var stepTokens = new ArrayList<Token>();
        while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.RANGE))) : (tok))) {
            startTokens.add(Extensions.operGetIndex(tok, 0));
            tok.remove(0);
        }
        tok.remove(0);
        while (LangUtil.isTruthy((LangUtil.isTruthy(tok)) ? (!((boolean) Extensions.operEq(Extensions.operGetIndex(tok, 0).type, Token.Type.RANGE))) : (tok))) {
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
    public static int findToken(ArrayList<Token> tok, String value) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenRev(ArrayList<Token> tok, String value) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).value, value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyToken(ArrayList<Token> tok, List<String> values) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenRev(ArrayList<Token> tok, List<String> values) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(values.contains(Extensions.operGetIndex(tok, i).value))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenType(ArrayList<Token> tok, Token . Type type) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findTokenTypeRev(ArrayList<Token> tok, Token . Type type) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, i).type, type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenType(ArrayList<Token> tok, List<Token.Type> types) {
        for (var i : LangUtil.asIterable(Extensions.len(tok))) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static int findAnyTokenTypeRev(ArrayList<Token> tok, List<Token.Type> types) {
        for (int i = tok.size() - 1; i >= 0; --i) {
            if (LangUtil.isTruthy(types.contains(Extensions.operGetIndex(tok, i).type))) { return i; }
        }
        return Extensions.operUnarySub(1);
    }
    public static MethodAccess getMethodAccess(ArrayList<Token> tok, int end) {
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
    public static MethodAccess getMethodAccess(ArrayList<Token> tok) {
        return getMethodAccess(tok, Extensions.len(tok));
    }
    public static ArrayList<Token> stripMethodAccess(ArrayList<Token> tok, int end) {
        for (var i : LangUtil.asIterable(end)) {
            var t = Extensions.operGetIndex(tok, i);
            var v = t.value;
            if (LangUtil.isTruthy(((LangUtil.isTruthy(Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))) ? ((LangUtil.isTruthy(!((boolean) Extensions.operEq(v, "static")))) ? ((LangUtil.isTruthy(!((boolean) Extensions.operEq(v, "#")))) ? ((LangUtil.isTruthy(!((boolean) Extensions.operEq(v, "instance")))) ? (!((boolean) Extensions.operEq(v, "^"))) : (!((boolean) Extensions.operEq(v, "instance")))) : (!((boolean) Extensions.operEq(v, "#")))) : (!((boolean) Extensions.operEq(v, "static")))) : (Extensions.operEq(MethodAccess.accessModFromToken(t), AccessMod.NONE))))) {
                return LangUtil.slice(tok, i, null, 1);
            }
        }
        return tok;
    }
    public static ArrayList<Token> stripMethodAccess(ArrayList<Token> tok) {
        return stripMethodAccess(tok, Extensions.len(tok));
    }
    public static void declareLocal(String name, int scope) {
        locals.put(name, scope);
    }
    public static void declareLocal(String name) {
        declareLocal(name, scope);
    }
    public static boolean localInScope(String name) {
        return Extensions.operGe(scope, locals.getOrDefault(name, Extensions.operAdd(scope, 1)));
    }
    public static void cleanLocals(int scope) {
        var toRemove = new ArrayList<String>();
        for (var name : LangUtil.asIterable(locals.keySet())) {
            if (LangUtil.isTruthy(Extensions.operGt(locals.get(name), scope))) { toRemove.add(name); }
        }
        for (var name : LangUtil.asIterable(toRemove)) { locals.remove(name); }
    }
}

