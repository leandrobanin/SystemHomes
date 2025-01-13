package moe.sebiann.system.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DelHomeCommand implements CommandExecutor {

    private final File homesFile;

    public DelHomeCommand(File homesFile) {
        this.homesFile = homesFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration homesConfig = YamlConfiguration.loadConfiguration(homesFile);

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a home name: /delhome <name>");
                return true;
            }

            String homeName = args[0].toLowerCase(); // Normalize the home name
            String playerPath = "homes." + player.getUniqueId();

            // Check if the player has the specified home
            if (!homesConfig.contains(playerPath + "." + homeName)) {
                player.sendMessage("§cHome '" + homeName + "' does not exist!");
                return true;
            }

            // Remove the home from the configuration
            homesConfig.set(playerPath + "." + homeName, null);

            // Save the updated file
            try {
                homesConfig.save(homesFile);
                homesConfig = YamlConfiguration.loadConfiguration(homesFile); // Reload the configuration
                player.sendMessage("§aHome '" + homeName + "' has been deleted!");
            } catch (IOException e) {
                player.sendMessage("§cAn error occurred while deleting the home.");
                e.printStackTrace();
            }

            return true;
        }
        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
