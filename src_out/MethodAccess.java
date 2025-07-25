import java.io.*;
import java.util.*;

public class MethodAccess {
    {
        var  = AccessMod . PUBLIC;
        var  = false;
        public MethodAccess (AccessMod accessMod, boolean isStatic) {; {
            var  = accessMod;
            var  = isStatic;
    }
        };
        public static AccessMod accessModFromToken (Token tok) {; {
            if ((tok . value . equals ("public") | | tok . value . equals ("+"))) {
                return AccessMod . PUBLIC;
    }
            if ((tok . value . equals ("private") | | tok . value . equals ("-"))) {
                return AccessMod . PRIVATE;
    }
            if ((tok . value . equals ("protected") | | tok . value . equals ("*"))) {
                return AccessMod . PROTECTED;
    }
            return AccessMod . DEFAULT;
    }
        };
        public static String accessModToString (AccessMod accessMod) {; {
            if ((( == AccessMod . PUBLIC))) {
                return "public";
    }
            if ((( == AccessMod . PRIVATE))) {
                return "private";
    }
            if ((( == AccessMod . PROTECTED))) {
                return "protected";
    }
            return "";
    }
        };
        public String toString () {; {
            return accessModToString (accessMod) + (isStatic ? " static" : "");
    }
        };
    }
    };
}

