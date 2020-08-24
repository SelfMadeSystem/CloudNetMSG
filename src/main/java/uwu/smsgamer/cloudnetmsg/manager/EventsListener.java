package uwu.smsgamer.cloudnetmsg.manager;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import uwu.smsgamer.cloudnetmsg.*;

public class EventsListener implements Listener {
    @EventHandler
    public void onJoin(PostLoginEvent event) {
        PlayerManager.playerHashMap.put(event.getPlayer().getName().toLowerCase(), new CPlayer(event.getPlayer()));
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "pjoin",
          JsonDocument.newDocument().append("message", event.getPlayer().getName()));
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        PlayerManager.playerHashMap.remove(event.getPlayer().getName().toLowerCase());
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "pquit",
          JsonDocument.newDocument().append("message", event.getPlayer().getName()));
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        CPlayer player = PlayerManager.getPlayer(((ProxiedPlayer) event.getSender()).getName());
        assert player != null;
        if (event.getMessage().startsWith("/")) {// TODO: 2020-08-24 admin commands like reload
            String msg = event.getMessage();
            int indexOf = msg.indexOf(" ");
            String cmd = msg.substring(1).substring(0, indexOf < 0 ? msg.length() - 1 : indexOf).trim();
            String rawArgs = msg.substring(indexOf + 1);
            String[] args;
            if (indexOf != -1)
                args = rawArgs.split(" ");
            else
                args = new String[0];
            switch (cmd.toLowerCase()) {
                case "reply":
                case "r": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.reply")) {
                        if (args.length == 0) {
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.usageReply, player.getName(), cmd)));
                            break;
                        }
                        if (player.lastMsg == null || player.lastMsg.isEmpty()) {
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.replyNoLast, player.getName(), cmd)));
                            break;
                        }
                        player.sendMSG(player.lastMsg, rawArgs);
                    }
                    break;
                }
                case "w":
                case "tell":
                case "pm":
                case "msg": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.message")) {
                        if (args.length <= 1) {
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.usageMsg, player.getName(), cmd)));
                            break;
                        }
                        String rawArgs1 = rawArgs.substring(rawArgs.indexOf(" ") + 1);
                        player.sendMSG(args[0], rawArgs1);
                    }
                    break;
                }
                case "gc":
                case "globalchat": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.globalchat")) {
                        if (args.length == 0) {
                            if (player.enableGCChat)
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOff, player.getName(), cmd)));
                            else
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOn, player.getName(), cmd)));
                            player.enableGCChat = !player.enableGCChat;
                            break;
                        }
                        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "globalchat",
                          JsonDocument.newDocument().append("message", rawArgs).append("sender", player.getName()));
//                        CloudNetDriver.getInstance().getEventManager().callEvent(
//                          new MessageEvent(player.getName(), rawArgs, MessageEvent.Type.GLOBAL_CHAT));
                    }
                    break;
                }
                case "bc":
                case "broadcast": {
                    // TODO: 2020-08-24 maybe have
                    //  /bc Broadcast in survival only! --servers Survival
                    //  or
                    //  /bc Broadcast in survival and lobby! --servers Survival Lobby
                    //  or
                    //  /bc Broadcast not in lobby! --no-servers Lobby
                    //  or
                    //  /bc Broadcast not in lobby or survival! --no-servers Lobby Survival
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.broadcast")) {
                        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "broadcast",
                          JsonDocument.newDocument().append("message", rawArgs).append("sender", player.getName()));
//                        CloudNetDriver.getInstance().getEventManager().callEvent(
//                          new MessageEvent(player.getName(), String.join(" ", args)));
                    }
                    break;
                }
                case "sc":
                case "staffchat": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.staffchat")) {
                        if (args.length == 0) {
                            if (player.enableSC)
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.scOff, player.getName(), cmd)));
                            else
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.scOn, player.getName(), cmd)));
                            player.enableSC = !player.enableSC;
                            break;
                        }
                        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "staffchat",
                          JsonDocument.newDocument().append("message", rawArgs).append("sender", player.getName()));
