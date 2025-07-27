import java.io.*;
import java.util.*;

public class Parser {
    public static ArrayList < Token > convertIdentifiers(ArrayList < Token > tok) {
        var lastWasDot = false;
        for (int i = 0; i < tok.size(); ++i) {
            var t = tok.get(i);
            var nextTok = i + 1 < tok.size ()? tok.get (i + 1): new Token(Token.Type.BLANK , "");
            System.out.println(i + ": " + tok);
            if (LangUtil.isTruthy((LangUtil.isTruthy(t.type == Token.Type.ID)) ? (!LangUtil.isTruthy(lastWasDot)) : (t.type == Token.Type.ID))) {
                if (LangUtil.isTruthy(t.value.equals("string"))) {
                    t . value = "String";
                }
                else if (LangUtil.isTruthy(t.value.equals("String"))) {
                    t . value = "string";
                }
                else if (LangUtil.isTruthy(t.value.equals("object"))) {
                    t . value = "Object";
                }
                else if (LangUtil.isTruthy(t.value.equals("Object"))) {
                    t . value = "object";
                }
                else if (LangUtil.isTruthy(t.value.equals("byte"))) {
                    t . value = "Byte";
                }
                else if (LangUtil.isTruthy(t.value.equals("Byte"))) {
                    t . value = "byte";
                }
                else if (LangUtil.isTruthy(t.value.equals("short"))) {
                    t . value = "Short";
                }
                else if (LangUtil.isTruthy(t.value.equals("Short"))) {
                    t . value = "short";
                }
                else if (LangUtil.isTruthy(t.value.equals("int"))) {
                    t . value = "Integer";
                }
                else if (LangUtil.isTruthy(t.value.equals("Integer"))) {
                    t . value = "int";
                }
                else if (LangUtil.isTruthy(t.value.equals("long"))) {
                    t . value = "Long";
                }
                else if (LangUtil.isTruthy(t.value.equals("Long"))) {
                    t . value = "long";
                }
                else if (LangUtil.isTruthy(t.value.equals("float"))) {
                    t . value = "Float";
                }
                else if (LangUtil.isTruthy(t.value.equals("Float"))) {
                    t . value = "float";
                }
                else if (LangUtil.isTruthy(t.value.equals("double"))) {
                    t . value = "Double";
                }
                else if (LangUtil.isTruthy(t.value.equals("Double"))) {
                    t . value = "double";
                }
                else if (LangUtil.isTruthy(t.value.equals("char"))) {
                    t . value = "Character";
                }
                else if (LangUtil.isTruthy(t.value.equals("Character"))) {
                    t . value = "char";
                }
                else if (LangUtil.isTruthy(t.value.equals("bool"))) {
                    t . value = "Boolean";
                }
                else if (LangUtil.isTruthy(t.value.equals("Boolean"))) {
                    t . value = "bool";
                }
                else if (LangUtil.isTruthy(t.value.equals("boolean"))) {
                    t . value = "_boolean";
                }
                tok.set(i , t);
                if (LangUtil.isTruthy(Compiler.aliases.containsKey(t.value))) {
                    var alias = Compiler.aliases.get(t.value);
                    var aliasTokens = alias.tokens;
                    var hasArgs = (LangUtil.isTruthy(!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)))) ? (nextTok.type == Token.Type.EXPR) : (!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)));
                    tok.remove(i);
                    if (LangUtil.isTruthy(hasArgs)) {
                        System.out.println("so this shouldn't go");
                        var argTokens = Tokeniser.extractCommaExpr(nextTok.value);
                        aliasTokens = replaceIdentifiers(aliasTokens , alias.args , argTokens);
                        tok.remove(i);
                    }
                    tok.addAll(i , aliasTokens);
                    i += aliasTokens.size() - 1;
                }
            }
            lastWasDot = false;
            if (LangUtil.isTruthy(t.type == Token.Type.DOT)) {
                lastWasDot = true;
            }
        }
        return tok;
    }
    public static ArrayList < Token > replaceIdentifiers(ArrayList < Token > tok , ArrayList < String > names , ArrayList < ArrayList < Token > > values) {
        var lastWasDot = false;
        var newTok = new ArrayList < Token > ();
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(t.type == Token.Type.ID)) ? (!LangUtil.isTruthy(lastWasDot)) : (t.type == Token.Type.ID))) {
                var i = names.indexOf(t.value);
                if (LangUtil.isTruthy(i != - 1)) {
                    newTok.addAll(values.get(i));
                    continue;
                }
            }
            newTok.add(t);
        }
        return newTok;
    }
    public static ArrayList < Token > parseAccessMods(ArrayList < Token > tok) {
        var accessMod = AccessMod.DEFAULT;
        var isStatic = false;
        while (LangUtil.isTruthy(tok)) {
            var v = tok.get(0).value;
            if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("+"))) ? (v.equals("+")) : (v.equals("public")))) {
                accessMod = AccessMod.PUBLIC;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("-"))) ? (v.equals("-")) : (v.equals("private")))) {
                accessMod = AccessMod.PRIVATE;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("*"))) ? (v.equals("*")) : (v.equals("protected")))) {
                accessMod = AccessMod.PROTECTED;
            }
            else if (LangUtil.isTruthy((LangUtil.isTruthy(v.equals("#"))) ? (v.equals("#")) : (v.equals("static")))) {
                isStatic = true;
            }
            else {
                break;
            }
            tok.remove(0);
        }
        System.out.println(tok);
        var newTok = new ArrayList < Token > ();
        newTok.add(new Token(Token.Type.ACCESS_MOD ,("" + accessMod).toLowerCase()));
        if (LangUtil.isTruthy(isStatic)) { newTok.add(new Token(Token.Type.STATIC , "static")); }
        for (var t : LangUtil.asIterable(tok)) { newTok.add(t); }
        return newTok;
    }
    public static ArrayList < Token > parseDeclaration(ArrayList < Token > tok) {
        System.out.println(tok);
        return null;
    }
}

