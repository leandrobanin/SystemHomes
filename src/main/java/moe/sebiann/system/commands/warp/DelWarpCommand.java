package moe.sebiann.system.commands.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class DelWarpCommand implements CommandExecutor {
    private final File warpsFile;

    public DelWarpCommand(File warpsFile) {
        this.warpsFile = warpsFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a warp name: /delwarp <name>");
                return true;
            }

            String warpName = args[0].toLowerCase(); // Normalize warp name
            String path = "warps." + warpName;

            if (!warpsConfig.contains(path)) {
                player.sendMessage("§cWarp '" + warpName + "' does not exist!");
                return true;
            }

            warpsConfig.set(path, null);

            try {
                warpsConfig.save(warpsFile);
                player.sendMessage("§aWarp '" + warpName + "' has been deleted!");
            } catch (IOException e) {
                player.sendMessage("§cAn error occurred while deleting the warp.");
                e.printStackTrace();
            }

            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
