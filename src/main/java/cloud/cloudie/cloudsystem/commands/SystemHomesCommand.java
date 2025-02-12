package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import cloud.cloudie.cloudsystem.SystemHomes;


@CommandAlias("systemhomes")
public class SystemHomesCommand extends BaseCommand {

    @Default
    @Subcommand("reload")
    @CommandPermission("systemhomes.admin.reload")
    public void reload(CommandSender sender) {
        SystemHomes.plugin.reloadConfig();
        sender.sendMessage(Component.text("SystemHomes configuration reloaded successfully!")
                .color(TextColor.fromHexString("#55FF55")));
    }
}
