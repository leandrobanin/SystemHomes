package moe.sebiann.system;

import co.aikar.commands.PaperCommandManager;
import moe.sebiann.system.commands.tpa.*;
import moe.sebiann.system.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SystemHomes extends JavaPlugin {

    public static SystemHomes plugin = null;

    public File homesFile;
    private File warpsFile;

    @Override
    public void onEnable() {
        if(plugin == null){
            plugin = this;
        }
        // Save and load the configuration
        saveDefaultConfig();
        // Initialize the homes file
        createHomesFile();
        createWarpsFile();

        TpaManager tpaManager = new TpaManager();

        registerCommands();

//        // Register home commands
//        getCommand("sethome").setExecutor(new SetHomeCommand(homesFile, this));
//        getCommand("home").setExecutor(new HomeCommand(homesFile, this));
//        getCommand("homes").setExecutor(new ListHomesCommand(homesFile));
//        getCommand("delhome").setExecutor(new DelHomeCommand(homesFile));
//
//        // Register warp commands
//        getCommand("setwarp").setExecutor(new SetWarpCommand(warpsFile));
//        getCommand("warp").setExecutor(new WarpCommand(warpsFile, this));
//        getCommand("delwarp").setExecutor(new DelWarpCommand(warpsFile));
//        getCommand("warps").setExecutor(new ListWarpsCommand(warpsFile));
//        getCommand("spawn").setExecutor(new SpawnCommand(warpsFile, this));
//
//        // Register tpa commands
//        getCommand("tpa").setExecutor(new TpaCommand(tpaManager));
//        getCommand("tpahere").setExecutor(new TpahereCommand(tpaManager));
//        getCommand("tpaccept").setExecutor(new TpAcceptCommand(tpaManager, this));
//        getCommand("tpdeny").setExecutor(new TpDenyCommand(tpaManager));
//
//        // Register tab completions for warps and homes and reload
//        getCommand("warp").setTabCompleter(new WarpTabCompleter(warpsFile));
//        getCommand("delwarp").setTabCompleter(new WarpTabCompleter(warpsFile));
//        getCommand("home").setTabCompleter(new HomeTabCompleter(homesFile));
//        getCommand("delhome").setTabCompleter(new HomeTabCompleter(homesFile));
//        getCommand("systemhomes").setTabCompleter(new SystemHomesTabCompleter());
    }

    void registerCommands(){
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new SystemHomesCommand());
        manager.registerCommand(new HomeCommands(homesFile, this));
    }

    private void createHomesFile() {
        homesFile = new File(getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            try {
                if (homesFile.getParentFile().mkdirs()) {
                    getLogger().info("Created plugin data directory.");
                }
                if (homesFile.createNewFile()) {
                    getLogger().info("Created homes.yml file.");
                }
            } catch (IOException e) {
                getLogger().severe("Could not create homes.yml file!");
                e.printStackTrace();
            }
        }

    }
    private void createWarpsFile() {
        warpsFile = new File(getDataFolder(), "warps.yml");

        if (!warpsFile.exists()) {
            try {
                if (warpsFile.getParentFile().mkdirs()) {
                    getLogger().info("Created plugin data directory.");
                }
                if (warpsFile.createNewFile()) {
                    getLogger().info("Created warps.yml file.");
                }
            } catch (IOException e) {
                getLogger().severe("Could not create warps.yml file!");
                e.printStackTrace();
            }
        }

    }

}
