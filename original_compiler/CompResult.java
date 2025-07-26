import java.util.HashMap;

public class CompResult {
    public HashMap<String, String> classes;
    public HashMap<String, AccessMod> classAccess;
    public String startTemplate;
    public String endTemplate;

    public CompResult(
        HashMap<String, String> classes,
        HashMap<String, AccessMod> classAccess,
        String startTemplate,
        String endTemplate) {
        this.classes = classes;
        this.classAccess = classAccess;
        this.startTemplate = startTemplate;
        this.endTemplate = endTemplate;
    }

    public String constructClassString(String className) {
        String code = classes.getOrDefault(className, "");
        AccessMod accessMod = classAccess.getOrDefault(className, AccessMod.DEFAULT);

        if (code.isBlank())
            return "";

        String accessModStr = MethodAccess.accessModToString(accessMod);
        String separator = (accessModStr.length() > 0 ? " " : "");

        String out = accessModStr + separator + "class " + className + " {\n";
        out += "    " + code.trim();
        out += "\n}\n";

        return out;
    }

    public String getCompiledCode(String mainClassName) {
        // Construct classes
        String out = "";

        // File class goes first
        if (classes.containsKey(mainClassName))
            out += constructClassString(mainClassName);

        // Construct other classes
        for (String c : classes.keySet()) {
            // The null class is used to write code without a class
            if (c.equals("null"))
                out += classes.get(c);
            else if (!c.equals(mainClassName))
                out += constructClassString(c);
        }

        return startTemplate + "\n" + out + endTemplate;
    }
}
