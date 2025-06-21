package cloud.cloudie.cloudsystem.commands;

import cloud.cloudie.cloudsystem.SystemHomes;
import cloud.cloudie.cloudsystem.classes.Home;
import cloud.cloudie.cloudsystem.classes.Location;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class HomeCommands implements CommandExecutor, TabCompleter {

    private final SystemHomes plugin;
    private final Map<UUID, String> pendingConfirmations = new HashMap<>();

    public HomeCommands(SystemHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return true;
        }

        switch (command.getName().toLowerCase()) {
            case "sethome" -> handleSetHome(player, args);
            case "delhome" -> handleDelHome(player, args);
            case "home" -> handleHome(player, args);
            case "homes" -> handleHomes(player);
        }
        return true;
    }

    private void handleSetHome(Player player, String[] args) {
        String homeName = (args.length < 1) ? "home" : args[0].toLowerCase();
        String playerPath = "homes." + player.getUniqueId() + "." + homeName;

        boolean isCreatingNewHome = !Home.containsHome(playerPath);
        if (isCreatingNewHome) {
            int currentHomes = Home.getPlayerHomes(player.getUniqueId()).size();
            int homeLimit = determineHomeLimit(player);
            if (currentHomes >= homeLimit) {
                player.sendMessage(Component.text("You have reached your limit of ").color(TextColor.fromHexString("#FF5555"))
                        .append(Component.text(homeLimit).color(TextColor.fromHexString("#FFAA00")))
                        .append(Component.text(" homes!").color(TextColor.fromHexString("#FF5555"))));
                return;
            }
        }

        Home home = new Home(homeName, player.getUniqueId(), new Location(player.getLocation()));

        if (Home.containsHome(playerPath)) {
            if (pendingConfirmations.containsKey(player.getUniqueId()) && pendingConfirmations.get(player.getUniqueId()).equals(homeName)) {
                pendingConfirmations.remove(player.getUniqueId());
                home.uploadHome();
                player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#55FF55"))
                        .append(Component.text(homeName).color(TextColor.fromHexString("#00AA00")))
                        .append(Component.text(" has been overridden!").color(TextColor.fromHexString("#55FF55"))));
            } else {
                pendingConfirmations.put(player.getUniqueId(), homeName);
                player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#FF5555"))
                        .append(Component.text(homeName).color(TextColor.fromHexString("#AA0000")))
                        .append(Component.text(" already exists! Use: ").color(TextColor.fromHexString("#FF5555")))
                        .append(Component.text("/sethome " + homeName).color(TextColor.fromHexString("#AA0000"))
                                .hoverEvent(HoverEvent.showText(Component.text("Click this to override your home!")
                                        .color(TextColor.fromHexString("#FF5555")))).clickEvent(ClickEvent.runCommand("/sethome " + homeName)))
                        .append(Component.text(" again to override it!").color(TextColor.fromHexString("#FF5555"))));
            }
            return;
        }
        home.uploadHome();
        player.sendMessage(Component.text("Home ")
                .color(TextColor.fromHexString("#55FF55")).append(Component.text(homeName)
                .color(TextColor.fromHexString("#00AA00"))).append(Component.text(" has been set to this location!")
                .color(TextColor.fromHexString("#55FF55"))));
    }

    private void handleDelHome(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(Component.text("Please put in your home which you want to delete").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        String homeName = args[0].toLowerCase();
        try {
            Home.getHome(player.getUniqueId(), homeName).deleteHome();
            player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#FF5555")).append(Component.text(homeName).color(TextColor.fromHexString("#AA0000"))).append(Component.text(" has been deleted!").color(TextColor.fromHexString("#FF5555"))));
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("This home does not exist.").color(TextColor.fromHexString("#FF5555")));
        }
    }

    private void handleHome(Player player, String[] args) {
        String homeName = (args.length < 1) ? "home" : args[0].toLowerCase();
        int delayInSeconds = plugin.getConfig().getInt("home.teleport_delay", 2);
        Home home;
        try {
            home = Home.getHome(player.getUniqueId(), homeName);
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("This home does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        org.bukkit.Location location = home.getLocation().toBukkitLocation();
        player.sendMessage(Component.text("Teleporting you to home: ").color(TextColor.fromHexString("#55FF55")).append(Component.text(home.getHomeName()).color(TextColor.fromHexString("#00AA00"))).append(Component.text(" in " + delayInSeconds + " seconds.").color(TextColor.fromHexString("#55FF55"))));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(location);
            player.sendMessage(Component.text("Teleported you to your home: ").color(TextColor.fromHexString("#55FF55")).append(Component.text(homeName).color(TextColor.fromHexString("#00AA00"))));
        }, delayInSeconds * 20L);
    }

    private void handleHomes(Player player) {
        List<Home> homes = Home.getPlayerHomes(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(Component.text("You have no homes set.").color(TextColor.fromHexString("#FFAA00")));
            return;
        }
        Component component = Component.text("Your Homes (" + homes.size() + "):\n").color(TextColor.fromHexString("#FFAA00"));
        for (Home home : homes) {
            component = component.append(Component.text(home.getHomeName()).color(TextColor.fromHexString("#FFAA00")).clickEvent(ClickEvent.runCommand("/home " + home.getHomeName())).hoverEvent(HoverEvent.showText(Component.text("Teleport to home: " + home.getHomeName()).color(TextColor.fromHexString("#FFAA00"))))).append(Component.text(" - ").color(TextColor.fromHexString("#AAAAAA"))).append(Component.text("(" + home.world.replace("world", "Overworld").replace("_nether", " Nether").replace("_the_end", " The End") + "; " + (int) home.x + ", " + (int) home.y + ", " + (int) home.z + ")\n").color(TextColor.fromHexString("#55FFFF")).clickEvent(ClickEvent.runCommand("/home " + home.getHomeName())).hoverEvent(HoverEvent.showText(Component.text("Teleport to home: " + home.getHomeName()).color(TextColor.fromHexString("#FFAA00")))));
        }
        player.sendMessage(component);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            String commandName = command.getName().toLowerCase();
            if ((commandName.equals("home") || commandName.equals("delhome")) && args.length == 1) {
                List<String> homeNames = Home.getPlayerHomes(player.getUniqueId()).stream()
                        .map(Home::getHomeName)
                        .collect(Collectors.toList());
                return homeNames.stream()
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private int determineHomeLimit(Player player) {
        int highestLimitFound = -1;
        for (var pai : player.getEffectivePermissions()) {
            String perm = pai.getPermission().toLowerCase();
            if (perm.startsWith("systemhomes.home.max.")) {
                try {
                    int limit = Integer.parseInt(perm.substring(perm.lastIndexOf('.') + 1));
                    if (limit > highestLimitFound) {
                        highestLimitFound = limit;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return (highestLimitFound != -1) ? highestLimitFound : Integer.MAX_VALUE;
    }
}