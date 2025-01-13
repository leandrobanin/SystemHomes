package moe.sebiann.system.commands.marriage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarryTabCompleter implements TabCompleter {

    private final MarriageManager marriageManager;

    public MarryTabCompleter(MarriageManager marriageManager) {
        this.marriageManager = marriageManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            // Suggest main subcommands
            suggestions = new ArrayList<>(Arrays.asList("propose", "accept", "deny", "list", "kiss", "divorce", "tp"));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("propose")) {
            // Suggest online players for /marry propose
            for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    suggestions.add(onlinePlayer.getName());
                }
            }
        }

        // Filter suggestions based on the partial input
        String partialInput = args[args.length - 1].toLowerCase();
        suggestions.removeIf(suggestion -> !suggestion.toLowerCase().startsWith(partialInput));

        return suggestions;
    }
}
