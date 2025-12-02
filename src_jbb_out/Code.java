import java.io.*;
import java.util.*;

public class Code {
    public void what() {
        LangUtil.nullCheck(getPlayer(), _t0 -> _t0.name().upper());
        LangUtil.nullCheck(getPlayer(), _t1 -> _t1.sendMessage("Hi!"));
        LangUtil.nullCheck(player, _t3 -> LangUtil.nullCheck(_t3.name(), _t2 -> _t2.upper().how())) + "what";
        LangUtil.nullCheck(player.name().upper(), _t4 -> _t4.how());
    }
    public static void main(String[] args) {
        var empty = List.of();
        empty = List.of(1, 2, 3);
        for (var i : LangUtil.asIterable(10)) {
            i = 20; {
                i = 30;
            }
            i = 40;
        }
        var i = 50;
        var lst = List.of(1, 2, 3, "hello");
        var ints = List.of(0, 1, 2, 3);
        LangUtil.println(LangUtil.slice(ints, 0, - 1, 1), LangUtil.slice(ints, null, null, - 1), LangUtil.slice(ints, null, null, 1), LangUtil.slice(ints, null, 2, - 1), LangUtil.slice(ints, 1, null, 1));
        LangUtil.println(Extensions.operEq("69", "69"));
    }
}

