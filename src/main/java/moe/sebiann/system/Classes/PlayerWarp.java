package moe.sebiann.system.Classes;

import moe.sebiann.system.SystemHomes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerWarp extends Warp{

    UUID owningPlayer;

    public PlayerWarp(String warpName, UUID owningPlayer, org.bukkit.Location location) {
        super(warpName, location);
        this.owningPlayer = owningPlayer;
    }

    public PlayerWarp(String warpName,  UUID owningPlayer, Location location) {
        super(warpName, location);
        this.owningPlayer = owningPlayer;
    }

    public PlayerWarp(String warpName, UUID owningPlayer, String world, float x, float y, float z, float yaw, float pitch) {
        super(warpName, world, x, y, z, yaw, pitch);
        this.owningPlayer = owningPlayer;
    }

    public PlayerWarp(String warpName, UUID owningPlayer, String world, float x, float y, float z) {
        super(warpName, world, x, y, z);
        this.owningPlayer = owningPlayer;
    }

    public UUID getOwningPlayer() {
        return owningPlayer;
    }

    /**
     * Creates the YAML FileConfig of this class
     * @return FileConfiguration of the YAML
     */
    @SuppressWarnings("DuplicatedCode")
    private FileConfiguration toYamlConfiguration(){
        File warpFile = new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml");

        if (!warpFile.exists()) {
            try {
                if (warpFile.getParentFile().mkdirs()) {
                    SystemHomes.plugin.getLogger().info("Created plugin data directory.");
                }
                if (warpFile.createNewFile()) {
                    SystemHomes.plugin.getLogger().info("Created playerwarps.yml file.");
                }
            } catch (IOException e) {
                SystemHomes.plugin.getLogger().severe("Could not create playerwarps.yml file!");
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(warpFile);
        String path = "warps." + warpName;

        config.set(path + ".world", world);
        config.set(path + ".player", owningPlayer.toString());
        config.set(path + ".x", x);
        config.set(path + ".y", y);
        config.set(path + ".z", z);
        config.set(path + ".yaw", yaw);
        config.set(path + ".pitch", pitch);
        return config;
    }

    /**
     * @return the YAML string from this class
     */
    public String toYamlString(){
        return toYamlConfiguration().toString();
    }

    /**
     * Adds the warp to the playerwarps.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void uploadPlayerWarp(){
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml");
        try {
            toYamlConfiguration().save(warpsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a home from the playerwarps.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void deletePlayerWarp() {
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);
        String path = "warps." + warpName;

        if (config.contains(path)) {
            config.set(path, null);

            try {
                config.save(warpsFile);
            } catch (IOException e) {
                throw new RuntimeException("Could not save updated playerwarps.yml file", e);
            }
        }
    }

    /**
     * Gets the home object from a YAML config file
     * @param config The YAML FileConfiguration
     * @param pathPrefix the prefix of what to search
     * @return Home object
     */
    private static PlayerWarp getPlayerWarpFromConfig(FileConfiguration config, String pathPrefix){
        String[] pathSegments = pathPrefix.split("\\.");
        String name = pathSegments[pathSegments.length - 1];

        return new PlayerWarp(
                name,
                UUID.fromString(config.getString(pathPrefix + ".player")),
                config.getString(pathPrefix + ".world"),
                Float.parseFloat(config.getString(pathPrefix + ".x")),
                Float.parseFloat(config.getString(pathPrefix + ".y")),
                Float.parseFloat(config.getString(pathPrefix + ".z")),
                Float.parseFloat(config.getString(pathPrefix + ".yaw")),
                Float.parseFloat(config.getString(pathPrefix + ".pitch"))
        );
    }

    /**
     * Gets the home object from a YAML string
     * @param yamlString the YAML string of the object
     * @return Home object
     */
    public static PlayerWarp fromYamlString(String yamlString) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new StringReader(yamlString));
        String pathPrefix = "warps.";
        return getPlayerWarpFromConfig(config, pathPrefix);
    }

    /**
     * Gets the specified warp
     * @param name The home name
     * @return The home object
     */
    public static PlayerWarp getPlayerWarp(String name) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml"));
        String pathPrefix = "warps." + name;

        if (!config.contains(pathPrefix)) {
            throw new IllegalArgumentException("Warp '" + name + "' does not exist in the file.");
        }

        return getPlayerWarpFromConfig(config, pathPrefix);
    }

    public static List<PlayerWarp> getPlayerWarps(){
        List<PlayerWarp> warps = new ArrayList<>();
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);

        String path = "warps.";

        ConfigurationSection section = config.getConfigurationSection(path);
        if(section == null){
            return warps;
        }

        Set<String> warpNames = section.getKeys(false);
        for (String warpName : warpNames) {
            String warpPath = path + warpName;
            PlayerWarp warp = getPlayerWarpFromConfig(config, warpPath);
            warps.add(warp);
        }

        return warps;
    }

    public static boolean containsPlayerWarp(String warpPath){
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "playerwarps.yml"));
        return config.contains(warpPath);
    }
}
