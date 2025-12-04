import java.io.*;
import java.util.*;

public class Code extends JavaPlugin {
    public static final int BUILD_ID = 79;
    public Random rand = new Random();
    @Override public boolean onCommand(final CommandSender sender , Command command , String label , String [] args) {
        Teams.setup();
        var cmd = command.getName();
        if (LangUtil.isTruthy(false)) {
            
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "joinspeedrunners"))) {
            if (LangUtil.isTruthy(Teams.teamsLocked)) {
                sender.sendMessage(Extensions.operAdd(ChatColor.RED, "Teams are locked."));
                return true;
            }
            var pl = (Player)sender;
            Teams.setTeam(pl, "speedrunners");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(ChatColor.GREEN, "Successfully joined the "), ChatColor.DARK_AQUA), "speedrunners"), ChatColor.GREEN), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "newcmd")))
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "joinchildren"))) {
            if (LangUtil.isTruthy(Teams.teamsLocked)) {
                sender.sendMessage(Extensions.operAdd(ChatColor.RED, "Teams are locked."));
                return true;
            }
            var pl = (Player)sender;
            Teams.setTeam(pl, "children");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(ChatColor.GREEN, "Successfully joined the "), ChatColor.LIGHT_PURPLE), "children"), ChatColor.GREEN), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "announce"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                return false;
            }
            var pl = (Player)sender;
            getServer().broadcastMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(pl.getDisplayName(), " says: "), ChatColor.GOLD), Extensions.operGetIndex(args, 0)));
        }
    }
}

