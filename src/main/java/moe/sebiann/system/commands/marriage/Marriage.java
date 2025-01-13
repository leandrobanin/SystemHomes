package moe.sebiann.system.commands.marriage;

import java.util.ArrayList;
import java.util.List;

public class Marriage {
    private String p; // UUID of player 1
    private String p2; // UUID of player 2
    private String type; // Marriage type (e.g., "STRAIGHT", "GAY", "LESBIAN")
    private List<String> children; // UUIDs of children
    private long marriedSince; // Timestamp of marriage

    // Getters and setters
    public String getP() { return p; }
    public void setP(String p) { this.p = p; }

    public String getP2() { return p2; }
    public void setP2(String p2) { this.p2 = p2; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getChildren() { return children; }
    public void setChildren(List<String> children) { this.children = children; }
    public void addChild(String childUuid) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childUuid);
    }

    public void removeChild(String childUuid) {
        if (children != null) {
            children.remove(childUuid);
        }
    }
    public long getMarriedSince() { return marriedSince; }
    public void setMarriedSince(long marriedSince) { this.marriedSince = marriedSince; }
}
