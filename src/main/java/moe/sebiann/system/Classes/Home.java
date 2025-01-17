package moe.sebiann.system.Classes;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Home {

    String name;
    UUID owningPlayer;
    Location location;

    //<editor-fold desc="Constructor Overloading">
    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The UUID of the player who owns the home
     * @param location The location where the home is
     */
    public Home(String name, UUID owningPlayer, Location location) {
        this.name = name;
        this.owningPlayer = owningPlayer;
        this.location = location;
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param location The location where the home is
     */
    public Home(String name, OfflinePlayer owningPlayer, Location location) {
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
        this.location = location;
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     */
    public Home(String name, OfflinePlayer owningPlayer, float x, float y, float z) {
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
        this.location = new Location(x,y,z);
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player UUID who owns the home
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     */
    public Home(String name, UUID owningPlayer, float x, float y, float z) {
        this.name = name;
        this.owningPlayer = owningPlayer;
        this.location = new Location(x,y,z);
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     * @param yaw The yaw
     * @param pitch The pitch
     */
    public Home(String name, OfflinePlayer owningPlayer, float x, float y, float z, float yaw, float pitch) {
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
        this.location = new Location(x,y,z,yaw,pitch);
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player UUID who owns the home
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     * @param yaw The yaw
     * @param pitch The pitch
     */
    public Home(String name, UUID owningPlayer, float x, float y, float z, float yaw, float pitch) {
        this.name = name;
        this.owningPlayer = owningPlayer;
        this.location = new Location(x,y,z,yaw,pitch);
    }
    //</editor-fold>
}
