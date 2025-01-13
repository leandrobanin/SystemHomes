package moe.sebiann.system.commands.home;

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

public class HomeCommand implements CommandExecutor {

    private final File homesFile;
    private final JavaPlugin plugin;

    public HomeCommand(File homesFile, JavaPlugin plugin) {
        this.homesFile = homesFile;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);
            String homeName = "home"; // Default to "home"
            if (args.length > 0) {
                homeName = args[0].toLowerCase(); // Use the specified home name
            }

            String path = "homes." + player.getUniqueId() + "." + homeName;

            if (!homesConfig.contains(path)) {
                if (args.length == 0) {
                    player.sendMessage("§cYou must specify a home name or set a default home named 'home'!");
                } else {
                    player.sendMessage("§cHome '" + homeName + "' does not exist!");
                }
                return true;
            }
            final String finalHomeName = homeName; // Declare as final for use in lambda
            String worldName = homesConfig.getString(path + ".world");
            double x = homesConfig.getDouble(path + ".x");
            double y = homesConfig.getDouble(path + ".y");
            double z = homesConfig.getDouble(path + ".z");
            float yaw = (float) homesConfig.getDouble(path + ".yaw");
            float pitch = (float) homesConfig.getDouble(path + ".pitch");

            if (worldName == null || Bukkit.getWorld(worldName) == null) {
                player.sendMessage("§cUnable to teleport: World does not exist.");
                return true;
            }

            final Location finalHomeLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch); // Final for lambda

            // Read the delay from the config
            int delayInSeconds = plugin.getConfig().getInt("home.teleport_delay", 2); // Default to 2 seconds
            long delayInTicks = delayInSeconds * 20L; // Convert seconds to ticks

            player.sendMessage("§eTeleporting to '" + finalHomeName + "' in " + delayInSeconds + " seconds...");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.teleport(finalHomeLocation);
                player.sendMessage("§aYou have been teleported to '" + finalHomeName + "'!");
            }, delayInTicks);
            return true;
        }
        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