//                        CloudNetDriver.getInstance().getEventManager().callEvent(
//                          new MessageEvent(player.getName(), rawArgs, MessageEvent.Type.STAFF_CHAT));
                    }
                    break;
                }
            }
        } else {// TODO: 2020-08-24 .-. finish this
            if (player.enableSC) {
                event.setCancelled(true);
            } else if (player.enableGCChat) {
                event.setCancelled(true);
            }
        }
    }

    @EventListener
    public void handleChannelMessage(ChannelMessageReceiveEvent event) {
        if (event.getChannel().equals("cloudnetmsg")) {
            JsonDocument data = event.getData();
            String msg = data.getString("message");
            switch (event.getMessage().toLowerCase()) {
                case "message": {
                    String sender = data.getString("sender");
                    CPlayer receiver = PlayerManager.getPlayer(data.getString("receiver"));
                    if (receiver != null) {
                        if (receiver.getMSG(sender, msg))
                            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "staffchat",
                              JsonDocument.newDocument().append("message", msg).append("sender", sender).append("receiver", receiver.getName()));
                    }
                    break;
                }
                case "globalchat": {
                    String sender = data.getString("sender");
                    msg = StrU.messaging(Vars.globalChat, sender, "%receiver%", msg);
                    for (CPlayer player : PlayerManager.playerHashMap.values()) {
                        if (player.enableGC)
                            player.sender.sendMessage(new TextComponent(msg));
                    }
                    break;
                }
                case "staffchat": {
                    String sender = data.getString("sender");
                    msg = StrU.messaging(Vars.staffChat, sender, "%receiver%", msg);
                    for (CPlayer player : PlayerManager.playerHashMap.values()) {
                        if (player.sender.hasPermission("cloudnetmsg.staffchat"))
                            player.sender.sendMessage(new TextComponent(msg));
                    }
                    break;
                }
                case "broadcast": {
                    String sender = data.getString("sender");
                    msg = StrU.messaging(Vars.broadcast, sender, "%receiver%", msg);
                    for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                        p.sendMessage(new TextComponent(msg.replace("%receiver%", p.getName())));
                    break;
                }
                case "pjoin": {
                    msg = msg.toLowerCase();
                    if (!PlayerManager.playerHashMap.containsKey(msg))
                        PlayerManager.otherPlayers.add(msg);
                    break;
                }
                case "pquit": {
                    msg = msg.toLowerCase();
                    PlayerManager.otherPlayers.remove(msg);
                    break;
                }
                case "pdisablemsg": {
                    String receiver = data.getString("receiver");
                    CPlayer sender = PlayerManager.getPlayer(data.getString("sender"));
                    if (sender != null)
                        sender.sender.sendMessage(new TextComponent(StrU.messaging(Vars.disabledMSG, sender.getName(), receiver, msg)));
                    break;
                }
                default:
                    throw new IllegalArgumentException("HandleChannelMessage's message in CloudNetMSG is: " + msg);
            }
        }
    }

    /*@EventListener
    public void onMSG(MessageEvent event) {
        switch (event.type) {
            case MSG: {
                CPlayer player = PlayerManager.getPlayer(event.receiver);
                if (player != null) {
                    event.returnType = player.getMSG(event.sender, event.message);
                }
                break;
            }
            case BROADCAST: {
                String msg = StrU.messaging(Vars.broadcast, event.sender, "%receiver%", event.message);
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
                    p.sendMessage(new TextComponent(msg.replace("%receiver%", p.getName())));
                break;
            }
            case STAFF_CHAT: {
                String msg = StrU.messaging(Vars.staffChat, event.sender, "%receiver%", event.message);
                for (CPlayer player : PlayerManager.playerHashMap.values()) {
                    if (player.sender.hasPermission("cloudnetmsg.staffchat"))
                        player.sender.sendMessage(new TextComponent(msg));
                }
                break;
            }
            case GLOBAL_CHAT: {
                String msg = StrU.messaging(Vars.globalChat, event.sender, "%receiver%", event.message);
                for (CPlayer player : PlayerManager.playerHashMap.values()) {
                    if (player.enableGC)
                        player.sender.sendMessage(new TextComponent(msg));
                }
                break;
            }
        }
    }*/
}
