import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) {
        switch (startTok) {
            case IF -> out += "if (";
            case ELIF -> out += "else if (";
            case ELSE -> out += "else";
            case WHILE -> out += "while (";
            case UNTIL -> out += "while (!(";
            default -> {}
        }
    }
}

