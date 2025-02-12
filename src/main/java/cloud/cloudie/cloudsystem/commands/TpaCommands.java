package cloud.cloudie.cloudsystem.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import cloud.cloudie.cloudsystem.Classes.TpaRequest;
import cloud.cloudie.cloudsystem.Enums.TpaType;
import cloud.cloudie.cloudsystem.SystemHomes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandPermission("systemhomes.player.tpa")
public class TpaCommands extends BaseCommand {

    List<TpaRequest> tpaRequests = new ArrayList<>();

    @CommandAlias("tpa")
    @CommandCompletion("@players")
    public void tpa(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please mention a player you'd want to teleport to.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(Component.text("You can not teleport to an offline player.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You can not teleport to yourself.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        for(TpaRequest r : tpaRequests) {
            if(r.getRequester().equals(player.getUniqueId())) {
                player.sendMessage(Component.text("You already have an outgoing TPA request to this player").color(TextColor.fromHexString("#FF5555")));
                return;
            }
        }

        TpaRequest tpaRequest = new TpaRequest(player.getUniqueId(), target.getUniqueId());
        tpaRequests.add(tpaRequest);

        int timeoutTime = SystemHomes.plugin.getConfig().getInt("warp.request_timeout", 30);
        deleteTpaAfterDelay(player, target, timeoutTime, tpaRequest);

