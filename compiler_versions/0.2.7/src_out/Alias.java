import java.io.*;
import java.util.*;

public class Alias {
    public String name;
    public ArrayList < Token > tokens;
    public ArrayList < String > args;
    public Alias(String name , ArrayList < Token > tokens , ArrayList < String > args) {
        this . name = name;
        this . tokens = tokens;
        this . args = args;
    }
}

