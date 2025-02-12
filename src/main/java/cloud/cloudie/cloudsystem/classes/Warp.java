package cloud.cloudie.cloudsystem.classes;

import cloud.cloudie.cloudsystem.SystemHomes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Warp extends Location{

    String warpName;

    //<editor-fold desc="Constructors">
    /**
     * Creates a warp object
     * @param warpName The name of the warp
     * @param location The location of the warp
     */
    public Warp(String warpName, Location location) {
        super(location);
        this.warpName = warpName;
    }

    /**
     * Creates a warp object
     * @param warpName The name of the warp
     * @param location The bukkit location of the warp
     */
    public Warp(String warpName, org.bukkit.Location location) {
        super(location);
        this.warpName = warpName;
    }

    /**
     * Creates a warp object
     * @param warpName The name of the warp
     * @param world The world of the warp
     * @param x The X-Coordinate of the warp
     * @param y The Y-Coordinate of the warp
     * @param z The Z-Coordinate of the warp
     */
    public Warp(String warpName, String world, float x, float y, float z) {
        super(world, x, y, z);
        this.warpName = warpName;
    }

    /**
     *
     * Creates a warp object
     * @param warpName The name of the warp
     * @param world The world of the warp
     * @param x The X-Coordinate of the warp
     * @param y The Y-Coordinate of the warp
     * @param z The Z-Coordinate of the warp
     * @param yaw The yaw of the warp
     * @param pitch The pitch of the warp
     */
    public Warp(String warpName, String world, float x, float y, float z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
        this.warpName = warpName;
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">

    /**
     * @return Gets the warp name
     */
    public String getWarpName() {
        return warpName;
    }

    /**
     * Sets the warp name
     * @param warpName New name for this warp
     */
    @SuppressWarnings("unused")
    public void setWarpName(String warpName) {
        this.warpName = warpName;
    }
    //</editor-fold>


    /**
     * Creates the YAML FileConfig of this class
     * @return FileConfiguration of the YAML
     */
    @SuppressWarnings("DuplicatedCode")
    private FileConfiguration toYamlConfiguration(){
        File warpFile = new File(SystemHomes.plugin.getDataFolder(), "warps.yml");

        if (!warpFile.exists()) {
            try {
                if (warpFile.getParentFile().mkdirs()) {
                    SystemHomes.plugin.getLogger().info("Created plugin data directory.");
                }
                if (warpFile.createNewFile()) {
                    SystemHomes.plugin.getLogger().info("Created warps.yml file.");
                }
            } catch (IOException e) {
                SystemHomes.plugin.getLogger().severe("Could not create warps.yml file!" + e.getMessage());
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(warpFile);
        String path = "warps." + warpName;

        config.set(path + ".world", world);
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
    @SuppressWarnings("unused")
    public String toYamlString(){
        return toYamlConfiguration().toString();
    }

    /**
     * Adds the warp to the warps.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void uploadWarp(){
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "warps.yml");
        try {
            toYamlConfiguration().save(warpsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a home from the warps.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void deleteWarp() {
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "warps.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);
        String path = "warps." + warpName;

        if (config.contains(path)) {
            config.set(path, null);

            try {
                config.save(warpsFile);
            } catch (IOException e) {
                throw new RuntimeException("Could not save updated warps.yml file", e);
            }
        }
    }

    /**
     * Gets the home object from a YAML config file
     * @param config The YAML FileConfiguration
     * @param pathPrefix the prefix of what to search
     * @return Home object
     */
    @SuppressWarnings("ConstantConditions")
    private static Warp getWarpFromConfig(FileConfiguration config, String pathPrefix){
        String[] pathSegments = pathPrefix.split("\\.");
        String name = pathSegments[pathSegments.length - 1];

        return new Warp(
                name,
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
    @SuppressWarnings("unused")
    public static Warp fromYamlString(String yamlString) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new StringReader(yamlString));
        String pathPrefix = "warps.";
        return getWarpFromConfig(config, pathPrefix);
    }

    /**
     * Gets the specified warp
     * @param name The home name
     * @return The home object
     */
    public static Warp getWarp(String name) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "warps.yml"));
        String pathPrefix = "warps." + name;

        if (!config.contains(pathPrefix)) {
            throw new IllegalArgumentException("Warp '" + name + "' does not exist in the file.");
        }

        return getWarpFromConfig(config, pathPrefix);
    }

    public static List<Warp> getWarps(){
        List<Warp> warps = new ArrayList<>();
        File warpsFile = new File(SystemHomes.plugin.getDataFolder(), "warps.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(warpsFile);

        String path = "warps.";

        ConfigurationSection section = config.getConfigurationSection(path);
        if(section == null){
            return warps;
        }

        Set<String> warpNames = section.getKeys(false);
        for (String warpName : warpNames) {
            String warpPath = path + warpName;
            Warp warp = getWarpFromConfig(config, warpPath);
            warps.add(warp);
        }

        return warps;
    }

    public static boolean containsWarp(String warpPath){
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "warps.yml"));
        return config.contains(warpPath);
    }
}
