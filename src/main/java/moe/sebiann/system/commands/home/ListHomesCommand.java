package moe.sebiann.system.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ListHomesCommand implements CommandExecutor {

    private final File homesFile;

    public ListHomesCommand(File homesFile) {
        this.homesFile = homesFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

            String playerPath = "homes." + player.getUniqueId();

            // Check if the player has any homes
            if (!homesConfig.contains(playerPath)) {
                player.sendMessage("§cYou don't have any homes set!");
                return true;
            }

            // Get the list of home names
            var homesSection = homesConfig.getConfigurationSection(playerPath);
            if (homesSection == null || homesSection.getKeys(false).isEmpty()) {
                player.sendMessage("§cYou don't have any homes set!");
                return true;
            }

            // Send the list of home names with dimensions to the player
            player.sendMessage("§aYour homes:");
            int totalHomes = 0; // Counter for total homes
            for (String homeName : homesSection.getKeys(false)) {
                String world = homesConfig.getString(playerPath + "." + homeName + ".world", "Unknown");
                player.sendMessage(" - §e" + homeName + " §7(in §b" + world + "§7)");
                totalHomes++;
            }
            // Display the total number of homes
            player.sendMessage("§aTotal homes: §e" + totalHomes);
            return true;
        }
        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
