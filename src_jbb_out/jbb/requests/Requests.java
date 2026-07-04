import java.io.*;
import java.util.*;

public class Requests {
    public static void post(String url, String info) {
        LangUtil.println(Extensions.operShl(Extensions.operShl(Extensions.operShl("posting ", url), " with "), info));
    }
    public static void get(String url) {
        LangUtil.println(Extensions.operShl("getting ", url));
    }
}

