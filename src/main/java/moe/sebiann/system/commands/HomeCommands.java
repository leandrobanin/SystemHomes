package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import moe.sebiann.system.Classes.Home;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeCommands extends BaseCommand {

    private final Map<UUID, String> pendingConfirmations = new HashMap<>();

    @CommandAlias("sethome")
    public void setHome(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String homeName;
        if (args.length < 1) {
            homeName = "home";
        }else{
            homeName = args[0].toLowerCase();
        }

        String playerPath = "homes." + player.getUniqueId() + "." + homeName;

        Home home = new Home(
                homeName,
                player.getUniqueId(),
                new moe.sebiann.system.Classes.Location(player.getLocation())
        );

        if (Home.containsHome(playerPath)) {
            if (pendingConfirmations.containsKey(player.getUniqueId()) &&
                    pendingConfirmations.get(player.getUniqueId()).equals(homeName)) {

                pendingConfirmations.remove(player.getUniqueId());
                home.uploadHome();

                player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#55FF55"))
                        .append(Component.text(homeName).color(TextColor.fromHexString("#00AA00")))
                        .append(Component.text(" has been overridden!").color(TextColor.fromHexString("#55FF55"))));
                return;
            }

            pendingConfirmations.put(player.getUniqueId(), homeName);
            player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#FF5555"))
                    .append(Component.text(homeName).color(TextColor.fromHexString("#AA0000")))
                    .append(Component.text(" already exists! Use: ").color(TextColor.fromHexString("#FF5555")))
                    .append(Component.text("/sethome " + homeName).color(TextColor.fromHexString("#AA0000"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click this to override your home!").color(TextColor.fromHexString("#FF5555"))))
                            .clickEvent(ClickEvent.runCommand("/sethome " + homeName)))
                    .append(Component.text(" again to override it!").color(TextColor.fromHexString("#FF5555"))));
            return;
        }

        home.uploadHome();
        player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(homeName).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" has been set to this location!").color(TextColor.fromHexString("#55FF55"))));
    }
}