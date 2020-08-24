package uwu.smsgamer.cloudnetmsg.manager;

import de.dytanic.cloudnet.driver.event.EventListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import uwu.smsgamer.cloudnetmsg.*;
import uwu.smsgamer.cloudnetmsg.events.MessageEvent;

public class EventsListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        PlayerManager.playerHashMap.put(event.getPlayer().getName(), new CPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        PlayerManager.playerHashMap.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        CPlayer player = PlayerManager.getPlayer(((ProxiedPlayer) event.getSender()).getName());
        if (event.getMessage().startsWith("/")) {
            String msg = event.getMessage();
            int indexOf = msg.indexOf(" ");
            String cmd = msg.substring(1).substring(0, indexOf < 0 ? msg.length() - 1 : indexOf);
            switch (cmd.toLowerCase()) {
                case "reply":
                case "r":
                    break;
                case "w":
                case "msg":
                    break;
            }
        } else {
            if (player.enableSC) {
            } else if (player.enableGC) {
            }
        }
    }

    @EventListener
    public void onMSG(MessageEvent event) {
        switch (event.type) {
            case MSG: {
                CPlayer player = PlayerManager.getPlayer(event.receiver);
                if (player != null) {
                    player.getMSG(event.sender, event.message);
                }
                break;
            }
            case BROADCAST: {
                String msg = StrU.replaceString(Vars.broadcast, event.sender, "%receiver%", event.message);
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                    p.sendMessage(new TextComponent(msg.replace("%receiver%", p.getName())));
                break;
            }
            case STAFF_CHAT: {
                String msg = StrU.replaceString(Vars.staffChat, event.sender, "%receiver%", event.message);
                for (CPlayer player : PlayerManager.playerHashMap.values()) {
                    if (player.sender.hasPermission("cloudnetmsg.staffchat"))
                        player.sender.sendMessage(new TextComponent(msg));
                }
                break;
            }
            case GLOBAL_CHAT: {
                String msg = StrU.replaceString(Vars.globalChat, event.sender, "%receiver%", event.message);
                for (CPlayer player : PlayerManager.playerHashMap.values()) {
                    if (player.enableGC)
                        player.sender.sendMessage(new TextComponent(msg));
                }
                break;
            }
        }
    }
}
