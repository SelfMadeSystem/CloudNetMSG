package uwu.smsgamer.cloudnetmsg.manager;

import net.md_5.bungee.api.CommandSender;

import javax.annotation.Nullable;
import java.util.*;

public class PlayerManager {
    public static HashMap<String, CPlayer> playerHashMap = new HashMap<>();
    public static List<String> otherPlayers = new ArrayList<>();

    @Nullable
    public static CPlayer getPlayer(CommandSender sender) {
        return playerHashMap.get(sender.getName().toLowerCase());
    }

    @Nullable
    public static CPlayer getPlayer(String s) {
        return playerHashMap.get(s.toLowerCase());
    }

    public static boolean playerExists(String p) {
        return playerHashMap.containsKey(p.toLowerCase()) || otherPlayers.contains(p.toLowerCase());
    }
}
