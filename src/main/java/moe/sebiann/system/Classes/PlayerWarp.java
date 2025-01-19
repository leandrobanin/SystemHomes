package moe.sebiann.system.Classes;

import java.util.UUID;

public class PlayerWarp extends Warp{

    UUID owningPlayer;

    public PlayerWarp(String warpName, org.bukkit.Location location) {
        super(warpName, location);
    }

    public PlayerWarp(String warpName, Location location) {
        super(warpName, location);
    }

    public PlayerWarp(String warpName, String world, float x, float y, float z, float yaw, float pitch) {
        super(warpName, world, x, y, z, yaw, pitch);
    }

    public PlayerWarp(String warpName, String world, float x, float y, float z) {
        super(warpName, world, x, y, z);
    }
}
