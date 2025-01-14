package moe.sebiann.system.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemHomesTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null; // Tab completion only for players
        }

        List<String> suggestions = new ArrayList<>();

        // Tab complete for `/systemhomes`
        if (args.length == 1) {
            suggestions = Arrays.asList("reload");
        }

        // Filter suggestions based on partial input
        String partialInput = args[args.length - 1].toLowerCase();
        suggestions.removeIf(suggestion -> !suggestion.toLowerCase().startsWith(partialInput));

        return suggestions;
    }
}
