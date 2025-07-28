import java.io.*;
import java.util.*;

public class Parser {
    public static ArrayList < Token > convertIdentifiers(ArrayList < Token > tok) {
        var lastWasDot = false;
        for (int i = 0; i < tok.size(); ++i) {
            var t = tok.get(i);
            var nextTok = i + 1 < tok.size ()? tok.get (i + 1): new Token();
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
                else if (LangUtil.isTruthy(t.value.equals("Int"))) {
                    t . value = "Integer";
                }
                else if (LangUtil.isTruthy(t.value.equals("Integer"))) {
                    t . value = "Int";
                }
                else if (LangUtil.isTruthy(t.value.equals("Char"))) {
                    t . value = "Character";
                }
                else if (LangUtil.isTruthy(t.value.equals("Character"))) {
                    t . value = "Char";
                }
                else if (LangUtil.isTruthy(t.value.equals("bool"))) {
                    t . value = "boolean";
                }
                else if (LangUtil.isTruthy(t.value.equals("boolean"))) {
                    t . value = "_boolean";
                }
                else if (LangUtil.isTruthy(t.value.equals("Boolean"))) {
                    t . value = "Bool";
                }
                else if (LangUtil.isTruthy(t.value.equals("Bool"))) {
                    t . value = "Boolean";
                }
                tok.set(i , t);
                if (LangUtil.isTruthy(Compiler.aliases.containsKey(t.value))) {
                    var alias = Compiler.aliases.get(t.value);
                    var aliasTokens = alias.tokens;
                    var hasArgs = (LangUtil.isTruthy(!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)))) ? (nextTok.type == Token.Type.EXPR) : (!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)));
                    tok.remove(i);
                    if (LangUtil.isTruthy(hasArgs)) {
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
}

