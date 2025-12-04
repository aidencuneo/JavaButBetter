import java.io.*;
import java.util.*;

public class Precompiler {
    public static List < RegexRule > regexRules = new ArrayList < > ();
    public static void precompileFile(String className , String code) {
        var lines = Tokeniser.splitFile(code);
        for (var i : LangUtil.asIterable(Extensions.len(lines))) {
            var tok = Tokeniser.tokLine(Extensions.operGetIndex(lines, i), true);
            if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { continue; }
            tok.remove(0);
            if (LangUtil.isTruthy(tok)) { precompileStatement(tok); }
        }
    }
    public static void precompileStatement(ArrayList < Token > tok) {
        if (LangUtil.isTruthy(!LangUtil.isTruthy(tok))) { return; }
        var types = Tokeniser.getTokenTypes(tok);
        var startTok = Extensions.operGetIndex(types, 0);
        var endTok = Extensions.operGetIndex(types, Extensions.operUnarySub(1));
        var f = Extensions.operUnarySub(1);
        var f2 = Extensions.operUnarySub(1);
        if (LangUtil.isTruthy(Extensions.operEq(startTok, Token.Type.REGEX))) {
            if (LangUtil.isTruthy(Extensions.len(tok) < 2)) {
                return;
            }
            var stage = "pre";
            if (LangUtil.isTruthy(Extensions.operEq(Extensions.operGetIndex(tok, 1).type, Token.Type.ID))) {
                stage = Extensions.operGetIndex(tok, 1).value;
            }
            if (LangUtil.isTruthy(Extensions.operEq(((f = Compiler.findTokenType(tok, Token.Type.EXPR))), Extensions.operUnarySub(1)))) {
                return;
            }
            var pair = LangUtil.slice(Extensions.operGetIndex(tok, f).value, 1, Extensions.operUnarySub(1), 1);
            var pairTok = Tokeniser.tokLine(pair);
            LangUtil.println(pairTok);
            if (LangUtil.isTruthy(Extensions.len(pairTok) < 2)) {
                return;
            }
            var find = LangUtil.slice(Extensions.operGetIndex(pairTok, 0).value, 1, Extensions.operUnarySub(1), 1);
            var replace = LangUtil.slice(Extensions.operGetIndex(pairTok, 1).value, 1, Extensions.operUnarySub(1), 1);
            regexRules.add(new RegexRule(find, replace, stage));
        }
    }
    public static String applyRegexRules(String code , int attempts) {
        for (var rule : LangUtil.asIterable(regexRules)) {
            for (var i : LangUtil.asIterable(LangUtil.range(0, attempts, null))) {
                if (LangUtil.isTruthy(!LangUtil.isTruthy(rule.find(code)))) { break; }
                code = rule.apply(code);
            }
        }
        return code;
    }
    public static String applyRegexRules(String code) {
        return applyRegexRules(code, 10000);
    }
}

