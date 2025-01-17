package moe.sebiann.system.Classes;

import moe.sebiann.system.SystemHomes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Home extends Location implements Serializable {

    String name;
    UUID owningPlayer;
    boolean isPublic = false;

    //<editor-fold desc="Constructor Overloading">
    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The UUID of the player who owns the home
     * @param location The location where the home is
     */
    public Home(String name, UUID owningPlayer, Location location) {
        super(location);
        this.name = name;
        this.owningPlayer = owningPlayer;
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param location The location where the home is
     */
    public Home(String name, OfflinePlayer owningPlayer, Location location) {
        super(location);
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param worldName The name of the world
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     */
    public Home(String name, OfflinePlayer owningPlayer, String worldName, float x, float y, float z) {
        super(worldName, x,y,z);
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player UUID who owns the home
     * @param worldName The name of the world
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     */
    public Home(String name, UUID owningPlayer, String worldName, float x, float y, float z) {
        super(worldName, x,y,z);
        this.name = name;
        this.owningPlayer = owningPlayer;
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player who owns the home
     * @param worldName The name of the world
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     * @param yaw The yaw
     * @param pitch The pitch
     */
    public Home(String name, OfflinePlayer owningPlayer, String worldName, float x, float y, float z, float yaw, float pitch) {
        super(worldName, x,y,z, yaw, pitch);
        this.name = name;
        this.owningPlayer = owningPlayer.getUniqueId();
    }

    /**
     * Creates a home Object
     * @param name The name of the home
     * @param owningPlayer The player UUID who owns the home
     * @param worldName The name of the world
     * @param x The X-Coordinate
     * @param y The Y-Coordinate
     * @param z The Z-Coordinate
     * @param yaw The yaw
     * @param pitch The pitch
     */
    public Home(String name, UUID owningPlayer, String worldName, float x, float y, float z, float yaw, float pitch) {
        super(worldName, x,y,z, yaw, pitch);
        this.name = name;
        this.owningPlayer = owningPlayer;
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    /**
     * Gets the current home name
     * @return The name of the home
     */
    public String getHomeName() {
        return name;
    }

    /**
     * Sets the name of the home
     * @param name New name of the home
     */
    public void setHomeName(String name) {
        this.name = name;
    }

    /**
     * Gets who owns the home
     * @return UUID of the player
     */
    public UUID getOwner() {
        return owningPlayer;
    }

    /**
     * Gets if the home is public
     * @return publicity of the home
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets the new publicity of this home
     * @param isPublic whether the home is public or not
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    //</editor-fold>

    @Override
    public String toString(){
        return name;
    }

    /**
     * Creates the YAML FileConfig of this class
     * @return FileConfiguration of the YAML
     */
    private FileConfiguration toYamlConfiguration(){
        File homesFile = new File(SystemHomes.plugin.getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            try {
                if (homesFile.getParentFile().mkdirs()) {
                    SystemHomes.plugin.getLogger().info("Created plugin data directory.");
                }
                if (homesFile.createNewFile()) {
                    SystemHomes.plugin.getLogger().info("Created homes.yml file.");
                }
            } catch (IOException e) {
                SystemHomes.plugin.getLogger().severe("Could not create homes.yml file!");
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(homesFile);
        String path = "homes." + owningPlayer + "." + name;

        config.set(path + ".world", world);
        config.set(path + ".public", isPublic);
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
     * Adds the home to the homes.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void uploadHome(){
        File homesFile = new File(SystemHomes.plugin.getDataFolder(), "homes.yml");
        try {
            toYamlConfiguration().save(homesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a home from the homes.yml file
     * @throws RuntimeException May throw a runtime exception if it can not write the file
     */
    public void deleteHome() {
        File homesFile = new File(SystemHomes.plugin.getDataFolder(), "homes.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(homesFile);
        String path = "homes." + owningPlayer + "." + name;

        if (config.contains(path)) {
            config.set(path, null);

            try {
                config.save(homesFile);
            } catch (IOException e) {
                throw new RuntimeException("Could not save updated homes.yml file", e);
            }
        }
    }

    /**
     * Gets the home object from a YAML config file
     * @param config The YAML FileConfiguration
     * @param pathPrefix the prefix of what to search
     * @return Home object
     */
    private static Home getHomeFromConfig(FileConfiguration config, String pathPrefix){
        String[] pathSegments = pathPrefix.split("\\.");
        String owningPlayer = pathSegments[pathSegments.length - 2];
        String name = pathSegments[pathSegments.length - 1];

        Home home = new Home(
                name,
                UUID.fromString(owningPlayer),
                config.getString(pathPrefix + ".world"),
                Float.parseFloat(config.getString(pathPrefix + ".x")),
                Float.parseFloat(config.getString(pathPrefix + ".y")),
                Float.parseFloat(config.getString(pathPrefix + ".z")),
                Float.parseFloat(config.getString(pathPrefix + ".yaw")),
                Float.parseFloat(config.getString(pathPrefix + ".pitch"))
        );

        home.setPublic(config.getBoolean(pathPrefix + ".public", false));
        return home;
    }

    /**
     * Gets the home object from a YAML string
     * @param yamlString the YAML string of the object
     * @return Home object
     */
    public static Home fromYamlString(String yamlString) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new StringReader(yamlString));
        String pathPrefix = "homes.";
        return getHomeFromConfig(config, pathPrefix);
    }

    /**
     * Gets the default home of the player
     * @param owningPlayer The player whose home you want to get
     * @return The home object
     */
    public static Home getHome(UUID owningPlayer) {
        return getHome(owningPlayer, "home");
    }

    /**
     * Gets the specified home of the player
     * @param owningPlayer The player whose home you want to get
     * @param name The home name
     * @return The home object
     */
    public static Home getHome(UUID owningPlayer, String name) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "homes.yml"));
        String pathPrefix = "homes." + owningPlayer + "." + name;

        if (!config.contains(pathPrefix)) {
            throw new IllegalArgumentException("Home '" + name + "' does not exist for player '" + owningPlayer + "' in the file.");
        }

        return getHomeFromConfig(config, pathPrefix);
    }

    public static boolean containsHome(String homePath){
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(SystemHomes.plugin.getDataFolder(), "homes.yml"));
        return config.contains(homePath);
    }

    public static List<Home> getPlayerHomes(UUID owningPlayer){
        List<Home> homes = new ArrayList<Home>();
        File homesFile = new File(SystemHomes.plugin.getDataFolder(), "homes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(homesFile);

        String playerPath = "homes." + owningPlayer.toString();

        ConfigurationSection section = config.getConfigurationSection(playerPath);
        if(section == null){
            return homes;
        }

        Set<String> homeNames = section.getKeys(false);
        for (String homeName : homeNames) {
            String homePath = playerPath + "." + homeName;
            Home home = getHomeFromConfig(config, homePath);
            homes.add(home);
        }

        return homes;
    }

}
