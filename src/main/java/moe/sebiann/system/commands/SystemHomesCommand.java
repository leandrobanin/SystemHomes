package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import moe.sebiann.system.SystemHomes;


@CommandAlias("systemhomes")
public class SystemHomesCommand extends BaseCommand {

    @Default
    @Subcommand("reload")
    @CommandPermission("systemhomes.admin")
    public void reload(CommandSender sender, String[] args) {
        SystemHomes.plugin.reloadConfig();
        sender.sendMessage("§aSystemHomes configuration reloaded successfully!");
    }
}
