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
        if (event.getMessage().startsWith("/")) {
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
                case "cnmsgr":
                case "cnmsgreload":
                case "cloudnetmsgreload": {
                    if (player.sender.hasPermission("cloudnetmsg.commands.reload")) {
                        Vars.loadYaml(Vars.dataFolder);
                        player.sender.sendMessage(new TextComponent("Reloaded config."));
                    }
                }
                case "reply":
                case "r": {
                    event.setCancelled(true);
                    //if (player.sender.hasPermission("cloudnetmsg.commands.reply")) {
                    if (args.length == 0) {
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.usageReply, player.getName(), cmd)));
                        break;
                    }
                    if (player.lastMsg == null || player.lastMsg.isEmpty()) {
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.replyNoLast, player.getName(), cmd)));
                        break;
                    }
                    player.sendMSG(player.lastMsg, rawArgs);
                    //}
                    break;
                }
                case "w":
                case "tell":
                case "pm":
                case "msg": {
                    event.setCancelled(true);
                    //if (player.sender.hasPermission("cloudnetmsg.commands.message")) {
                    if (args.length <= 1) {
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.usageMsg, player.getName(), cmd)));
                        break;
                    }
                    String rawArgs1 = rawArgs.substring(rawArgs.indexOf(" ") + 1);
                    player.sendMSG(args[0], rawArgs1);
                    //}
                    break;
                }
                case "gc":
                case "globalchat": {
                    event.setCancelled(true);
                    //if (player.sender.hasPermission("cloudnetmsg.commands.globalchat")) {
                    if (args.length == 0) {
                        if (player.enableGC)
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOff, player.getName(), cmd)));
                        else
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOn, player.getName(), cmd)));
                        player.enableGC = !player.enableGC;
                        break;
                    }
                    CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "globalchat",
                      JsonDocument.newDocument().append("message", rawArgs).append("sender", player.getName()));
//                        CloudNetDriver.getInstance().getEventManager().callEvent(
//                          new MessageEvent(player.getName(), rawArgs, MessageEvent.Type.GLOBAL_CHAT));
                    //}
                    break;
                }
                case "gct":
                case "gctoggle":
                case "globalchattoggle": {
                    event.setCancelled(true);
                    if (args.length == 0) {
                        if (player.enableGCChat)
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOffMsg, player.getName(), cmd)));
                        else
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOnMsg, player.getName(), cmd)));
                        player.enableGCChat = !player.enableGCChat;
                        break;
                    }
                    boolean on = false;
                    switch (args[0].toLowerCase()) {
                        case "yes":
                        case "on":
                        case "enable":
                        case "enabled":
                        case "true":
                            on = true;
                            break;
                        case "toggle":
                            on = !player.enableGCChat;
                    }
                    if (on)
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOnMsg, player.getName(), cmd)));
                    else
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.gcOffMsg, player.getName(), cmd)));
                    player.enableGCChat = on;
                    break;
                }
                case "chatspy": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.chatspy")) {
                        if (args.length == 0) {
                            if (player.enableChatSpy)
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.csOff, player.getName(), cmd)));
                            else
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.csOn, player.getName(), cmd)));
                            player.enableChatSpy = !player.enableChatSpy;
                            break;
                        }
                        boolean on = false;
                        switch (args[0].toLowerCase()) {
                            case "yes":
                            case "on":
                            case "enable":
                            case "enabled":
                            case "true":
                                on = true;
                                break;
                            case "toggle":
                                on = !player.enableChatSpy;
                        }
                        if (on)
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.csOff, player.getName(), cmd)));
                        else
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.csOn, player.getName(), cmd)));
                        player.enableChatSpy = on;
                        break;
                    } else
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.noPermission, player.getName(), cmd)));
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
                    } else
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.noPermission, player.getName(), cmd)));
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
                    } else
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.noPermission, player.getName(), cmd)));
                    break;
                }
                case "joinalerts":
                case "joinalert": {
                    event.setCancelled(true);
                    if (player.sender.hasPermission("cloudnetmsg.commands.joinalert")) {
                        if (args.length == 0) {
                            if (player.enableJA)
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.jaOff, player.getName(), cmd)));
                            else
                                ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.jaOn, player.getName(), cmd)));
                            player.enableJA = !player.enableJA;
                            break;
                        }
                        boolean on = false;
                        switch (args[0].toLowerCase()) {
                            case "yes":
                            case "on":
                            case "enable":
                            case "enabled":
                            case "true":
                                on = true;
                                break;
                            case "toggle":
                                on = !player.enableJA;
                        }
                        if (on)
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.jaOff, player.getName(), cmd)));
                        else
                            ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.jaOn, player.getName(), cmd)));
                        player.enableJA = on;
                        break;
                    } else
                        ((ProxiedPlayer) event.getSender()).sendMessage(new TextComponent(StrU.usage(Vars.noPermission, player.getName(), cmd)));
                    break;
                }
            }
        } else {
            if (player.enableSC) {
                event.setCancelled(true);
                CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "staffchat",
                  JsonDocument.newDocument().append("message", event.getMessage()).append("sender", player.getName()));
            } else if (player.enableGCChat && player.enableGC) {
                CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "globalchat",
                  JsonDocument.newDocument().append("message", event.getMessage()).append("sender", player.getName()));
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
                            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "msgplayer",
                              JsonDocument.newDocument().append("message", StrU.messaging(Vars.disabledMSG, sender, receiver.getName(), msg))
                                .append("receiver", receiver.getName()));
                        else
                            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cloudnetmsg", "msgplayer",
                              JsonDocument.newDocument().append("message", StrU.messaging(Vars.toMSG, sender, receiver.getName(), msg))
                                .append("receiver", receiver.getName()));
                    }
                    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
                        if (PlayerManager.getPlayer(player).enableChatSpy)
                            player.sendMessage(new TextComponent(StrU.messaging(Vars.chatSpy, sender, data.getString("receiver"), msg)));
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
                    for (CPlayer player : PlayerManager.playerHashMap.values())
                        if (player.enableJA)
                            player.sender.sendMessage(new TextComponent(StrU.messaging(Vars.broadcast, msg, "%receiver%", "%msg%")));
                    break;
                }
                case "pquit": {
                    msg = msg.toLowerCase();
                    PlayerManager.otherPlayers.remove(msg);
                    break;
                }
                case "msgplayer": {
                    CPlayer receiver = PlayerManager.getPlayer(data.getString("receiver"));
                    if (receiver != null)
                        receiver.sender.sendMessage(new TextComponent(msg));
                    /*CPlayer sender = PlayerManager.getPlayer(data.getString("sender"));
                    if (sender != null)
                        sender.sender.sendMessage(new TextComponent(StrU.messaging(Vars.disabledMSG, sender.getName(), receiver, msg)));*/
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
