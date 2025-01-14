package moe.sebiann.system.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SpawnCommand implements CommandExecutor {

    private final FileConfiguration warpsConfig;
    private final JavaPlugin plugin;

    public SpawnCommand(File warpsFile, JavaPlugin plugin) {
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            String path = "warps.spawn";

            if (!warpsConfig.contains(path)) {
                player.sendMessage("§cThe spawn warp is not set!");
                return true;
            }

            String worldName = warpsConfig.getString(path + ".world");
            double x = warpsConfig.getDouble(path + ".x");
            double y = warpsConfig.getDouble(path + ".y");
            double z = warpsConfig.getDouble(path + ".z");
            float yaw = (float) warpsConfig.getDouble(path + ".yaw");
            float pitch = (float) warpsConfig.getDouble(path + ".pitch");

            if (worldName == null || Bukkit.getWorld(worldName) == null) {
                player.sendMessage("§cThe world for the spawn warp does not exist!");
                return true;
            }

            Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            // Configurable delay
            int delayInSeconds = plugin.getConfig().getInt("warp.teleport_delay", 2); // Default 2 seconds
            long delayInTicks = delayInSeconds * 20L;

            // Inform the player about the teleport delay
            player.sendMessage("§eTeleporting to spawn in " + delayInSeconds + " seconds...");

            // Schedule the teleport
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.teleport(spawnLocation);
                player.sendMessage("§aYou have been teleported to spawn!");
            }, delayInTicks);
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
