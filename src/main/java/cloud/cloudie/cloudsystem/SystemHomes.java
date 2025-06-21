package cloud.cloudie.cloudsystem;

import cloud.cloudie.cloudsystem.commands.HomeCommands;
import cloud.cloudie.cloudsystem.utils.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemHomes extends JavaPlugin {

    public static SystemHomes plugin = null;

    @Override
    public void onEnable() {
        if(plugin == null){
            plugin = this;
        }

        saveDefaultConfig();

        getLogger().info("|---[ SystemHomes ]---------------------------------------|");
        getLogger().info("|                                                         |");

        registerCommands();

        getLogger().info("|                                                         |");
        getLogger().info("|------------------------------[ ENABLED SUCCESSFULLY ]---|");

        // Mantemos o UpdateChecker se você quiser
        new UpdateChecker(this).checkForUpdates();
    }

    void registerCommands(){
        HomeCommands homeExecutor = new HomeCommands(this);

        getCommand("sethome").setExecutor(homeExecutor);
        getCommand("delhome").setExecutor(homeExecutor);
        getCommand("home").setExecutor(homeExecutor);
        getCommand("homes").setExecutor(homeExecutor);

        getCommand("delhome").setTabCompleter(homeExecutor);
        getCommand("home").setTabCompleter(homeExecutor);

        getLogger().info("|   Enabled commands via Bukkit API                       |");
    }


    @Override
    public void onDisable() {
        getLogger().info("|---[ SystemHomes ]---------------------------------------|");
        getLogger().info("|                                                         |");
        getLogger().info("|                                                         |");
        getLogger().info("|-----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}