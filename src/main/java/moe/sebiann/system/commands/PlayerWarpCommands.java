package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import moe.sebiann.system.Classes.Location;
import moe.sebiann.system.Classes.PlayerWarp;
import moe.sebiann.system.SystemHomes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandPermission("systemhomes.player.pwarp")
public class PlayerWarpCommands extends BaseCommand {

    List<String> pendingOverwrittenConfirmations = new ArrayList<>();

    @CommandAlias("setplayerwarp|setpwarp")
    @CommandCompletion("@nothing")
    public void setPlayerWarp(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1){
            player.sendMessage(Component.text("Please give a name to this PlayerWarp.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String warpPath = "warps." + args[0];
        PlayerWarp warp = new PlayerWarp(
                args[0],
                player.getUniqueId(),
                new Location(player.getLocation())
        );

        if(PlayerWarp.containsPlayerWarp(warpPath)) {
            if(pendingOverwrittenConfirmations.contains(args[0])) {
                try{
                    warp.uploadPlayerWarp();
                    pendingOverwrittenConfirmations.remove(args[0]);
                }catch(Exception e) {
                    player.sendMessage(Component.text("Failed to upload PlayerWarp, please try again later.").color(TextColor.fromHexString("#FF5555")));
                    return;
                }

                player.sendMessage(Component.text("PlayerWarp ").color(TextColor.fromHexString("#55FF55"))
                        .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                        .append(Component.text(" has been overridden!").color(TextColor.fromHexString("#55FF55"))));
                return;
            }

            pendingOverwrittenConfirmations.add(args[0]);
            player.sendMessage(Component.text("PlayerWarp ").color(TextColor.fromHexString("#FF5555"))
                    .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#AA0000")))
                    .append(Component.text(" already exists! Use: ").color(TextColor.fromHexString("#FF5555")))
                    .append(Component.text("/setpwarp " + warp.getWarpName()).color(TextColor.fromHexString("#AA0000"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click this to override this warp!").color(TextColor.fromHexString("#FF5555"))))
                            .clickEvent(ClickEvent.runCommand("/setpwarp " + warp.getWarpName())))
                    .append(Component.text(" again to override it!").color(TextColor.fromHexString("#FF5555"))));
            return;
        }

        int warpsMade = 0;
        int maxWarps = SystemHomes.plugin.getConfig().getInt("pwarp.max_warps", 3);
        for(PlayerWarp w : PlayerWarp.getPlayerWarps()){
            if(w.getOwningPlayer().equals(player.getUniqueId())){
                warpsMade++;
                if(warpsMade >= maxWarps){
                    player.sendMessage(Component.text("You already have the maximum amount of player warps set.").color(TextColor.fromHexString("#FF5555")));
                    return;
                }
            }
        }

        try{
            warp.uploadPlayerWarp();
        }catch(Exception e) {
            player.sendMessage(Component.text("Failed to upload PlayerWarp, please try again later.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        player.sendMessage(Component.text("PlayerWarp ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" has been set to this location!").color(TextColor.fromHexString("#55FF55"))));
    }

    @CommandAlias("delplayerwarp|delpwarp|remplayerwarp|rempwarp")
    @CommandCompletion("@pwarpNames")
    public void delPlayerWarp(CommandSender sender, String[] args){
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please put in the warp which you want to delete").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String warpName = args[0].toLowerCase();
        PlayerWarp warp;
        try{
            warp = PlayerWarp.getPlayerWarp(warpName);
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("This warp does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        if(!warp.getOwningPlayer().equals(player.getUniqueId()) && !player.hasPermission("systemhomes.admin.pwarp")){
            player.sendMessage(Component.text("You can not delete this PlayerWarp as it does not belong to you!").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        try{
            warp.deletePlayerWarp();
        }catch (RuntimeException e) {
            player.sendMessage(Component.text("This warp could not be deleted, please try again later.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        player.sendMessage(Component.text("PlayerWarp ").color(TextColor.fromHexString("#FF5555"))
                .append(Component.text(warpName).color(TextColor.fromHexString("#AA0000")))
                .append(Component.text(" has been deleted!").color(TextColor.fromHexString("#FF5555"))));

    }

    @CommandAlias("playerwarp|pwarp")
    @CommandCompletion("@pwarpNames")
    public void playerWarp(CommandSender sender, String[] args){
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please input to which warp you want to teleport.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        int delayInSeconds = SystemHomes.plugin.getConfig().getInt("pwarp.teleport_delay", 2);

        PlayerWarp warp;
        try{
            warp = PlayerWarp.getPlayerWarp(args[0].toLowerCase());
        }catch (RuntimeException e){
            player.sendMessage(Component.text("This warp does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        org.bukkit.Location location = warp.getLocation().toBukkitLocation();

        player.sendMessage(Component.text("Teleporting you to PlayerWarp: ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" in " + delayInSeconds + " seconds.").color(TextColor.fromHexString("#55FF55"))));

        Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> {
            player.teleport(location);
            player.sendMessage(Component.text("Teleported you to PlayerWarp: ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00"))));
        }, delayInSeconds * 20L);

    }

    @CommandAlias("playerwarps|pwarps")
    public void getPlayerWarps(CommandSender sender){
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        List<PlayerWarp> warps = PlayerWarp.getPlayerWarps();

        Component component = Component.text("The server's PlayerWarps:\n").color(TextColor.fromHexString("#FFAA00"));
        for (PlayerWarp warp : warps) {
            component = component.append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#FFAA00"))
                    .clickEvent(ClickEvent.runCommand("/pwarp " + warp.getWarpName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to PlayerWarp: " + warp.getWarpName()).color(TextColor.fromHexString("#FFAA00")))));
            component = component.append(Component.text(" - ").color(TextColor.fromHexString("#AAAAAA")));

            String worldName = switch (warp.world) {
                case "world" -> "Overworld";
                case "world_nether" -> "Nether";
                case "world_the_end" -> "The End";
                default -> warp.world;
            };

            component = component.append(Component.text("(" + Bukkit.getOfflinePlayer(warp.getOwningPlayer()).getName() + "; " + worldName + ", " + (int) warp.x + ", " + (int) warp.y + ", " + (int) warp.z + ")\n").color(TextColor.fromHexString("#55FFFF"))
                    .clickEvent(ClickEvent.runCommand("/pwarp " + warp.getWarpName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to PlayerWarp: " + warp.getWarpName()).color(TextColor.fromHexString("#FFAA00")))));
        }

        player.sendMessage(component);

    }

    public static List<String> pwarpNameToString(){
        List<String> warpNameList = new ArrayList<>();
        List<PlayerWarp> warps = PlayerWarp.getPlayerWarps();

        for(PlayerWarp w : warps){
            warpNameList.add(w.getWarpName());
        }
        return warpNameList;
    }

}
