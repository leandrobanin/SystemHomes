package moe.sebiann.system.Classes;

import org.bukkit.Bukkit;

import java.io.Serializable;

public class Location implements Serializable {

    public float x;
    public float y;
    public float z;
    public float yaw = 0;
    public float pitch = 0;
    public String world;

    //<editor-fold desc="Constructor Overloading">
    /**
     * Creates a location
     * @param location An already existing location
     */
    public Location(Location location) {
        this.world = location.world;
        this.x = location.x;
        this.y = location.y;
        this.z = location.z;

        if(location.yaw != 0){
            this.yaw = location.yaw;
        }
        if(location.pitch != 0){
            this.pitch = location.pitch;
        }
    }

    public Location(org.bukkit.Location location) {
        this.world = location.getWorld().getName();
        this.x = (float) location.getX();
        this.y = (float) location.getY();
        this.z = (float) location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    /**
     * Creates a new location
     * @param world Name of the world this location is in
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     */
    public Location(String world, float x, float y, float z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * Creates a new location
     * @param world Name of the world this location is in
     * @param x X-Coordinate
     * @param y Y-Coordinate
     * @param z Z-Coordinate
     * @param yaw Yaw
     * @param pitch Pitch
     */
    public Location(String world, float x, float y, float z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    //</editor-fold>

    public Location getLocation(){
        return this;
    }

    public org.bukkit.Location toBukkitLocation() {
        return new org.bukkit.Location(
                Bukkit.getWorld(world),
                x, y, z,
                pitch, yaw
        );
    }
}
