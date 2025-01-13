package moe.sebiann.system;

import moe.sebiann.system.commands.home.*;
import moe.sebiann.system.commands.warp.*;
import moe.sebiann.system.commands.tpa.*;
import moe.sebiann.system.commands.marriage.*;
import moe.sebiann.system.commands.*;
import moe.sebiann.system.events.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class System extends JavaPlugin {

    private File homesFile;
    private File warpsFile;

    @Override
    public void onEnable() {
        // Save and load the configuration
        saveDefaultConfig();
        // Initialize the homes file
        createHomesFile();
        createWarpsFile();

        TpaManager tpaManager = new TpaManager();
        MarriageManager marriageManager = new MarriageManager(getDataFolder());

        // Register commands
        getCommand("sethome").setExecutor(new SetHomeCommand(homesFile, this));
        getCommand("home").setExecutor(new HomeCommand(homesFile, this));
        getCommand("homes").setExecutor(new ListHomesCommand(homesFile)); // Register list command
        getCommand("delhome").setExecutor(new DelHomeCommand(homesFile)); // Register delete command
        getCommand("setwarp").setExecutor(new SetWarpCommand(warpsFile));
        getCommand("warp").setExecutor(new WarpCommand(warpsFile, this));
        getCommand("delwarp").setExecutor(new DelWarpCommand(warpsFile));
        getCommand("warps").setExecutor(new ListWarpsCommand(warpsFile));
        getCommand("spawn").setExecutor(new SpawnCommand(warpsFile, this));
        getCommand("tpa").setExecutor(new TpaCommand(tpaManager));
        getCommand("tpahere").setExecutor(new TpahereCommand(tpaManager));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(tpaManager, this));
        getCommand("tpdeny").setExecutor(new TpDenyCommand(tpaManager));
        getCommand("marry").setExecutor(new MarryCommand(marriageManager, this));

        // Register tab completions
        getCommand("warp").setTabCompleter(new WarpTabCompleter(warpsFile));
        getCommand("delwarp").setTabCompleter(new WarpTabCompleter(warpsFile));
        getCommand("home").setTabCompleter(new HomeTabCompleter(homesFile));
        getCommand("delhome").setTabCompleter(new HomeTabCompleter(homesFile));
        getCommand("marry").setTabCompleter(new MarryTabCompleter(marriageManager)); // Register the tab completer
        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(getConfig()), this);
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
