/*
package uwu.smsgamer.cloudnetmsg.events;

import de.dytanic.cloudnet.driver.event.*;

public class MessageEvent extends Event {
    public String sender;
    public String receiver;
    public String message;
    public int returnType = 0; //-1 unknown error, 0 non existent player, 1 worked, 2 player disabled messages
    public Type type;

    public MessageEvent(String sender, String receiver, String message, Type type) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.type = type;
    }

    public MessageEvent(String sender, String message, Type type) {
        this.sender = sender;
        this.message = message;
        this.type = type;
    }

    public MessageEvent(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.type = Type.BROADCAST;
    }

    @Override
    public boolean isShowDebug() {
        return false;
    }

    public enum Type {
        MSG,
        BROADCAST,
        STAFF_CHAT,
        GLOBAL_CHAT
    }
}
*/
