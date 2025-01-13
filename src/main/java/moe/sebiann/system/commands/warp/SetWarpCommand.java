package moe.sebiann.system.commands.warp;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

// SetWarpCommand
public class SetWarpCommand implements CommandExecutor {
    private final File warpsFile;
    private final Map<UUID, String> pendingConfirmations = new HashMap<>();

    public SetWarpCommand(File warpsFile) {
        this.warpsFile = warpsFile;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Reload the configuration to reflect recent updates
            FileConfiguration warpsConfig = YamlConfiguration.loadConfiguration(warpsFile);

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a warp name: /setwarp <name>");
                return true;
            }

            String warpName = args[0].toLowerCase(); // Normalize warp name
            Location location = player.getLocation();

            String path = "warps." + warpName;

            // Confirmation logic
            if (warpsConfig.contains(path)) {
                if (pendingConfirmations.containsKey(player.getUniqueId()) &&
                        pendingConfirmations.get(player.getUniqueId()).equals(warpName)) {
                    pendingConfirmations.remove(player.getUniqueId());
                } else {
                    pendingConfirmations.put(player.getUniqueId(), warpName);
                    player.sendMessage("§cWarp '" + warpName + "' already exists. Use /setwarp again to override.");
                    return true;
                }
            }

            warpsConfig.set(path + ".world", location.getWorld().getName());
            warpsConfig.set(path + ".x", location.getX());
            warpsConfig.set(path + ".y", location.getY());
            warpsConfig.set(path + ".z", location.getZ());
            warpsConfig.set(path + ".yaw", location.getYaw());
            warpsConfig.set(path + ".pitch", location.getPitch());

            try {
                warpsConfig.save(warpsFile);
                player.sendMessage("§aWarp '" + warpName + "' has been set!");
            } catch (IOException e) {
                player.sendMessage("§cAn error occurred while saving the warp.");
                e.printStackTrace();
            }
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
