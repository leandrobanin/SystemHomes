package moe.sebiann.system.commands.home;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {

    private final File homesFile;
    private FileConfiguration homesConfig;

    public HomeTabCompleter(File homesFile) {
        this.homesFile = homesFile;
        this.homesConfig = YamlConfiguration.loadConfiguration(homesFile);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Reload the configuration at the beginning
        this.homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        if (args.length == 1) {
            // Get all warp names for suggestion
            var homesSection = (homesConfig.getConfigurationSection("homes") != null ? homesConfig.getConfigurationSection("homes") : homesConfig.createSection("homes"));
            if (homesSection != null) {
                if (sender instanceof org.bukkit.entity.Player player) {
                    var playerSection = homesSection.getConfigurationSection(player.getUniqueId().toString());
                    if (playerSection != null) {
                        List<String> homes = new ArrayList<>(playerSection.getKeys(false));
                        // Filter based on partial input
                        String partial = args[0].toLowerCase();
                        homes.removeIf(home -> !home.startsWith(partial));
                        return homes;
                    }
                }
            }
        }

        return Collections.emptyList(); // No suggestions
    }
}
