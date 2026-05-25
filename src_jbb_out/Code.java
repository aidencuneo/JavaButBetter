import java.io.*;
import java.util.*;
import java.util.regex.*;

class Code {
    public static void main(String[] args) {
        RegexRule r = new RegexRule("a", "b", "pre");
        LangUtil.println(r.find("a"));
        LangUtil.println(r.apply("a"));
    }
}
class RegexRule {
    public String find = "";
    public String replace = "";
    public String stage = "pre";
    public Pattern pattern = null;
    public RegexRule(String find , String replace , String stage) {
        this . find = find;
        this . replace = replace;
        this . stage = stage;
        this.pattern = Pattern.compile(find);
    }
    public boolean find(String s) {
        return pattern.matcher(s).find();
    }
    public String apply(String s) {
        return Dynamic.call("operAdd", pattern.matcher(s).replaceAll(replace), "...");
    }
}

