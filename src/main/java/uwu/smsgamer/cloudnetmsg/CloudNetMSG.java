package uwu.smsgamer.cloudnetmsg;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.cloudnetmsg.events.MessageEvent;

public final class CloudNetMSG extends Plugin {

    @Override
    public void onEnable() {
//        CloudNetDriver.getInstance().getEventManager().callEvent(new MessageEvent());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