        player.sendMessage(Component.text("Teleport request sent to ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(target.getName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(".\nRequest will time out in ").color(TextColor.fromHexString("#55FF55")))
                .append(Component.text(timeoutTime).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

        target.sendMessage(Component.text(player.getName()).color(TextColor.fromHexString("#00AA00"))
                .append(Component.text(" has requested to teleport to you.").color(TextColor.fromHexString("#55FF55")))
                .append(Component.text("\nWrite /tpaccept to accept this request.").color(TextColor.fromHexString("#55FF55"))
                        .clickEvent(ClickEvent.runCommand("/tpaccept"))
                        .hoverEvent(HoverEvent.showText(Component.text("Accepts the TPA request").color(TextColor.fromHexString("#55FF55")))))
                .append(Component.text("\nWrite /tpdeny to deny this request.").color(TextColor.fromHexString("#FF5555"))
                        .clickEvent(ClickEvent.runCommand("/tpdeny"))
                        .hoverEvent(HoverEvent.showText(Component.text("Denies the TPA request").color(TextColor.fromHexString("#FF5555"))))));
    }

    @CommandAlias("tpahere")
    @CommandCompletion("@players")
    @SuppressWarnings("DuplicatedCode")
    public void tpahere(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(args.length < 1) {
            player.sendMessage(Component.text("Please mention a player you'd want to teleport to.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(Component.text("You can not teleport to an offline player.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        if(target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("You can not teleport to yourself.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        TpaRequest tpaRequest = new TpaRequest(player.getUniqueId(), target.getUniqueId(), TpaType.TPA_HERE);
        tpaRequests.add(tpaRequest);

        int timeoutTime = SystemHomes.plugin.getConfig().getInt("tpa.request_timeout", 30);
        deleteTpaAfterDelay(player, target, timeoutTime, tpaRequest);

        player.sendMessage(Component.text("Teleport request sent to ").color(TextColor.fromHexString("#55FF55"))
                .append(Component.text(target.getName()).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(".\nRequest will time out in ").color(TextColor.fromHexString("#55FF55")))
                .append(Component.text(timeoutTime).color(TextColor.fromHexString("#00AA00")))
                .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

        target.sendMessage(Component.text(player.getName()).color(TextColor.fromHexString("#00AA00"))
                .append(Component.text(" has requested you to teleport to them.").color(TextColor.fromHexString("#55FF55")))
                .append(Component.text("\nWrite /tpaccept to accept this request.").color(TextColor.fromHexString("#55FF55"))
                        .clickEvent(ClickEvent.runCommand("/tpaccept"))
                        .hoverEvent(HoverEvent.showText(Component.text("Accepts the TPA request").color(TextColor.fromHexString("#55FF55")))))
                .append(Component.text("\nWrite /tpdeny to deny this request.").color(TextColor.fromHexString("#FF5555"))
                        .clickEvent(ClickEvent.runCommand("/tpdeny"))
                        .hoverEvent(HoverEvent.showText(Component.text("Denies the TPA request").color(TextColor.fromHexString("#FF5555"))))));
    }

    @CommandAlias("tpaccept|tpyes")
    @SuppressWarnings("DuplicatedCode")
    public void tpaccept(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        TpaRequest tpaRequest = null;
        for(TpaRequest r : tpaRequests) {
            if(r.getTarget().equals(player.getUniqueId())) {
                tpaRequest = r;
                break;
            }
        }
        if(tpaRequest == null){
            player.sendMessage(Component.text("You don't have any outgoing TPA requests.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        Player target = Bukkit.getPlayer(tpaRequest.getRequester());

        if(target == null) {
            player.sendMessage(Component.text("Player is no longer online.").color(TextColor.fromHexString("#FF5555")));
            tpaRequests.remove(tpaRequest);
            return;
        }
        int tpTime = SystemHomes.plugin.getConfig().getInt("tpa.teleport_delay", 2);
        tpaRequests.remove(tpaRequest);

        if (tpaRequest.getType() == TpaType.TPA_HERE) {
            player.sendMessage(Component.text("TPA request to ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(target.getName()).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" has been accepted.\nYou will be teleported in ").color(TextColor.fromHexString("#55FF55")))
                    .append(Component.text(tpTime).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

            target.sendMessage(Component.text("The TPA request from ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(player.getName()).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" has been accepted.\nThey will be teleported to you in ").color(TextColor.fromHexString("#55FF55")))
                    .append(Component.text(tpTime).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

            Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> player.teleport(target), tpTime * 20L);

        } else if (tpaRequest.getType() == TpaType.TPA_THERE) {
            target.sendMessage(Component.text("TPA request to ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(player.getName()).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" has been accepted.\nYou will be teleported in ").color(TextColor.fromHexString("#55FF55")))
                    .append(Component.text(tpTime).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

            player.sendMessage(Component.text("The TPA request from ").color(TextColor.fromHexString("#55FF55"))
                    .append(Component.text(target.getName()).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" has been accepted.\nThey will be teleported to you in ").color(TextColor.fromHexString("#55FF55")))
                    .append(Component.text(tpTime).color(TextColor.fromHexString("#00AA00")))
                    .append(Component.text(" seconds.").color(TextColor.fromHexString("#55FF55"))));

            Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> target.teleport(player), tpTime * 20L);
        }
    }

    @CommandAlias("tpdeny|tpno")
    @SuppressWarnings("DuplicatedCode")
    public void tpdeny(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only a player can run this command.").color(TextColor.fromHexString("#FF5555")));
            return;
        }

        TpaRequest tpaRequest = null;
        for(TpaRequest r : tpaRequests) {
            if(r.getTarget().equals(player.getUniqueId())) {
                tpaRequest = r;
                break;
            }
        }
        if(tpaRequest == null){
            player.sendMessage(Component.text("You don't have any outgoing TPA requests.").color(TextColor.fromHexString("#FF5555")));
            return;
        }
        Player target = Bukkit.getPlayer(tpaRequest.getRequester());
        OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(tpaRequest.getRequester());

        tpaRequests.remove(tpaRequest);

        player.sendMessage(Component.text("Declined ").color(TextColor.fromHexString("#FF5555"))
                .append(Component.text(targetOffline.getName() == null ? "Unknown" : targetOffline.getName()).color(TextColor.fromHexString("#AA0000")))
                .append(Component.text("'s TPA request.").color(TextColor.fromHexString("#FF5555"))));

        if(target != null){
            target.sendMessage(Component.text(player.getName()).color(TextColor.fromHexString("#AA0000"))
                    .append(Component.text(" declined your TPA request.").color(TextColor.fromHexString("#FF5555"))));
        }
    }

    private void deleteTpaAfterDelay(Player player, Player target, int requestTimeout, TpaRequest tpaRequest) {
        Bukkit.getScheduler().runTaskLater(SystemHomes.plugin, () -> {
            if(tpaRequests.remove(tpaRequest)){
                player.sendMessage(Component.text("TPA Request to ").color(TextColor.fromHexString("#FF5555"))
                        .append(Component.text(target.getName()).color(TextColor.fromHexString("#AA0000")))
                        .append(Component.text(" has timed out.").color(TextColor.fromHexString("#FF5555"))));

                target.sendMessage(Component.text("TPA Request from ").color(TextColor.fromHexString("#FF5555"))
                        .append(Component.text(player.getName()).color(TextColor.fromHexString("#AA0000")))
                        .append(Component.text(" has timed out.").color(TextColor.fromHexString("#FF5555"))));
            }
        }, requestTimeout * 20L);
    }

}
