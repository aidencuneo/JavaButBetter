import java.io.*;
import java.util.*;

public class Alias {
    public ArrayList < Token > tokens;
    public ArrayList < String > args;
    public Alias(ArrayList < Token > tokens , ArrayList < String > args) {
        var this . tokens = tokens;
        var this . args = args;
    }
    public Alias(ArrayList < Token > tokens) {
        this . tokens = tokens;
        this . args = new ArrayList < String > ();
    }
}

