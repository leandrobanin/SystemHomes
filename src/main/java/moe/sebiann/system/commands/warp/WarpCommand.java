package moe.sebiann.system.commands.warp;

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

public class WarpCommand implements CommandExecutor {
    private final File warpsFile;
    private final JavaPlugin plugin;

    public WarpCommand(File warpsFile, JavaPlugin plugin) {
        this.warpsFile = warpsFile;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a warp name: /warp <name>");
                return true;
            }

            String warpName = args[0].toLowerCase(); // Normalize warp name
            String path = "warps." + warpName;

            if (!warpsConfig.contains(path)) {
                player.sendMessage("§cWarp '" + warpName + "' does not exist!");
                return true;
            }

            String worldName = warpsConfig.getString(path + ".world");
            double x = warpsConfig.getDouble(path + ".x");
            double y = warpsConfig.getDouble(path + ".y");
            double z = warpsConfig.getDouble(path + ".z");
            float yaw = (float) warpsConfig.getDouble(path + ".yaw");
            float pitch = (float) warpsConfig.getDouble(path + ".pitch");

            if (worldName == null || Bukkit.getWorld(worldName) == null) {
                player.sendMessage("§cWarp world does not exist!");
                return true;
            }

            Location warpLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            // Read the delay from the config
            int delayInSeconds = plugin.getConfig().getInt("warp.teleport_delay", 3); // Default to 3 seconds
            long delayInTicks = delayInSeconds * 20L;

            player.sendMessage("§eTeleporting to warp '" + warpName + "' in " + delayInSeconds + " seconds...");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.teleport(warpLocation);
                player.sendMessage("§aYou have been teleported to warp '" + warpName + "'!");
            }, delayInTicks);
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}