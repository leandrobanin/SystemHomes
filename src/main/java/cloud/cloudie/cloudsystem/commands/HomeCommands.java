package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import cloud.cloudie.cloudsystem.Classes.Home;
import cloud.cloudie.cloudsystem.Classes.Location;
import cloud.cloudie.cloudsystem.SystemHomes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

@CommandPermission("systemhomes.player.home")
public class HomeCommands extends BaseCommand {

    private final Map<UUID, String> pendingConfirmations = new HashMap<>();

    @CommandAlias("sethome")
    @CommandCompletion("@nothing")
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
                new Location(player.getLocation())
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

    @CommandAlias("delhome")
    @CommandCompletion("@homeNames")
    public void delHome(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        if(args.length < 1) {
            player.sendMessage(Component.text("Please put in your home which you want to delete").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String homeName = args[0].toLowerCase();
        Home home;
        try{
            home = Home.getHome(player.getUniqueId(), homeName);
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("This home does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        
        try{
            home.deleteHome();
        }catch (RuntimeException e) {
            player.sendMessage(Component.text("This home could not be deleted, please try again later.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        player.sendMessage(Component.text("Home ").color(TextColor.fromHexString("#FF5555"))
                .append(Component.text(homeName).color(TextColor.fromHexString("#AA0000")))
                .append(Component.text(" has been deleted!").color(TextColor.fromHexString("#FF5555"))));
    }

    @CommandAlias("home")
    @CommandCompletion("@homeNames")
    public void home(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String homeName;
        if (args.length < 1) {
            homeName = "home";
        } else {
            homeName = args[0].toLowerCase();
        }

        int delayInSeconds = SystemHomes.plugin.getConfig().getInt("home.teleport_delay", 2);

        Home home;
        try{
            home = Home.getHome(player.getUniqueId(), homeName);
        }catch (RuntimeException e){
            player.sendMessage(Component.text("This home does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        org.bukkit.Location location = home.getLocation().toBukkitLocation();

        player.sendMessage(Component.text("Teleporting you to home: ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(home.getHomeName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" in " + delayInSeconds + " seconds.").color(TextColor.fromHexString("#55FF55"))));

        Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> {
            player.teleport(location);
            player.sendMessage(Component.text("Teleported you to your home: ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(homeName).color(TextColor.fromHexString("#00AA00"))));
        }, delayInSeconds * 20L);
    }

    @CommandAlias("homes")
    public void homeList(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        List<Home> homes = Home.getPlayerHomes(player.getUniqueId());

        Component component = Component.text("Your Homes (" + homes.size() + "):\n").color(TextColor.fromHexString("#FFAA00"));
        for (Home home : homes) {
            component = component.append(Component.text(home.getHomeName()).color(TextColor.fromHexString("#FFAA00"))
                    .clickEvent(ClickEvent.runCommand("/home " + home.getHomeName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to home: " + home.getHomeName()).color(TextColor.fromHexString("#FFAA00")))));
            component = component.append(Component.text(" - ").color(TextColor.fromHexString("#AAAAAA")));

            String worldName = switch (home.world) {
                case "world" -> "Overworld";
                case "world_nether" -> "Nether";
                case "world_the_end" -> "The End";
                default -> home.world;
            };

            component = component.append(Component.text("(" + worldName + "; " + (int) home.x + ", " + (int) home.y + ", " + (int) home.z + ")\n").color(TextColor.fromHexString("#55FFFF"))
                    .clickEvent(ClickEvent.runCommand("/home " + home.getHomeName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to home: " + home.getHomeName()).color(TextColor.fromHexString("#FFAA00")))));
        }

        player.sendMessage(component);
    }

    public static List<String> homeNameToString(String playerName){
        List<String> homeNameList = new ArrayList<>();
        List<Home> homes = Home.getPlayerHomes(Bukkit.getOfflinePlayer(playerName).getUniqueId());

        for(Home h : homes){
            homeNameList.add(h.getHomeName());
        }
        return homeNameList;
    }

}