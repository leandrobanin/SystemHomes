package moe.sebiann.system.commands.marriage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class MarriageManager {

    private final File marriagesFile;
    private final Gson gson;
    private List<Marriage> marriages;
    private final Map<UUID, UUID> proposals = new HashMap<>();
    private final Map<UUID, UUID> adoptionRequests = new HashMap<>(); // Key: Child, Value: Parent

    public MarriageManager(File dataFolder) {
        this.marriagesFile = new File(dataFolder, "marriages.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.marriages = new ArrayList<>();

        loadMarriages();
    }
    // Add an adoption request
    public void addAdoptionRequest(UUID parent, UUID child) {
        adoptionRequests.put(child, parent);
    }

    // Get the parent for a pending adoption
    public UUID getAdoptionRequester(UUID child) {
        return adoptionRequests.get(child);
    }

    // Remove an adoption request
    public void removeAdoptionRequest(UUID child) {
        adoptionRequests.remove(child);
    }

    // Check if a child has a pending adoption request
    public boolean hasAdoptionRequest(UUID child) {
        return adoptionRequests.containsKey(child);
    }
    // Proposal Management
    public void addProposal(UUID proposer, UUID proposed) {
        proposals.put(proposed, proposer);
    }

    public UUID getProposer(UUID proposed) {
        return proposals.get(proposed);
    }

    public void removeProposal(UUID proposed) {
        proposals.remove(proposed);
    }

    public boolean hasProposal(UUID proposed) {
        return proposals.containsKey(proposed);
    }

    // Marriage Management
    public void addMarriage(Marriage marriage) {
        marriages.add(marriage);
        saveMarriages();
    }

    public void removeMarriage(UUID player1, UUID player2) {
        marriages.removeIf(marriage ->
                (marriage.getP().equals(player1.toString()) && marriage.getP2().equals(player2.toString())) ||
                        (marriage.getP().equals(player2.toString()) && marriage.getP2().equals(player1.toString()))
        );
        saveMarriages(); // Ensure changes are saved to the JSON file
    }


    public List<Marriage> getAllMarriages() {
        return new ArrayList<>(marriages);
    }

    private void loadMarriages() {
        if (!marriagesFile.exists()) {
            saveMarriages(); // Create an empty file if it doesn't exist
            return;
        }

        try (FileReader reader = new FileReader(marriagesFile)) {
            Type listType = new TypeToken<List<Marriage>>() {}.getType();
            marriages = gson.fromJson(reader, listType);
            if (marriages == null) {
                marriages = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Could not load marriages.json!");
            e.printStackTrace();
        }
    }

    public void saveMarriages() {
        try (FileWriter writer = new FileWriter(marriagesFile)) {
            gson.toJson(marriages, writer);
        } catch (IOException e) {
            System.err.println("Could not save marriages.json!");
            e.printStackTrace();
        }
    }

    public UUID getSpouse(UUID player) {
        return marriages.stream()
                .filter(marriage -> marriage.getP().equals(player.toString()) || marriage.getP2().equals(player.toString()))
                .map(marriage -> marriage.getP().equals(player.toString()) ? UUID.fromString(marriage.getP2()) : UUID.fromString(marriage.getP()))
                .findFirst()
                .orElse(null);
    }
    public Marriage getMarriage(UUID player1, UUID player2) {
        for (Marriage marriage : marriages) {
            if ((marriage.getP().equals(player1.toString()) && marriage.getP2().equals(player2.toString())) ||
                    (marriage.getP().equals(player2.toString()) && marriage.getP2().equals(player1.toString()))) {
                return marriage;
            }
        }
        return null;
    }

}
