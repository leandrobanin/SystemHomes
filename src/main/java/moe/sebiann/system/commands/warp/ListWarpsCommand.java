package moe.sebiann.system.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ListWarpsCommand implements CommandExecutor {
    private final File warpsFile;

    public ListWarpsCommand(File warpsFile) {
        this.warpsFile = warpsFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);

            var warpsSection = warpsConfig.getConfigurationSection("warps");

            if (warpsSection == null || warpsSection.getKeys(false).isEmpty()) {
                player.sendMessage("§cNo warps have been set!");
                return true;
            }

            player.sendMessage("§aAvailable warps:");
            for (String warpName : warpsSection.getKeys(false)) {
                String world = warpsConfig.getString("warps." + warpName + ".world", "Unknown");
                player.sendMessage(" - §e" + warpName + " §7(in §b" + world + "§7)");
            }

            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
