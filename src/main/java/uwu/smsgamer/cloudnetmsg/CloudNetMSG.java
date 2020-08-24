package uwu.smsgamer.cloudnetmsg;

import net.md_5.bungee.api.plugin.Plugin;

public final class CloudNetMSG extends Plugin {

    @Override
    public void onEnable() {
        Vars.loadYaml(this.getDataFolder());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
