import java.io.*;
import java.util.*;

public class Parser {
    public static ArrayList < Token > convertIdentifiers(ArrayList < Token > tok) {
        var lastWasDot = false;
        for (int i = 0; i < tok.size(); ++i) {
            var t = tok.get(i);
            var nextTok = LangUtil.isTruthy(Extensions.operAdd(i, 1) < tok.size()) ? (tok.get(Extensions.operAdd(i, 1))) : (new Token());
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.ID))) ? (!LangUtil.isTruthy(lastWasDot)) : (Extensions.operEq(t.type, Token.Type.ID)))) {
                if (LangUtil.isTruthy(Extensions.operEq(t.value, "string"))) {
                    t . value = "String";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "String"))) {
                    t . value = "string";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "object"))) {
                    t . value = "Object";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Object"))) {
                    t . value = "object";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Int"))) {
                    t . value = "Integer";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Integer"))) {
                    t . value = "Int";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Char"))) {
                    t . value = "Character";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Character"))) {
                    t . value = "Char";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "bool"))) {
                    t . value = "boolean";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "boolean"))) {
                    t . value = "_boolean";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Boolean"))) {
                    t . value = "Bool";
                }
                else if (LangUtil.isTruthy(Extensions.operEq(t.value, "Bool"))) {
                    t . value = "Boolean";
                }
                tok.set(i, t);
                if (LangUtil.isTruthy(Compiler.aliases.containsKey(t.value))) {
                    var alias = Compiler.aliases.get(t.value);
                    var aliasTokens = alias.tokens;
                    var hasArgs = (LangUtil.isTruthy(!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)))) ? (Extensions.operEq(nextTok.type, Token.Type.EXPR)) : (!LangUtil.isTruthy(!LangUtil.isTruthy(alias.args)));
                    tok.remove(i);
                    if (LangUtil.isTruthy(hasArgs)) {
                        var argTokens = Tokeniser.extractCommaExpr(nextTok.value);
                        aliasTokens = replaceIdentifiers(aliasTokens, alias.args, argTokens);
                        tok.remove(i);
                    }
                    tok.addAll(i, aliasTokens);
                    i += Extensions.operSub(aliasTokens.size(), 1);
                }
            }
            lastWasDot = false;
            if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.DOT))) {
                lastWasDot = true;
            }
        }
        return tok;
    }
    public static ArrayList < Token > replaceIdentifiers(ArrayList < Token > tok , ArrayList < String > names , ArrayList < ArrayList < Token > > values) {
        var lastWasDot = false;
        var newTok = new ArrayList < Token > ();
        for (var t : LangUtil.asIterable(tok)) {
            if (LangUtil.isTruthy((LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.ID))) ? (!LangUtil.isTruthy(lastWasDot)) : (Extensions.operEq(t.type, Token.Type.ID)))) {
                var i = names.indexOf(t.value);
                if (LangUtil.isTruthy(!Extensions.operEq(i, Extensions.operUnarySub(1)))) {
                    newTok.addAll(values.get(i));
                    continue;
                }
            }
            else if (LangUtil.isTruthy(Extensions.operEq(t.type, Token.Type.EXPR))) {
                var exprStr = t.value.substring(1, Extensions.operSub(t.value.length(), 1));
                var replaced = replaceIdentifiers(Tokeniser.tokLine(exprStr), names, values);
                var newExpr = "";
                for (var token : LangUtil.asIterable(replaced)) { newExpr += token.value; }
                t . value = Extensions.operAdd(Extensions.operAdd("(", newExpr), ")");
            }
            newTok.add(t);
        }
        return newTok;
    }
}

