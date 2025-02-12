package cloud.cloudie.cloudsystem.classes;

import cloud.cloudie.cloudsystem.enums.TpaType;

import java.util.UUID;

public class TpaRequest {

    final UUID requester;
    final UUID target;
    final TpaType type;

    //<editor-fold desc="Constructors">
    /**
     * Makes a new TPA-Request (TPA, Not TPA-HERE)
     * @param requester The person who will be teleported
     * @param target The person who will be teleported to
     */
    public TpaRequest(UUID requester, UUID target) {
        this.requester = requester;
        this.target = target;
        this.type = TpaType.TPA_THERE;
    }

    /**
     * Makes a new TPA-Request
     * @param requester The person who will be teleported
     * @param target The person who will be teleported to
     * @param type The type of request it is TPA or TPA-here
     */
    public TpaRequest(UUID requester, UUID target, TpaType type) {
        this.requester = requester;
        this.target = target;
        this.type = type;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    public UUID getRequester() {
        return requester;
    }

    public UUID getTarget() {
        return target;
    }

    public TpaType getType() {
        return type;
    }
    //</editor-fold>
}
