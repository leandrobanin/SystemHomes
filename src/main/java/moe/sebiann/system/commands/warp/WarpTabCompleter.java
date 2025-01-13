package moe.sebiann.system.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {

    private final File warpsFile;
    private FileConfiguration warpsConfig;

    public WarpTabCompleter(File warpsFile) {
        this.warpsFile = warpsFile;
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Reload the configuration at the beginning
        this.warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);

        if (args.length == 1) {
            // Get all warp names for suggestion
            var warpsSection = warpsConfig.getConfigurationSection("warps");
            if (warpsSection != null) {
                List<String> warps = new ArrayList<>(warpsSection.getKeys(false));
                // Filter based on partial input
                String partial = args[0].toLowerCase();
                warps.removeIf(warp -> !warp.startsWith(partial));
                return warps;
            }
        }

        return Collections.emptyList(); // No suggestions
    }
}
