package cloud.cloudie.cloudsystem;

import cloud.cloudie.cloudsystem.commands.SystemHomesCommand;
import cloud.cloudie.cloudsystem.commands.HomeCommands;
import cloud.cloudie.cloudsystem.commands.PlayerWarpCommands;
import cloud.cloudie.cloudsystem.commands.SpawnCommand;
import cloud.cloudie.cloudsystem.commands.TpaCommands;
import cloud.cloudie.cloudsystem.commands.WarpCommands;
import cloud.cloudie.cloudsystem.utils.UpdateChecker;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SystemHomes extends JavaPlugin {

    public static SystemHomes plugin = null;
    PaperCommandManager manager;

    @Override
    public void onEnable() {
        if(plugin == null){
            plugin = this;
        }
        manager = new PaperCommandManager(this);
        saveDefaultConfig();

        getLogger().info("|---[ SystemHomes ]---------------------------------------|");
        getLogger().info("|                                                         |");

        registerCommands();
        commandCompletions();

        getLogger().info("|                                                         |");
        getLogger().info("|------------------------------[ ENABLED SUCCESSFULLY ]---|");

        new UpdateChecker(this).checkForUpdates();
    }

    void registerCommands(){

        manager.registerCommand(new SystemHomesCommand());

        manager.registerCommand(new HomeCommands());
        manager.registerCommand(new WarpCommands());
        manager.registerCommand(new SpawnCommand());
        manager.registerCommand(new TpaCommands());
        manager.registerCommand(new PlayerWarpCommands());

        getLogger().info("|   Enabled commands                                      |");
    }

    void commandCompletions(){
        manager.getCommandCompletions().registerAsyncCompletion("homeNames", c -> {
            String playerName = c.getSender().getName();

            List<String> homeNames = HomeCommands.homeNameToString(playerName);
            return ImmutableList.copyOf(homeNames);
        });

        manager.getCommandCompletions().registerAsyncCompletion("warpNames", c -> {
            List<String> warpNames = WarpCommands.warpNameToString();
            return ImmutableList.copyOf(warpNames);
        });

        manager.getCommandCompletions().registerAsyncCompletion("pwarpNames", c -> {
            List<String> warpNames = PlayerWarpCommands.pwarpNameToString();
            return ImmutableList.copyOf(warpNames);
        });

        getLogger().info("|   Enabled command completions                           |");
    }

    @Override
    public void onDisable() {
        getLogger().info("|---[ SystemHomes ]---------------------------------------|");
        getLogger().info("|                                                         |");
        getLogger().info("|                                                         |");
        getLogger().info("|-----------------------------[ DISABLED SUCCESSFULLY ]---|");
    }
}
