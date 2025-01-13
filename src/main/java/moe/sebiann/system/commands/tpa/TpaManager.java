package moe.sebiann.system.commands.tpa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {
    private final Map<UUID, UUID> pendingRequests = new HashMap<>();
    private final Map<UUID, UUID> tpahereRequests = new HashMap<>();

    public void addTpaRequest(UUID requester, UUID recipient) {
        pendingRequests.put(recipient, requester);
    }

    public UUID getTpaRequester(UUID recipient) {
        return pendingRequests.get(recipient);
    }

    public void removeTpaRequest(UUID recipient) {
        pendingRequests.remove(recipient);
    }

    public boolean hasTpaRequest(UUID recipient) {
        return pendingRequests.containsKey(recipient);
    }

    public void addTpahereRequest(UUID requester, UUID recipient) {
        tpahereRequests.put(recipient, requester);
    }

    public UUID getTpahereRequester(UUID recipient) {
        return tpahereRequests.get(recipient);
    }

    public void removeTpahereRequest(UUID recipient) {
        tpahereRequests.remove(recipient);
    }

    public boolean hasTpahereRequest(UUID recipient) {
        return tpahereRequests.containsKey(recipient);
    }
}
