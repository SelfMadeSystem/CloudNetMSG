package uwu.smsgamer.cloudnetmsg;

import net.md_5.bungee.config.*;

import java.io.*;

public class Vars {
    public static String toMSG = "&b&lYOU&r -> %receiver%: &r%msg%";
    public static String fromMSG = "%sender% -> &b&lYOU&r: %msg%";
    public static String disabledMSG = "%receiver%&c has disabled messages.";
    public static String broadcast = "&c[&l&4Broadcast&c]  &6%msg%";
    public static String staffChat = "&7[&6SC&7]&r %sender%  &r%msg%";
    public static String globalChat = "&7[&bGlobal&7]&r %sender%  &r%msg%";
    public static String gcOn = "Global chat &aenabled&r.";
    public static String gcOff = "Global chat &cdisabled&r.";
    public static String scOn = "Staff chat &aenabled&r.";
    public static String scOff = "Staff chat &cdisabled&r.";

    public static String noPermission = "&cYou do not have permission to execute that command.";
    public static String noPlayer = "&cThat player does not exist.";
    public static String replyNoLast = "&cNobody in last message.";

    public static String usageReply = "Replies to last player you messaged. Usage: /%cmd% (msg).";
    public static String usageMsg = "Messages someone. Can be on another sub server. Usage: /%cmd% (player) (msg).";

    // calling it load yaml just encase we use json as well
    public static void loadYaml(File dataFolder) {
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(dataFolder, "config.yml"));
            Configuration msgs = config.getSection("messages");
            // probably not best way to do it but idfc I think it works (if it doesn't I'll do it another way)
            msgs.set("toMSG", toMSG = msgs.getString("toMSG", toMSG));
            msgs.set("fromMSG", fromMSG = msgs.getString("fromMSG", fromMSG));
            msgs.set("disabledMSG", disabledMSG = msgs.getString("disabledMSG", disabledMSG));
            msgs.set("broadcast", broadcast = msgs.getString("broadcast", broadcast));
            msgs.set("staffChat", staffChat = msgs.getString("staffChat", staffChat));
            msgs.set("globalChat", globalChat = msgs.getString("globalChat", globalChat));
            msgs.set("gcOn", gcOn = msgs.getString("gcOn", gcOn));
            msgs.set("gcOff", gcOff = msgs.getString("gcOff", gcOff));
            msgs.set("scOn", scOn = msgs.getString("scOn", scOn));
            msgs.set("scOff", scOff = msgs.getString("scOff", scOff));
            msgs.set("noPermission", noPermission = msgs.getString("noPermission", noPermission));
            msgs.set("replyNoLast", replyNoLast = msgs.getString("replyNoLast", replyNoLast));
            msgs.set("usageReply", usageReply = msgs.getString("usageReply", usageReply));
            msgs.set("usageMsg", usageMsg = msgs.getString("usageMsg", usageMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
