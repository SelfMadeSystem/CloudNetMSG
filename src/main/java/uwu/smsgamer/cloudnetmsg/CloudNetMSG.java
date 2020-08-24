package uwu.smsgamer.cloudnetmsg;

import net.md_5.bungee.api.plugin.Plugin;

public final class CloudNetMSG extends Plugin {

    // TODO: 2020-08-24 Make Bukkit java plugin as well (?) and cloudnet module as well (?) (maybe not)

    @Override
    public void onEnable() {
        Vars.loadYaml(this.getDataFolder());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
