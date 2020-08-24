package uwu.smsgamer.cloudnetmsg;

import net.md_5.bungee.api.ChatColor;

public class StrU {
    // messaging between players'n'shit
    public static String messaging(String message, String sender, String receiver, String msg) {
        return ChatColor.
          translateAlternateColorCodes(
            '&', message.replace("%sender%", sender).replace("%receiver%", receiver)).replace("%msg%", msg);
    }

    // usage commands
    public static String usage(String message, String sender, String cmd) {
        return ChatColor.translateAlternateColorCodes('&', message.replace("%sender%", sender).replace("%cmd%", cmd));
    }
}
