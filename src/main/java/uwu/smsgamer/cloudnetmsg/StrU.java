package uwu.smsgamer.cloudnetmsg;

import net.md_5.bungee.api.ChatColor;

public class StrU {
    public static String replaceString(String message, String sender, String receiver, String msg) {
        return ChatColor.translateAlternateColorCodes('&', message.replace("%sender%", sender)
          .replace("%receiver%", receiver)).replace("%msg%", msg);
    }
}
