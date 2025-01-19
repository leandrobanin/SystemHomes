package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import moe.sebiann.system.Classes.Warp;
import moe.sebiann.system.SystemHomes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

    @CommandAlias("spawn")
    @SuppressWarnings("DuplicatedCode")
    public void spawn(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        Warp spawn;
        try{
            spawn = Warp.getWarp("spawn");
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("The spawn warp is not yet set.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        org.bukkit.Location location = spawn.getLocation().toBukkitLocation();
        int delayInSeconds = SystemHomes.plugin.getConfig().getInt("warp.teleport_delay", 2);

        player.sendMessage(Component.text("Teleporting you to warp: ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(spawn.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" in " + delayInSeconds + " seconds.").color(TextColor.fromHexString("#55FF55"))));

        Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> {
            player.teleport(location);
            player.sendMessage(Component.text("Teleported you to your warp: ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(spawn.getWarpName()).color(TextColor.fromHexString("#00AA00"))));
        }, delayInSeconds * 20L);
    }
}
