public class Options {
    public static String defaultDecimal;

    public static void reset() {
        defaultDecimal = "f";
    }

    static {
        reset();
    }

    public static void setOption(String name, String value) {
        name = name.toLowerCase();
        String valueLower = value.toLowerCase();

        if (name.equals("decimal") || name.equals("default_decimal")) {
            if (valueLower.equals("float") || valueLower.equals("f"))
                defaultDecimal = "f";
            else if (valueLower.equals("double") || valueLower.equals("d"))
                defaultDecimal = "d";
        }
    }
}
