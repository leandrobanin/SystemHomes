package moe.sebiann.system.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SystemHomesCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public SystemHomesCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("systemhomes.admin.reload")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage("§aSystemHomes configuration reloaded successfully!");
        } else {
            sender.sendMessage("§cUsage: /systemhomes reload");
        }

        return true;
    }
}
