package aidenbc.UVABOC;

import java.io.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class UVABOC extends JavaPlugin {
    public static final int BUILD_ID = 84;
    public Random rand = new Random();
    public static void main(String[] args) {
        LangUtil.println(a.b().c.d().e.f);
    }
    @Override public void onEnable() {
        var health = Extensions.operDiv(Math.round(Extensions.operDiv(Extensions.operMul(10, (Extensions.operSub(victim.getHealth(), e.getFinalDamage()))), 2)), 10.0f);
        health = LangUtil.round(Extensions.operDiv((Extensions.operSub(victim.getHealth(), e.getFinalDamage())), 2), 2);
        health = LangUtil.roundstr(Extensions.operDiv((Extensions.operSub(victim.getHealth(), e.getFinalDamage())), 2), 2);
        Var . plugin = this;
        EventHandling.addListener("Listeners", new Listeners());
        EventHandling.addListener("PlayerFreezing", new PlayerFreezing());
        EventHandling.addListener("Stats", new Stats());
        DB.load();
        Teams.load();
        var me = getServer().getPlayer(Master.myUUID);
        if (LangUtil.isTruthy(me)) {
            me.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("", (ChatColor.GOLD)), "UVABOC: "), (ChatColor.RESET)), ""), (BUILD_ID)), ""));
        }
        else {
            getServer().broadcastMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd("", (ChatColor.GOLD)), "UVABOC: "), (ChatColor.RESET)), ""), (BUILD_ID)), ""));
        }
    }
    @Override public void onDisable() {
        
    }
    @Override public boolean onCommand(final CommandSender sender , Command command , String label , String [] args) {
        Teams.setup();
        var cmd = command.getName();
        
        if (LangUtil.isTruthy(false)) {
            
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "joinspeedrunners"))) {
            if (LangUtil.isTruthy(Teams.teamsLocked)) {
                sender.sendMessage(Extensions.operAdd(C_RED, "Teams are locked."));
                return true;
            }
            var pl = (Player)sender;
            Teams.setTeam(pl, "speedrunners");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GREEN, "Successfully joined the "), C_DARK_AQUA), "speedrunners"), C_GREEN), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "joinchildren"))) {
            if (LangUtil.isTruthy(Teams.teamsLocked)) {
                sender.sendMessage(Extensions.operAdd(C_RED, "Teams are locked."));
                return true;
            }
            var pl = (Player)sender;
            Teams.setTeam(pl, "children");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GREEN, "Successfully joined the "), C_LIGHT_PURPLE), "children"), C_GREEN), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "announce"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                return false;
            }
            var pl = (Player)sender;
            getServer().broadcastMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(pl.getDisplayName(), " says: "), C_GOLD), Extensions.operGetIndex(args, 0)));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "freeze"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                PlayerFreezing.setAllFrozen(true);
                sender.sendMessage(Extensions.operAdd(C_RED, "All players are frozen."));
            }
            else {
                PlayerFreezing.setFrozen(Extensions.operGetIndex(args, 0), true);
                sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GOLD, Extensions.operGetIndex(args, 0)), C_RED), " is now frozen."));
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "unfreeze"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                PlayerFreezing.setAllFrozen(false);
                sender.sendMessage(Extensions.operAdd(C_GREEN, "All players are unfrozen."));
            }
            else {
                PlayerFreezing.setFrozen(Extensions.operGetIndex(args, 0), false);
                sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GOLD, Extensions.operGetIndex(args, 0)), C_GREEN), " is now unfrozen."));
            }
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "forcejoinspeedrunners"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                return false;
            }
            Player pl = sender.getServer().getPlayer(Extensions.operGetIndex(args, 0));
            Teams.setTeam(pl, "speedrunners");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GREEN, "Successfully put "), pl.getName()), " into the "), C_DARK_AQUA), "speedrunners"), C_GREEN), " team."));
            pl.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GOLD, "You were placed into the "), C_DARK_AQUA), "speedrunners"), C_GOLD), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "forcejoinchildren"))) {
            if (LangUtil.isTruthy(args.length < 1)) {
                return false;
            }
            Player pl = sender.getServer().getPlayer(Extensions.operGetIndex(args, 0));
            Teams.setTeam(pl, "children");
            sender.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GREEN, "Successfully put "), pl.getName()), " into the "), C_LIGHT_PURPLE), "children"), C_GREEN), " team."));
            pl.sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_GOLD, "You were placed into the "), C_LIGHT_PURPLE), "children"), C_GOLD), " team."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "lockteams"))) {
            if (LangUtil.isTruthy(Teams.teamsLocked)) {
                sender.sendMessage(Extensions.operAdd(C_GOLD, "Teams are already locked."));
                return true;
            }
            Teams . teamsLocked = true;
            sender.sendMessage(Extensions.operAdd(C_RED, "Teams are now locked."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "unlockteams"))) {
            if (LangUtil.isTruthy(!LangUtil.isTruthy(Teams.teamsLocked))) {
                sender.sendMessage(Extensions.operAdd(C_GOLD, "Teams are already unlocked."));
                return true;
            }
            Teams . teamsLocked = false;
            sender.sendMessage(Extensions.operAdd(C_GREEN, "Teams are now unlocked."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "resetevents"))) {
            Var . speedrunnersInNether = false;
            sender.sendMessage(Extensions.operAdd(C_GREEN, "Events reset."));
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "startgame"))) {
            if (LangUtil.isTruthy(Var.gameStarted)) {
                sender.sendMessage(Extensions.operAdd(C_RED, "The game is already going!"));
                return true;
            }
            var gracePeriod = 30;
            if (LangUtil.isTruthy(args.length > 0)) {
                gracePeriod = Integer.parseInt(Extensions.operGetIndex(args, 0));
            }
            getLogger().setFilter(record -> !record.getMessage().toLowerCase().startsWith("/gamerule"));
            getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback false");
            getServer().dispatchCommand(getServer().getConsoleSender(), "time set day");
            Var.setGameRule(GameRule.KEEP_INVENTORY, false);
            Var.setGameRule(GameRule.LOCATOR_BAR, false);
            Var.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            for (var pl : LangUtil.asIterable(getServer().getOnlinePlayers())) {
                pl.setHealth(0);
                var team = Teams.getTeam(pl);
                if (LangUtil.isTruthy(Extensions.operEq(team, "children"))) {
                    pl.setGameMode(GameMode.ADVENTURE);
                }
                else {
                    pl.setGameMode(GameMode.SURVIVAL);
                }
            }
            Var.setGameRule(GameRule.KEEP_INVENTORY, true);
            Var.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
            getServer().dispatchCommand(getServer().getConsoleSender(), "gamerule sendCommandFeedback true");
            Var.clearChat();
            Var . gracePeriodOn = true;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Var.plugin, () -> {
                        getServer().broadcastMessage(C_RED + "Grace period has ended!");
            Var.gracePeriodOn = false;
                        Collection<? extends Player> players1 = getServer().getOnlinePlayers();
            for (Player pl : players1) {
                pl.setGameMode(GameMode.SURVIVAL);
            }
        }, 20L * gracePeriod);
            Var.dangerSenseTask = new BukkitRunnable() {
            public void run() {
                                Team speedrunners = Teams.getSpeedrunners();
                Team children = Teams.getChildren();

                for (String name : speedrunners.getEntries()) {
                    Player pl = getServer().getPlayer(name);

                    if (pl == null)
                        continue;

                                        double closestDistance = Double.MAX_VALUE;

                    for (String child : children.getEntries()) {
                        Player childPl = getServer().getPlayer(child);

                                                if (childPl == null || pl.getWorld() != childPl.getWorld())
                            continue;

                        double distance = pl.getLocation().distanceSquared(childPl.getLocation());

                        if (distance < closestDistance)
                            closestDistance = distance;
                    }

                    if (closestDistance < 5 * 5)
                        Var.sendToActionBar(pl, C_BLACK + "A child is nearby");
                    else if (closestDistance < 10 * 10)
                        Var.sendToActionBar(pl, C_DARK_RED + "A child is nearby");
                    else if (closestDistance < 20 * 20)
                        Var.sendToActionBar(pl, C_RED + "A child is nearby");
                    else if (closestDistance < 30 * 30)
                        Var.sendToActionBar(pl, C_YELLOW + "A child is nearby");
                    else if (closestDistance < 50 * 50)
                        Var.sendToActionBar(pl, "A child is nearby");
                }
            }
        };
            Var.dangerSenseTask.runTaskTimer(this, Extensions.operMul(2, 20L), Extensions.operMul(2, 20L));
            getServer().broadcastMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_AQUA, "The game has begun. Speedrunners have a "), gracePeriod), " second grace period."));
            Teams.getImposter().sendMessage(Extensions.operAdd(C_DARK_PURPLE, "You are the imposter. Ensure that the speedrunners lose. You may attack speedrunners, but make sure you survive until the end."));
            Team children = Teams.getChildren();
            for (var name : LangUtil.asIterable(children.getEntries())) {
                getServer().getPlayer(name).sendMessage(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(Extensions.operAdd(C_DARK_PURPLE, "The imposter is: "), C_GOLD), Teams.getImposter().getName()), C_DARK_PURPLE), ". The speedrunners are not aware of this. Do not attack the imposter."));
            }
            Teams . teamsLocked = true;
            Var . playersFrozen = false;
            Var . gameStarted = true;
            Var . speedrunnersInNether = false;
            Stats.reset();
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "stopgame"))) {
            if (LangUtil.isTruthy(Var.dangerSenseTask)) { Var.dangerSenseTask.cancel(); }
            Var.endGame("");
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "restartgame"))) {
            sender.sendMessage(Extensions.operAdd(C_GREEN, "The game has restarted."));
            Var . gameStarted = true;
        }
        else if (LangUtil.isTruthy(Extensions.operIn(cmd, List.of("fortress", "bastion", "stronghold")))) {
            var pl = (Player)sender;
            var wasOp = pl.isOp();
            pl.setOp(true);
            if (LangUtil.isTruthy(Extensions.operEq(cmd, "bastion"))) {
                pl.performCommand("locate structure bastion_remnant");
            }
            else {
                pl.performCommand(Extensions.operAdd("locate structure ", cmd));
            }
            pl.setOp(wasOp);
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "surrender"))) {
            var pl = (Player)sender;
            pl.setHealth(0);
        }
        else if (LangUtil.isTruthy(Extensions.operEq(cmd, "stats"))) {
            var name = LangUtil.isTruthy(args) ? (Extensions.operGetIndex(args, 0)) : (sender.getName());
            sender.sendMessage(Stats.displayStats(name));
        }
        else {
            return false;
        }
        return true;
    }
}

