package moe.sebiann.system.Classes;

public class Location {

    float x;
    float y;
    float z;
    float yaw;
    float pitch;

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(float x, float y, float z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getZ(){
        return z;
    }
}
