package uwu.smsgamer.cloudnetmsg.manager;

import net.md_5.bungee.api.CommandSender;

import javax.annotation.Nullable;
import java.util.HashMap;

public class PlayerManager {
    public static HashMap<String, CPlayer> playerHashMap = new HashMap<>();

    @Nullable
    public static CPlayer getPlayer(CommandSender sender) {
        return playerHashMap.get(sender.getName());
    }

    @Nullable
    public static CPlayer getPlayer(String s) {
        return playerHashMap.get(s);
    }
}
