import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RegexRule {
    public String find = "";
    public String replace = "";
    public String stage = "jbb";
    public Pattern pattern = null;
    public RegexRule(String find, String replace, String stage) {
        this.find = find;
        this.replace = replace;
        this.stage = stage;
        this.pattern = Pattern.compile(find);
    }
    public boolean find(String s) {
        return pattern.matcher(s).find();
    }
    public String apply(String s) {
        try {
            return pattern.matcher(s).replaceAll(replace);
        }
        catch (Exception e) {
            return s;
        }
    }
}

