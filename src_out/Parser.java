import java.io.*;
import java.util.*;

public class Parser {
    public static ArrayList < Token > convertIdentifiers(ArrayList < Token > tok) {
        var lastWasDot = false;
        for (var i : LangUtil.asIterable(tok.size())) {
            var t = tok.get(i);
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
                for (var k : LangUtil.asIterable(Compiler.aliases.keySet())) {
                    System.out.println(k);
                    System.out.println(Compiler.aliases.get(k).tokens);
                }
                if (LangUtil.isTruthy(Compiler.aliases.containsKey(t.value))) {
                    var alias = Compiler.aliases.get(t.value);
                    tok.remove(i);
                    tok.addAll(i , alias.tokens);
                }
            }
            lastWasDot = false;
            if (LangUtil.isTruthy(t.type == Token.Type.DOT)) {
                lastWasDot = true;
            }
        }
        return tok;
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

