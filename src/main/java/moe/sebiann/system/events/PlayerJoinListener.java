package moe.sebiann.system.events;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final FileConfiguration config;

    public PlayerJoinListener(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String welcomeMessage = config.getString("messages.welcome", "Hello, {player}!");
        String personalizedMessage = welcomeMessage.replace("{player}", event.getPlayer().getName());
        event.getPlayer().sendMessage(Component.text(personalizedMessage));
    }
}
