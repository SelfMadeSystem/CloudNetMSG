package uwu.smsgamer.cloudnetmsg.manager;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.TextComponent;
import uwu.smsgamer.cloudnetmsg.*;

public class CPlayer { // TODO: 2020-08-23 Use MatrixPvPBase's shit w/ names chat colour nicks prefixes suffixes you know the drill oki doki
    public CommandSender sender; // instance of the sender associated w/ this
    public String lastMsg;      // last person's name (for /reply)
    public boolean enableMSGs = true; // messages
    public boolean enableSC = false; // staff chat for every msg
    public boolean enableGC = true; // global chat
    public boolean enableGCChat = false;  // global chat for every msg

    public CPlayer(CommandSender sender) {
        this.sender = sender;
    }

    public String getName() {
        return sender.getName();
    }

    public void sendMSG(String player, String message) {
        if (PlayerManager.playerExists(player)) {
            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "message",
              JsonDocument.newDocument().append("message", message).append("sender", getName()).append("receiver", player));
            sender.sendMessage(new TextComponent(StrU.messaging(Vars.toMSG, getName(), player, message)));
            lastMsg = player;
        } else {
            sender.sendMessage(new TextComponent(StrU.messaging(Vars.noPlayer, getName(), player, message)));
        }
        /*MessageEvent messageEvent = new MessageEvent(getName(), player, message, MessageEvent.Type.MSG);
        CloudNetDriver.getInstance().getEventManager().callEvent(messageEvent);

        switch (messageEvent.returnType) {
            case -1:
                sender.sendMessage(new TextComponent(ChatColor.RED + "An unknown error has occurred. Please contact development " +
                  "to resolve this issue or look at console if a developer."));
                break;
            case 0:
                sender.sendMessage(new TextComponent(StrU.messaging(Vars.noPlayer, getName(), player, message)));
                break;
            case 1:
                sender.sendMessage(new TextComponent(StrU.messaging(Vars.toMSG, getName(), player, message)));
                lastMsg = player;
                break;
            case 2:
                sender.sendMessage(new TextComponent(StrU.messaging(Vars.disabledMSG, getName(), player, message)));
                break;
        }*/
    }

    public boolean getMSG(String player, String message) {
        if (enableMSGs) {
            sender.sendMessage(new TextComponent(StrU.messaging(Vars.fromMSG, player, getName(), message)));
            lastMsg = player;
            return false;
        }
        return true;
    }
}
