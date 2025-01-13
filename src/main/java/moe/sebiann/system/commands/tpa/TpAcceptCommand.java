package moe.sebiann.system.commands.tpa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TpAcceptCommand implements CommandExecutor {

    private final TpaManager tpaManager;
    private final JavaPlugin plugin;

    public TpAcceptCommand(TpaManager tpaManager, JavaPlugin plugin) {
        this.tpaManager = tpaManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player recipient) {
            if (tpaManager.hasTpaRequest(recipient.getUniqueId())) {
                handleTpaRequest(recipient);
            } else if (tpaManager.hasTpahereRequest(recipient.getUniqueId())) {
                handleTpahereRequest(recipient);
            } else {
                recipient.sendMessage("§cYou don't have any teleport requests!");
            }
            return true;
        }

        sender.sendMessage("Only players can use this command!");
        return true;
    }

    private void handleTpaRequest(Player recipient) {
        Player requester = Bukkit.getPlayer(tpaManager.getTpaRequester(recipient.getUniqueId()));
        if (requester == null || !requester.isOnline()) {
            recipient.sendMessage("§cThe player who sent the request is no longer online.");
            tpaManager.removeTpaRequest(recipient.getUniqueId());
            return;
        }

        tpaManager.removeTpaRequest(recipient.getUniqueId());

        // Configurable delay
        int delayInSeconds = plugin.getConfig().getInt("tpa.teleport_delay", 2);
        long delayInTicks = delayInSeconds * 20L;

        // Countdown task
        final int[] secondsLeft = {delayInSeconds}; // Using an array to modify inside lambda
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (secondsLeft[0] > 0) {
                recipient.sendMessage("§eTeleporting " + requester.getName() + " to you in " + secondsLeft[0] + " second(s)...");
                requester.sendMessage("§eTeleporting to " + recipient.getName() + " in " + secondsLeft[0] + " second(s)...");
                secondsLeft[0]--;
            }
        }, 0L, 20L); // Repeats every second

        // Final teleportation task
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getScheduler().cancelTask(taskId); // Stop the countdown
            requester.teleport(recipient.getLocation());
            recipient.sendMessage("§a" + requester.getName() + " has been teleported to you!");
            requester.sendMessage("§aYou have been teleported to " + recipient.getName() + "!");
        }, delayInTicks);
    }

    private void handleTpahereRequest(Player recipient) {
        Player requester = Bukkit.getPlayer(tpaManager.getTpahereRequester(recipient.getUniqueId()));
        if (requester == null || !requester.isOnline()) {
            recipient.sendMessage("§cThe player who sent the request is no longer online.");
            tpaManager.removeTpahereRequest(recipient.getUniqueId());
            return;
        }

        tpaManager.removeTpahereRequest(recipient.getUniqueId());

        // Configurable delay
        int delayInSeconds = plugin.getConfig().getInt("tpa.teleport_delay", 2);
        long delayInTicks = delayInSeconds * 20L;

        // Countdown task
        final int[] secondsLeft = {delayInSeconds}; // Using an array to modify inside lambda
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (secondsLeft[0] > 0) {
                recipient.sendMessage("§eTeleporting to " + requester.getName() + " in " + secondsLeft[0] + " second(s)...");
                requester.sendMessage("§eTeleporting " + recipient.getName() + " to you in " + secondsLeft[0] + " second(s)...");
                secondsLeft[0]--;
            }
        }, 0L, 20L); // Repeats every second

        // Final teleportation task
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.getScheduler().cancelTask(taskId); // Stop the countdown
            recipient.teleport(requester.getLocation());
            recipient.sendMessage("§aYou have been teleported to " + requester.getName() + "!");
            requester.sendMessage("§a" + recipient.getName() + " has teleported to you!");
        }, delayInTicks);
    }
}
