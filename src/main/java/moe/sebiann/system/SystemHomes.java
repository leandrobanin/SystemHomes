package moe.sebiann.system;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import moe.sebiann.system.commands.*;
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
        // Save and load the configuration
        saveDefaultConfig();

        registerCommands();
        commandCompletions();
    }

    void registerCommands(){
        manager = new PaperCommandManager(this);
        manager.registerCommand(new SystemHomesCommand());

        manager.registerCommand(new HomeCommands());
        manager.registerCommand(new WarpCommands());
        manager.registerCommand(new SpawnCommand());
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
    }

}
