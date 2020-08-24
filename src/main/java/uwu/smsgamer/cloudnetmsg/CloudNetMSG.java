package uwu.smsgamer.cloudnetmsg;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import net.md_5.bungee.api.plugin.Plugin;
import uwu.smsgamer.cloudnetmsg.manager.EventsListener;

public final class CloudNetMSG extends Plugin {

    // TODO: 2020-08-24 Make Bukkit java plugin as well (?) and cloudnet module as well (?) (maybe not)
    EventsListener listener = new EventsListener();
    @Override
    public void onEnable() {
        Vars.loadYaml(this.getDataFolder());
        this.getProxy().getPluginManager().registerListener(this, listener);
        CloudNetDriver.getInstance().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
