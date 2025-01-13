package moe.sebiann.system.commands.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyCommand implements CommandExecutor {

    private final TpaManager tpaManager;

    public TpDenyCommand(TpaManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player recipient) {
            if (!tpaManager.hasTpaRequest(recipient.getUniqueId())) {
                recipient.sendMessage("§cYou don't have any teleport requests!");
                return true;
            }

            Player requester = Bukkit.getPlayer(tpaManager.getTpaRequester(recipient.getUniqueId()));
            if (requester != null && requester.isOnline()) {
                requester.sendMessage("§cYour teleport request to " + recipient.getName() + " was denied.");
            }

            tpaManager.removeTpaRequest(recipient.getUniqueId());
            recipient.sendMessage("§aYou denied the teleport request.");
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
