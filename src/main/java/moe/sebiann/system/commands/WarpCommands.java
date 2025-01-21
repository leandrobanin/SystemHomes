package moe.sebiann.system.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import moe.sebiann.system.Classes.Location;
import moe.sebiann.system.Classes.Warp;
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

@CommandPermission("systemhomes.player.warp")
public class WarpCommands extends BaseCommand {

    List<String> pendingOverwrittenConfirmations = new ArrayList<>();

    @CommandAlias("setwarp")
    @CommandCompletion("@nothing")
    @CommandPermission("systemhomes.admin.warp")
    public void setWarp(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        if(args.length < 1){
            player.sendMessage(Component.text("Please give a name to this warp.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String warpPath = "warps." + args[0];
        Warp warp = new Warp(
                args[0],
                new Location(player.getLocation())
        );

        if(Warp.containsWarp(warpPath)) {
            if(pendingOverwrittenConfirmations.contains(args[0])) {

                try{
                    warp.uploadWarp();
                    pendingOverwrittenConfirmations.remove(args[0]);
                }catch(Exception e) {
                    player.sendMessage(Component.text("Failed to upload Warp, please try again later.").color(TextColor.fromHexString("#FF5555")));
                    return;
                }

                player.sendMessage(Component.text("Warp ").color(TextColor.fromHexString("#55FF55"))
                        .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                        .append(Component.text(" has been overridden!").color(TextColor.fromHexString("#55FF55"))));
                return;
            }

            pendingOverwrittenConfirmations.add(args[0]);
            player.sendMessage(Component.text("Warp ").color(TextColor.fromHexString("#FF5555"))
                    .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#AA0000")))
                    .append(Component.text(" already exists! Use: ").color(TextColor.fromHexString("#FF5555")))
                    .append(Component.text("/setwarp " + warp.getWarpName()).color(TextColor.fromHexString("#AA0000"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click this to override this warp!").color(TextColor.fromHexString("#FF5555"))))
                            .clickEvent(ClickEvent.runCommand("/setwarp " + warp.getWarpName())))
                    .append(Component.text(" again to override it!").color(TextColor.fromHexString("#FF5555"))));
            return;
        }

        try{
            warp.uploadWarp();
        }catch(Exception e) {
            player.sendMessage(Component.text("Failed to upload Warp, please try again later.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        player.sendMessage(Component.text("Warp ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" has been set to this location!").color(TextColor.fromHexString("#55FF55"))));
    }

    @CommandAlias("delwarp")
    @CommandCompletion("@warpNames")
    @CommandPermission("systemhomes.admin.warp")
    public void delWarp(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please put in the warp which you want to delete").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        String warpName = args[0].toLowerCase();
        Warp warp;
        try{
            warp = Warp.getWarp(warpName);
        } catch (RuntimeException e) {
            player.sendMessage(Component.text("This warp does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        try{
            warp.deleteWarp();
        }catch (RuntimeException e) {
            player.sendMessage(Component.text("This warp could not be deleted, please try again later.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        player.sendMessage(Component.text("Warp ").color(TextColor.fromHexString("#FF5555"))
                .append(Component.text(warpName).color(TextColor.fromHexString("#AA0000")))
                .append(Component.text(" has been deleted!").color(TextColor.fromHexString("#FF5555"))));
    }

    @CommandAlias("warp")
    @CommandCompletion("@warpNames")
    public void warp(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please input to which warp you want to teleport.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        int delayInSeconds = SystemHomes.plugin.getConfig().getInt("warp.teleport_delay", 2);

        Warp warp;
        try{
            warp = Warp.getWarp(args[0].toLowerCase());
        }catch (RuntimeException e){
            player.sendMessage(Component.text("This warp does not exist.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        org.bukkit.Location location = warp.getLocation().toBukkitLocation();

        player.sendMessage(Component.text("Teleporting you to warp: ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" in " + delayInSeconds + " seconds.").color(TextColor.fromHexString("#55FF55"))));

        Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> {
            player.teleport(location);
            player.sendMessage(Component.text("Teleported you to your warp: ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#00AA00"))));
        }, delayInSeconds * 20L);
    }

    @CommandAlias("warps")
    @CommandCompletion("@nothing")
    public void warpList(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        List<Warp> warps = Warp.getWarps();

        Component component = Component.text("The servers warps (" + warps.size() + "):\n").color(TextColor.fromHexString("#FFAA00"));
        for (Warp warp : warps) {
            component = component.append(Component.text(warp.getWarpName()).color(TextColor.fromHexString("#FFAA00"))
                    .clickEvent(ClickEvent.runCommand("/warp " + warp.getWarpName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to warp: " + warp.getWarpName()).color(TextColor.fromHexString("#FFAA00")))));
            component = component.append(Component.text(" - ").color(TextColor.fromHexString("#AAAAAA")));

            String worldName = switch (warp.world) {
                case "world" -> "Overworld";
                case "world_nether" -> "Nether";
                case "world_the_end" -> "The End";
                default -> warp.world;
            };

            component = component.append(Component.text("(" + worldName + "; " + (int) warp.x + ", " + (int) warp.y + ", " + (int) warp.z + ")\n").color(TextColor.fromHexString("#55FFFF"))
                    .clickEvent(ClickEvent.runCommand("/warp " + warp.getWarpName()))
                    .hoverEvent(HoverEvent.showText(Component.text("Teleport to warp: " + warp.getWarpName()).color(TextColor.fromHexString("#FFAA00")))));
        }

        player.sendMessage(component);
    }

    public static List<String> warpNameToString(){
        List<String> warpNameList = new ArrayList<>();
        List<Warp> warps = Warp.getWarps();

        for(Warp w : warps){
            warpNameList.add(w.getWarpName());
        }
        return warpNameList;
    }

}
