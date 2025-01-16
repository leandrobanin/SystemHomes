package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeCommands extends BaseCommand {

    private final File homesFile;
    private FileConfiguration homesConfig;
    private final JavaPlugin plugin;
    private final Map<UUID, String> pendingConfirmations = new HashMap<>();

    public HomeCommands(File homesFile, JavaPlugin plugin) {
        this.homesFile = homesFile;
        this.homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        this.plugin = plugin;
    }

    @CommandAlias("sethome")
    public void setHome(CommandSender sender, String[] args) {
        //checks
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        // Reload the configuration to reflect recent updates
        FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        if (args.length < 1) {
            player.sendMessage("§cPlease specify a home name: /sethome <name>");
            return;
        }
        String homeName = args[0].toLowerCase(); // Normalize the home name
        String playerPath = "homes." + player.getUniqueId() + "." + homeName;

        // Check if the home already exists
        if (homesConfig.contains(playerPath)) {
            // If confirmation is pending, override the home
            if (pendingConfirmations.containsKey(player.getUniqueId()) &&
                    pendingConfirmations.get(player.getUniqueId()).equals(homeName)) {

                pendingConfirmations.remove(player.getUniqueId());
                saveHome(player, homeName);
                player.sendMessage("§aHome '" + homeName + "' has been overridden!");
                return;
            }

            // Prompt for confirmation
            pendingConfirmations.put(player.getUniqueId(), homeName);
            player.sendMessage("§cA home named '" + homeName + "' already exists. Use /sethome <name> again to override it.");
            return;
        }
        // Save the home if it doesn't already exist
        saveHome(player, homeName);
        player.sendMessage("§aHome '" + homeName + "' has been set!");
        return;
    }

    private void saveHome(Player player, String homeName) {
        Location location = player.getLocation();
        String path = "homes." + player.getUniqueId() + "." + homeName;

        homesConfig.set(path + ".world", location.getWorld().getName());
        homesConfig.set(path + ".x", location.getX());
        homesConfig.set(path + ".y", location.getY());
        homesConfig.set(path + ".z", location.getZ());
        homesConfig.set(path + ".yaw", location.getYaw());
        homesConfig.set(path + ".pitch", location.getPitch());

        saveHomesFile();
    }

    private void saveHomesFile() {
        try {
            homesConfig.save(homesFile);
            homesConfig = YamlConfiguration.loadConfiguration(homesFile); // Reload the configuration
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save homes.yml file!");
            e.printStackTrace();
        }
    }
}