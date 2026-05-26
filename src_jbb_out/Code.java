import java.io.*;
import java.util.*;

public class Code {
    public ArrayList<Token> tokens;
    public ArrayList<String> args;
    public void Alias(ArrayList<Token> tokens, ArrayList<String> args) {
        this . tokens = tokens;
        this . args = args;
    }
    public void Alias(ArrayList<Token> tokens) {
        this . tokens = tokens;
        this . args = new ArrayList<String>();
    }
}

