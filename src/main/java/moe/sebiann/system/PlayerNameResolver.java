package moe.sebiann.system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerNameResolver {

    public static String getPlayerName(UUID uuid) {
        // Check if the player is online
        Player onlinePlayer = Bukkit.getPlayer(uuid);
        if (onlinePlayer != null) {
            return onlinePlayer.getName();
        }

        // Check if the player has joined the server before
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer != null && offlinePlayer.getName() != null) {
            return offlinePlayer.getName();
        }

        // Fallback: Fetch name from Mojang API
        return fetchPlayerNameFromMojang(uuid);
    }

    private static String fetchPlayerNameFromMojang(UUID uuid) {
        try {
            String uuidNoDashes = uuid.toString().replace("-", "");
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuidNoDashes + "/names");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonArray nameHistory = JsonParser.parseReader(reader).getAsJsonArray();

                // Get the last element of the name history
                JsonObject latestName = nameHistory.get(nameHistory.size() - 1).getAsJsonObject();
                return latestName.get("name").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

}
