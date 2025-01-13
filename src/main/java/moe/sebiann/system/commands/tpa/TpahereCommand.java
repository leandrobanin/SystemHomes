package moe.sebiann.system.commands.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereCommand implements CommandExecutor {

    private final TpaManager tpaManager;

    public TpahereCommand(TpaManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player requester) {
            if (args.length < 1) {
                requester.sendMessage("§cPlease specify a player to teleport here: /tpahere <player>");
                return true;
            }

            Player recipient = Bukkit.getPlayer(args[0]);
            if (recipient == null || !recipient.isOnline()) {
                requester.sendMessage("§cPlayer not found or not online!");
                return true;
            }

            if (recipient.equals(requester)) {
                requester.sendMessage("§cYou cannot send a teleport request to yourself!");
                return true;
            }

            tpaManager.addTpahereRequest(requester.getUniqueId(), recipient.getUniqueId());
            requester.sendMessage("§aTeleport request sent to " + recipient.getName() + "!");
            recipient.sendMessage("§e" + requester.getName() + " wants you to teleport to them. Use /tpaccept or /tpdeny.");
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }
}
